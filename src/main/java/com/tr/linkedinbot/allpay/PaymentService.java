package com.tr.linkedinbot.allpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.linkedinbot.allpay.model.CheckPaymentStatusReq;
import com.tr.linkedinbot.allpay.model.PaymentRequest;
import static com.tr.linkedinbot.allpay.util.PaymentUtil.getApiSignature;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.Payment;
import com.tr.linkedinbot.notifications.events.RequireInteractionNoMessageEvent;
import com.tr.linkedinbot.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentConfig paymentConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher publisher;

    public String createPayLink(PaymentRequest paymentRequest) {
        try {
            paymentRequest.setLogin(paymentConfig.getApiLogin());
            var sign = getApiSignature(paymentRequest, paymentConfig.getApiKey(), objectMapper);
            paymentRequest.setSign(sign);

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var entity = new HttpEntity<>(paymentRequest, headers);
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(paymentConfig.getApiPayUrl(), entity, Map.class);
            var body = responseEntity.getBody();
            return String.valueOf(body.get("payment_url"));
        } catch (Exception e) {
            throw new RuntimeException("Error processing payment: " + e.getMessage());
        }
    }

    public Map<String, String> checkPaymentStatus(Payment payment) {
        CheckPaymentStatusReq req = CheckPaymentStatusReq.builder()
                .login(paymentConfig.getApiLogin())
                .order_id(String.valueOf(payment.getId()))
                .build();
        var sign = getApiSignature(req, paymentConfig.getApiKey(), objectMapper);
        req.setSign(sign);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(req, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(paymentConfig.getApiPayCheckUrl(), entity, Map.class);
        var body = responseEntity.getBody();
        return body;

    }

    @Transactional
    public void savePaymentData(Long chatId, Payment.Status status) {
        log.info("Creating new payment request for chatId: {}", chatId);
        Payment payment = Payment.builder()
                .id(chatId)
                .chatId(chatId)
                .status(status)
                .build();
        paymentRepository.save(payment);
    }

    @Transactional
    public void updatePaymentData(Long orderId, Payment.Status status) {
        Payment payment = paymentRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("Payment not found for orderId: " + orderId));
        payment.setStatus(status);
        paymentRepository.save(payment);
    }

    public boolean checkPaymentStatus(Long chatId) {
        Payment payment = paymentRepository.findPaymentByChatId(chatId).orElse(null);
        if (payment == null) {
            return false;
        }
        return Payment.Status.PAID.equals(payment.getStatus());
    }

    public void handlePaymentStatusCheck() {
        paymentRepository.findPaymentsByStatus(Payment.Status.PENDING)
                .forEach(payment -> {
                    Map<String, String> paymentStatus = checkPaymentStatus(payment);
                    String status = String.valueOf(paymentStatus.get("status"));
                    String sign = paymentStatus.get("sign");
                    String orderId = paymentStatus.get("order_id");
//                    if (status != null && status.equals("0")) {
                    if (status != null && status.equals("1") && sign != null) {
                        updatePaymentData(Long.parseLong(orderId), Payment.Status.PAID);
                        publisher.publishEvent(new RequireInteractionNoMessageEvent(this, Long.parseLong(orderId), BotState.PAID));
                        log.info("Payment for chat {} verified successfully", orderId);
                    }
                });
    }
}

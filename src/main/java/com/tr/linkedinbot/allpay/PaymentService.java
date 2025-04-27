package com.tr.linkedinbot.allpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.linkedinbot.allpay.model.PaymentRequest;
import static com.tr.linkedinbot.allpay.util.PaymentUtil.getApiSignature;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentConfig paymentConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

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
}

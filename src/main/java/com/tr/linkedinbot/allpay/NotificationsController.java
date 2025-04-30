package com.tr.linkedinbot.allpay;

import com.tr.linkedinbot.allpay.util.PaymentNotification;
import com.tr.linkedinbot.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@Slf4j
@RequiredArgsConstructor
public class NotificationsController {

    private final PaymentService paymentService;

    @PostMapping("/allpay")
    public ResponseEntity<Void> handleNotification(@RequestBody PaymentNotification notification) {
        if (notification.getStatus() != null && notification.getStatus() == 1 && notification.getSign() != null) {
            paymentService.updatePaymentData(Long.parseLong(notification.getOrder_id()), Payment.Status.PAID);
            log.info("Payment passed successfully");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            } else {
            log.error("Payment didnt go well, {}", notification);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            }
    }
}

package com.tr.linkedinbot.allpay;

import com.tr.linkedinbot.allpay.util.PaymentNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Handles POST requests from Allpay containing payment notifications.
     *
     * @param notification The notification payload from Allpay.
     * @return A ResponseEntity indicating the result.
     */
    @PostMapping("/allpay")
    public ResponseEntity<String> handleNotification(@RequestBody PaymentNotification notification) {
        try {
            // Build a map of parameters for signature calculation.
            // Note: Do not include the "sign" field.
            Map<String, Object> notificationMap = new HashMap<>();
            notificationMap.put("order_id", notification.getOrder_id());
            notificationMap.put("amount", notification.getAmount());
            notificationMap.put("status", notification.getStatus());
            notificationMap.put("foreign_card", notification.getForeign_card());
            notificationMap.put("card_mask", notification.getCard_mask());
            notificationMap.put("card_brand", notification.getCard_brand());
            notificationMap.put("currency", notification.getCurrency());
            notificationMap.put("receipt", notification.getReceipt());
            notificationMap.put("add_field_1", notification.getAdd_field_1());
            notificationMap.put("add_field_2", notification.getAdd_field_2());

            if (notification.getStatus() != null
                    && notification.getStatus() == 1
                    && notification.getSign() != null) {
                // Payment is verified as successful. Process your business logic here.
                // For example: update order status, send confirmation email, etc.
                return ResponseEntity.ok("Payment verified and processed successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid signature or unsuccessful payment");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing notification: " + e.getMessage());
        }
    }
}

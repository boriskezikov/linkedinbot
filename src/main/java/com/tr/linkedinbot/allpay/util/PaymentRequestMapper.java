package com.tr.linkedinbot.allpay.util;

import com.tr.linkedinbot.allpay.model.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestMapper {

    public static SignatureData toSignatureData(PaymentRequest paymentRequest) {
        return SignatureData.builder()
                .items(paymentRequest.getItems())
                .order_id(paymentRequest.getOrder_id())
                .lang(paymentRequest.getLang())
                .client_email(paymentRequest.getClient_email())
                .client_name(paymentRequest.getClient_name())
                .notifications_url(paymentRequest.getNotifications_url())
                .expire(paymentRequest.getExpire())
                .login(paymentRequest.getLogin())
                .show_bit(paymentRequest.getShow_bit())
                .build();
    }
}

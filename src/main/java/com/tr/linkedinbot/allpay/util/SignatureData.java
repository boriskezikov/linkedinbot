package com.tr.linkedinbot.allpay.util;

import com.tr.linkedinbot.allpay.model.PaymentItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureData {
    private String login;
    private List<PaymentItem> items;
    private String order_id;
    private String lang;
    private String client_email;
    private String client_name;
    private String notifications_url;
    private Long expire;
    private String show_bit;
}

package com.tr.linkedinbot.allpay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDetail {
    // Array of items
    private List<PaymentItem> items;

    // Required Allpay fields
    private String login;
    private String order_id;
    // Optional: total amount (can be computed by Allpay from items)
    private Integer amount;
    private String currency; // Options: ILS, USD, EUR
    private String lang;     // e.g., EN, HE, RU, etc.
    private String notifications_url; // URL to receive POST notifications after payment

    // Redirect URLs
    private String success_url;
    private String backlink_url;

    // Customer details
    private String client_name;
    private String client_tehudat;  // Social ID / Company Number (optional: pass "000000000" for foreign)
    private String client_email;
    private String client_phone;

    // Optional additional fields
    private String add_field_1;
    private String add_field_2;

    // Optional fast payment buttons
    private Boolean show_applepay;
    private Boolean show_bit;

    // Expiration time as Unix timestamp
    private Long expire;

    // Signature (computed on the server before sending)
    private String sign;
}

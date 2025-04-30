package com.tr.linkedinbot.allpay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentItem {
    private String name;
    private Integer qty;
    private Integer price;
    private Integer vat;
}

package com.tr.linkedinbot.allpay.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentNotification {
    private String order_id;
    private Double amount;
    private Integer status;       // Статус платежа: 0, 1, 3
    private Integer foreign_card; // 0 – местная, 1 – иностранная
    private String card_mask;
    private String card_brand;
    private String currency;
    private String receipt;
    private String add_field_1;
    private String add_field_2;
    private String sign;
}

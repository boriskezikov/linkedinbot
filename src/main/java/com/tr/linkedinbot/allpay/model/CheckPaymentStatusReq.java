package com.tr.linkedinbot.allpay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CheckPaymentStatusReq {

    private String login;
    private String order_id;
    private String sign;

}

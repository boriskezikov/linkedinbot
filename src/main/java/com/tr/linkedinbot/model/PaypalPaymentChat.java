package com.tr.linkedinbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "paypal_payment_chat")
@NoArgsConstructor
@AllArgsConstructor
public class PaypalPaymentChat {

    @Id
    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.sql.Timestamp createdAt;
}

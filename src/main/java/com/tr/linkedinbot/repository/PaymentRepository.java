package com.tr.linkedinbot.repository;

import com.tr.linkedinbot.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findPaymentByChatId(Long chatId);

    List<Payment> findPaymentsByStatus(Payment.Status status);
}

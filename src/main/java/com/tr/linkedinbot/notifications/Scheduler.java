package com.tr.linkedinbot.notifications;

import com.tr.linkedinbot.allpay.PaymentService;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final LinkedInAccountService service;
    private final NotificationSenderService senderService;
    private final PaymentService paymentService;


    @Scheduled(cron = "*/10 * * * * *")
    public void verifyPayments() {
        log.info("JOB:: Verifying payments started");
        paymentService.handlePaymentStatusCheck();
        log.info("JOB:: Verifying payments completed");
    }

    @Scheduled(cron = "0 0 10,19 * * *")
    public void notifyToCompleteProfile() {
        service.loadIncompleteProfilesWithDaysOffset(1)
                .forEach(senderService::sendCompleteProfileNotification);
    }

    @Scheduled(cron = "0 0 18 * * MON")
    public void notifyToCompleteProfileOldUsers() {
        service.loadIncompleteProfilesInBounds(999, 1)
                .forEach(senderService::sendCompleteProfileNotification);
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void sendNewProfileRegistered() {
        Optional.of(service.loadNewProfilesWithDaysInBounds(2, 1))
                .map(service::getMapOfProfilesAndLinks)
                .orElse(Collections.emptyMap())
                .forEach(senderService::sendNewUsersLinksToProfile);
    }

}

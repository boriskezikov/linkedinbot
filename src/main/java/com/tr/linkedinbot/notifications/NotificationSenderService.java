package com.tr.linkedinbot.notifications;

import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationSenderService {

    private final MetricSender metricSender;

    private final ApplicationEventPublisher publisher;

    public void sendCompleteProfileNotification(LinkedInProfile linkedInProfile) {
        var chatId = linkedInProfile.getChatId().toString();
        publisher.publishEvent(new AnswerEvent(
                this,
                SendMessage.builder()
                        .chatId(chatId)
                        .text(TextConstants.COMPLETE_PROFILE_MESSAGE.getText())
                        .build(),
                linkedInProfile.getTgUser()
        ));

        metricSender.registerCompleteProfileNotification(chatId);
    }

    public void sendNewUsersLinksToProfile(LinkedInProfile linkedInProfile, Set<String> links) {
        var chatId = linkedInProfile.getChatId().toString();
        publisher.publishEvent(new AnswerEvent(
                        this,
                        SendMessage.builder()
                                .chatId(chatId)
                                .text("Новые профили с учетом твоих настроек поиска! Добавляй в друзья:\n\n\uD83D\uDE80" + String.join("\n\n\uD83D\uDE80", links))
                                .build(),
                linkedInProfile.getTgUser()
                )
        );

        metricSender.registerNewProfilesLinksNotification(chatId);
    }

}

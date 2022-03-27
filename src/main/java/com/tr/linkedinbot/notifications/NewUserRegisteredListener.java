package com.tr.linkedinbot.notifications;

import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.logic.LinkedInBot;
import com.tr.linkedinbot.model.LinkedInProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewUserRegisteredListener implements ApplicationListener<LinkedInProfileCreateEvent> {

    private final LinkedInAccountService linkedInAccountService;
    private final LinkedInBot linkedInBot;

    @Override
    public void onApplicationEvent(LinkedInProfileCreateEvent event) {
        linkedInAccountService.loadAll().stream()
                .filter(linkedInProfile -> !linkedInProfile.getTgUser().equals(event.getLinkedInProfile().getTgUser()))
                .map(linkedInProfile -> getSendMessage(event, linkedInProfile))
                .forEach(this::sendNotification);
    }

    private void sendNotification(SendMessage sendMessage) {
        try {
            linkedInBot.execute(sendMessage);
            log.debug("sendMessage - {} sent", sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error notifying chat_id {}, error_message:{}", sendMessage.getChatId(), e.getMessage());
        }
    }

    private SendMessage getSendMessage(LinkedInProfileCreateEvent event, LinkedInProfile linkedInProfile) {
        SendMessage sm = new SendMessage();
        sm.setChatId(linkedInProfile.getChatId().toString());
        sm.setText("Гляди у нас новый профиль! Добавляй в друзья: " + event.getLinkedInProfile().getLinkedInUrl());
        return sm;
    }
}

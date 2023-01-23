package com.tr.linkedinbot.notifications;

import static com.tr.linkedinbot.commands.TextConstants.ADMIN_INTRODUCTION;
import static com.tr.linkedinbot.commands.TextConstants.ADMIN_MESSAGE;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.logic.LinkedInBot;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.notifications.events.AdminMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminMessageListener implements ApplicationListener<AdminMessageEvent> {


    private final LinkedInAccountService linkedInAccountService;
    private final LinkedInBot linkedInBot;

    @Override
    public void onApplicationEvent(AdminMessageEvent event) {
        linkedInAccountService.loadAll().stream()
                .map(linkedInProfile -> getSendMessage(linkedInProfile, event.getMessage()))
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

    private SendMessage getSendMessage(LinkedInProfile linkedInProfile, String adminMessage) {
        SendMessage sm = new SendMessage();
        sm.setChatId(linkedInProfile.getChatId().toString());
        sm.setText(ADMIN_INTRODUCTION.getText() + adminMessage);
        return sm;
    }
}

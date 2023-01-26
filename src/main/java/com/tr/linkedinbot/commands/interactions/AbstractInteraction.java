package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.logic.MetricSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public abstract class AbstractInteraction implements Interaction {

    @Value("${bot.admin.chatId}")
    private String adminChatId;

    protected final ApplicationEventPublisher publisher;

    protected final MetricSender metricSender;

    protected AbstractInteraction(ApplicationEventPublisher publisher, MetricSender metricSender) {
        this.publisher = publisher;
        this.metricSender = metricSender;
    }

    protected SendMessage prepareAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        return answer;
    }

    protected ForwardMessage prepareForward(Message message) {
        return ForwardMessage.builder()
                .messageId(message.getMessageId())
                .fromChatId(message.getChatId().toString())
                .chatId(adminChatId)
                .build();
    }

    protected String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

}

package com.tr.linkedinbot.commands.interactions;

import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public abstract class AbstractInteraction implements Interaction {

    protected final ApplicationEventPublisher publisher;

    protected AbstractInteraction(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    protected SendMessage prepareAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        return answer;
    }

    protected String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

}

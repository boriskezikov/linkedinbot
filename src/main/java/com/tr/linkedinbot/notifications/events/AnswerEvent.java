package com.tr.linkedinbot.notifications.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
public class AnswerEvent extends ApplicationEvent {

    private final SendMessage message;
    private final String userName;

    public AnswerEvent(Object source, SendMessage message, String userName) {
        super(source);
        this.message = message;
        this.userName = userName;
    }

}

package com.tr.linkedinbot.notifications;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AdminMessageEvent extends ApplicationEvent {

    private final String message;

    public AdminMessageEvent(Object source, String message) {
        super(source);
        this.message = message;

    }
}

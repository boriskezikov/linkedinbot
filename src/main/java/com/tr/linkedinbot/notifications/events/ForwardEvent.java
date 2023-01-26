package com.tr.linkedinbot.notifications.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;

@Getter
public class ForwardEvent extends ApplicationEvent {

    private final ForwardMessage message;

    public ForwardEvent(Object source, ForwardMessage message) {
        super(source);
        this.message = message;
    }

}

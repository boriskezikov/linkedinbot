package com.tr.linkedinbot.notifications.events;

import com.tr.linkedinbot.model.BotState;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RequireInteractionNoMessageEvent extends ApplicationEvent {

    private final BotState interactionRequired;
    private final Long chatId;

    public RequireInteractionNoMessageEvent(Object source, Long chatId, BotState interactionRequired) {
        super(source);
        this.chatId = chatId;
        this.interactionRequired = interactionRequired;
    }
}

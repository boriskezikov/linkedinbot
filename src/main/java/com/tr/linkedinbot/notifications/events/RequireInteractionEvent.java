package com.tr.linkedinbot.notifications.events;

import com.tr.linkedinbot.model.BotState;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
public class RequireInteractionEvent extends ApplicationEvent {

    private final Message message;
    private final BotState interactionRequired;

    public RequireInteractionEvent(Object source, Message message, BotState interactionRequired) {
        super(source);
        this.message = message;
        this.interactionRequired = interactionRequired;
    }
}

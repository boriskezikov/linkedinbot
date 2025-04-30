package com.tr.linkedinbot.notifications;

import com.tr.linkedinbot.logic.InteractionManager;
import com.tr.linkedinbot.notifications.events.RequireInteractionNoMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InteractionNoMessageListener implements ApplicationListener<RequireInteractionNoMessageEvent> {

    private final InteractionManager interactionManager;

    @Override
    public void onApplicationEvent(RequireInteractionNoMessageEvent event) {
        interactionManager.callInteraction(event.getInteractionRequired(), event.getChatId());
    }
}

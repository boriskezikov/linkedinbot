package com.tr.linkedinbot.notifications;

import com.tr.linkedinbot.logic.InteractionManager;
import com.tr.linkedinbot.notifications.events.RequireInteractionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InteractionListener implements ApplicationListener<RequireInteractionEvent> {

    private final InteractionManager interactionManager;

    @Override
    public void onApplicationEvent(RequireInteractionEvent event) {
        interactionManager.callInteraction(event.getInteractionRequired(), event.getMessage());
    }
}

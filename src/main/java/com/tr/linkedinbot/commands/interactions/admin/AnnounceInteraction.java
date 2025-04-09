package com.tr.linkedinbot.commands.interactions.admin;

import com.tr.linkedinbot.commands.interactions.AbstractInteraction;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.notifications.events.AdminMessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AnnounceInteraction extends AbstractInteraction {

    public AnnounceInteraction(ApplicationEventPublisher publisher, MetricSender metricSender) {
        super(publisher, metricSender);
    }

    @Override
    public void interact(Message message) {
        var adminMessageEvent = new AdminMessageEvent(this, message.getText());
        publisher.publishEvent(adminMessageEvent);
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.ANNOUNCE;
    }

}

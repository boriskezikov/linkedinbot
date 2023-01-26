package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import com.tr.linkedinbot.notifications.events.ForwardEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FeedbackInteraction extends AbstractInteraction {

    protected FeedbackInteraction(ApplicationEventPublisher publisher, MetricSender metricSender) {
        super(publisher, metricSender);
    }

    @Override
    public void interact(Message message) {
        metricSender.registerInteraction(message);

        publisher.publishEvent(new AnswerEvent(this, prepareAnswer(message.getChatId(), TextConstants.FEEDBACK_SENT_MESSAGE.getText()), getUserName(message)));
        publisher.publishEvent(new ForwardEvent(this, prepareForward(message)));
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.FEEDBACK;
    }
}

package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import com.tr.linkedinbot.notifications.events.ForwardEvent;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FeedbackInteraction extends AbstractInteraction {

    protected FeedbackInteraction(ApplicationEventPublisher publisher) {
        super(publisher);
    }

    @Override
    public void interact(Message message) {
        publisher.publishEvent(new AnswerEvent(this, prepareAnswer(message.getChatId(), TextConstants.FEEDBACK_SENT.getText()), getUserName(message)));
        publisher.publishEvent(new ForwardEvent(this, prepareForward(message)));
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.FEEDBACK;
    }
}

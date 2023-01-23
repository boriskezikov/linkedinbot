package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class DefaultInteraction extends AbstractInteraction{

    public DefaultInteraction(ApplicationEventPublisher publisher) {
        super(publisher);
    }

    @Override
    public void interact(Message message) {
        publisher.publishEvent(new AnswerEvent(this,
                prepareAnswer(message.getChatId(), TextConstants.PROFILE_ALREADY_SAVED_ERROR_MESSAGE.getText()),
                getUserName(message))
        );
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.NOT_IN_INTERACTION;
    }

}

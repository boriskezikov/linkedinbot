package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.commands.KeyboardHelper;
import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.tr.linkedinbot.commands.TextConstants.*;

@Component
public class CreateNewUserInteraction extends AbstractInteraction {

    private final LinkedInAccountService accountParser;

    public CreateNewUserInteraction(
            LinkedInAccountService accountParser,
            ApplicationEventPublisher publisher,
            MetricSender metricSender
    ) {
        super(publisher, metricSender);
        this.accountParser = accountParser;
    }

    @Override
    public void interact(Message message) {
        String answer;
        String userName = getUserName(message);
        try {
            accountParser.createNewProfile(message, userName);

            metricSender.registerNewUserEvent(message);

            answer = TextConstants.PROFILE_SAVED_MESSAGE.getText();
        } catch (IllegalLinkedInProfileException e) {
                answer =  e.getMessage();
        } catch (Exception e) {
            answer = DONT_UNDERSTAND_GLOBAL_ERROR_MESSAGE.getText();
        }

        publisher.publishEvent(new AnswerEvent(this, prepareAnswer(message.getChatId(), answer, KeyboardHelper.userKeyboard), userName));
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.EMPTY;
    }

}

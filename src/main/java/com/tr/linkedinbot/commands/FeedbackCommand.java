package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.CommandEnum;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.tr.linkedinbot.commands.TextConstants.FEEDBACK_MESSAGE;
import static com.tr.linkedinbot.commands.TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE;
import static com.tr.linkedinbot.model.CommandEnum.*;

@Component
@RequiredArgsConstructor
public class FeedbackCommand extends ServiceCommand {

    private final LinkedInProfileRepository repository;

    private final MetricSender metricSender;
    
    @Override
    public String getCommandIdentifier() {
        return FEEDBACK.getName();
    }

    @Override
    public String getDescription() {
        return FEEDBACK.getDescription();
    }

    @Override
    @Transactional
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        metricSender.registerCommandUse(FEEDBACK, message);
        
        var chat = message.getChat();
        var userName = getUsername(chat);

        var optionalLinkedInProfile = repository.getByChatId(chat.getId());
        String answerMessage;
        if (optionalLinkedInProfile.isEmpty()) {
            answerMessage = GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText();
        } else {
            var linkedInProfile = optionalLinkedInProfile.get();
            linkedInProfile.setState(BotState.FEEDBACK);
            answerMessage = FEEDBACK_MESSAGE.getText();

        }
        sendAnswer(absSender, chat.getId(), FEEDBACK.getName(), userName, answerMessage, FEEDBACK_MESSAGE.getParseMode());
    }

}

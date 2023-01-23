package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static com.tr.linkedinbot.commands.TextConstants.FEEDBACK;
import static com.tr.linkedinbot.commands.TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE;

@Component
@RequiredArgsConstructor
public class FeedbackCommand extends ServiceCommand {

    private final LinkedInProfileRepository repository;
    @Override
    public String getCommandIdentifier() {
        return "feedback";
    }

    @Override
    public String getDescription() {
        return "оставить отзыв/пожелание";
    }

    @Override
    @Transactional
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        var chat = message.getChat();
        var userName = (chat.getUserName() != null) ? chat.getUserName() :
                String.format("%s %s", chat.getLastName(), chat.getFirstName());

        var optionalLinkedInProfile = repository.getByChatId(chat.getId());
        String answerMessage;
        if (optionalLinkedInProfile.isEmpty()) {
            answerMessage = GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText();
        } else {
            var linkedInProfile = optionalLinkedInProfile.get();
            linkedInProfile.setState(BotState.FEEDBACK);
            answerMessage = FEEDBACK.getText();

        }
        sendAnswer(absSender, chat.getId(), "feedback", userName, answerMessage, FEEDBACK.getParseMode());
    }

}

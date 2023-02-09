package com.tr.linkedinbot.commands.profile;

import com.tr.linkedinbot.commands.ServiceCommand;
import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.tr.linkedinbot.model.CommandEnum.EXIT;

@Component
@RequiredArgsConstructor
public class ExitCommand extends ServiceCommand {

    private final LinkedInProfileRepository repository;

    private final MetricSender metricSender;
    
    @Override
    public String getCommandIdentifier() {
        return EXIT.getName();
    }

    @Override
    public String getDescription() {
        return EXIT.getDescription();
    }

    @Override
    @Transactional
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        metricSender.registerCommandUse(EXIT, message);

        var chat = message.getChat();
        var userName = getUsername(chat);

        var optionalLinkedInProfile = repository.getByChatId(chat.getId());
        if (optionalLinkedInProfile.isEmpty()) {
            sendAnswer(absSender, message.getChatId(), EXIT.getName(), userName, TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText(), ParseMode.HTML);
        } else {
            var linkedInProfile = optionalLinkedInProfile.get();
            linkedInProfile.setState(BotState.NOT_IN_INTERACTION);
            repository.save(linkedInProfile);
            if (linkedInProfile.isComplete()) {
                sendAnswer(absSender, message.getChatId(), EXIT.getName(), userName, TextConstants.EXIT_MESSAGE_FULL_PROFILE.getText(), ParseMode.HTML);
            } else {
                sendAnswer(absSender, message.getChatId(), EXIT.getName(), userName, TextConstants.EXIT_MESSAGE.getText(), ParseMode.HTML);
            }
        }
    }

}

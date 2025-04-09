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

import static com.tr.linkedinbot.model.CommandEnum.CHANGE_ROLE;

@Component
@RequiredArgsConstructor
public class ChangeRoleCommand extends ServiceCommand {

    private final LinkedInProfileRepository repository;

    private final MetricSender metricSender;
    
    @Override
    public String getCommandIdentifier() {
        return CHANGE_ROLE.getName();
    }

    @Override
    public String getDescription() {
        return CHANGE_ROLE.getDescription();
    }

    @Override
    @Transactional
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        metricSender.registerCommandUse(CHANGE_ROLE, message);

        var chat = message.getChat();
        var userName = getUsername(chat);

        var optionalLinkedInProfile = repository.getByChatId(chat.getId());
        if (optionalLinkedInProfile.isEmpty()) {
            sendAnswer(absSender, message.getChatId(), CHANGE_ROLE.getName(), userName, TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText(), ParseMode.HTML);
        } else {
            var linkedInProfile = optionalLinkedInProfile.get();
            linkedInProfile.setState(BotState.CHANGE_ROLE);
            repository.save(linkedInProfile);
            sendAnswer(absSender, message.getChatId(), CHANGE_ROLE.getName(), userName, TextConstants.NEW_ROLE_MESSAGE.getText(), ParseMode.HTML);
        }
    }

}

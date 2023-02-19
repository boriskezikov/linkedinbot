package com.tr.linkedinbot.commands.profile;

import com.tr.linkedinbot.commands.ServiceCommand;
import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;

import static com.tr.linkedinbot.model.CommandEnum.CHANGE_COUNTRY;

@Component
@RequiredArgsConstructor
public class ChangeCountryCommand extends ServiceCommand {

    private final LinkedInProfileRepository repository;

    private final MetricSender metricSender;
    
    @Override
    public String getCommandIdentifier() {
        return CHANGE_COUNTRY.getName();
    }

    @Override
    public String getDescription() {
        return CHANGE_COUNTRY.getDescription();
    }

    @Override
    @Transactional
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        metricSender.registerCommandUse(CHANGE_COUNTRY, message);

        var chat = message.getChat();
        var userName = getUsername(chat);

        var optionalLinkedInProfile = repository.getByChatId(chat.getId());
        if (optionalLinkedInProfile.isEmpty()) {
            sendAnswer(absSender, message.getChatId(), CHANGE_COUNTRY.getName(), userName, TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText(), ParseMode.HTML);
        } else {
            var linkedInProfile = optionalLinkedInProfile.get();
            linkedInProfile.setState(BotState.CHANGE_COUNTRY);
            repository.save(linkedInProfile);
            sendAnswer(absSender, message.getChatId(), CHANGE_COUNTRY.getName(), userName, TextConstants.NEW_COUNTRY_MESSAGE.getText(), ParseMode.HTML);
        }
    }

}

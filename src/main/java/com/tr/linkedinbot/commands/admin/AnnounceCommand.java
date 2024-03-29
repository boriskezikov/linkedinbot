package com.tr.linkedinbot.commands.admin;

import com.tr.linkedinbot.commands.ServiceCommand;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.CommandEnum;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.tr.linkedinbot.commands.TextConstants.ADMIN_MESSAGE;
import static com.tr.linkedinbot.model.CommandEnum.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnounceCommand extends ServiceCommand {

    @Value("${bot.admin.name}")
    private String admin;

    private final LinkedInProfileRepository repository;
    @Override
    public String getCommandIdentifier() {
        return ADMIN_ANNOUNCE.getName();
    }

    @Override
    public String getDescription() {
        return ADMIN_ANNOUNCE.getDescription();
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String userName = message.getChat().getUserName();
        if (admin.equals(userName)) {
            LinkedInProfile linkedInProfile = repository.getByChatId(message.getChatId()).orElseThrow();
            linkedInProfile.setState(BotState.ANNOUNCE);
            repository.save(linkedInProfile);
            sendAnswer(absSender, message.getChatId(), getCommandIdentifier(), userName, ADMIN_MESSAGE.getText(), ADMIN_MESSAGE.getParseMode());
        }
    }

}

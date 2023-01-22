package com.tr.linkedinbot.commands.admin;

import com.tr.linkedinbot.commands.ServiceCommand;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.tr.linkedinbot.commands.TextConstants.ADMIN_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnounceCommand extends ServiceCommand {

    private static final String ANNOUNCE = "announce";

    @Value("${bot.admin}")
    private String admin;

    private final LinkedInProfileRepository repository;
    @Override
    public String getCommandIdentifier() {
        return ANNOUNCE;
    }

    @Override
    public String getDescription() {
        return ANNOUNCE;
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

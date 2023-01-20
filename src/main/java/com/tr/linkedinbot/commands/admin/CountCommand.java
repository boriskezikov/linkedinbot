package com.tr.linkedinbot.commands.admin;

import com.tr.linkedinbot.commands.ServiceCommand;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class CountCommand extends ServiceCommand {

    private final LinkedInAccountService linkedInAccountService;

    @Value("${bot.admin}")
    private String admin;
    @Override
    public String getCommandIdentifier() {
        return "count";
    }

    @Override
    public String getDescription() {
        return "count";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String userName = message.getChat().getUserName();
        if (admin.equals(userName)) {
            var count = linkedInAccountService.countUsers() + " users registered";
            sendAnswer(absSender, message.getChatId(), getCommandIdentifier(), userName, count, ParseMode.HTML);
        }
    }
}

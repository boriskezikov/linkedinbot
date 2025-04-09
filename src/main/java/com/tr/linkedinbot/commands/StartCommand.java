package com.tr.linkedinbot.commands;


import static com.tr.linkedinbot.commands.TextConstants.START_MESSAGE;
import static com.tr.linkedinbot.model.CommandEnum.*;

import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.CommandEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class StartCommand extends ServiceCommand {

    private final MetricSender metricSender;

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        metricSender.registerCommandUse(START, message);
        
        var chat = message.getChat();
        var userName = getUsername(chat);
        sendAnswer(absSender, chat.getId(), getCommandIdentifier(), userName, START_MESSAGE.getText(), START_MESSAGE.getParseMode());
    }


    @Override
    public String getCommandIdentifier() {
        return START.getName();
    }

    @Override
    public String getDescription() {
        return START.getDescription();
    }
}

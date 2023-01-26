package com.tr.linkedinbot.commands;

import static com.tr.linkedinbot.commands.TextConstants.HELP_MESSAGE;
import static com.tr.linkedinbot.model.CommandEnum.*;

import com.tr.linkedinbot.logic.MetricSender;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class HelpCommand extends ServiceCommand {

    private final MetricSender metricSender;

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        metricSender.registerCommandUse(HELP, message);
        
        var chat = message.getChat();
        var userName = getUsername(chat);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, HELP_MESSAGE.getText(), HELP_MESSAGE.getParseMode());
    }

    @Override
    public String getCommandIdentifier() {
        return HELP.getName();
    }

    @Override
    public String getDescription() {
        return HELP.getDescription();
    }
}

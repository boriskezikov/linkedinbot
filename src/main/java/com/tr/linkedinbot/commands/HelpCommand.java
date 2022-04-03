package com.tr.linkedinbot.commands;

import static com.tr.linkedinbot.commands.TextConstants.HELP_MESSAGE;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class HelpCommand extends ServiceCommand {

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        Chat chat = message.getChat();
        String userName = (chat.getUserName() != null) ? chat.getUserName() :
                String.format("%s %s", chat.getLastName(), chat.getFirstName());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, HELP_MESSAGE.getText(), HELP_MESSAGE.getParseMode());
    }

    @Override
    public String getCommandIdentifier() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Помощь";
    }
}

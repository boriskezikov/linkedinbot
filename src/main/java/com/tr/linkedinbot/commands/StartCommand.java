package com.tr.linkedinbot.commands;


import static com.tr.linkedinbot.commands.TextConstants.START_MESSAGE_TEXT;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends ServiceCommand {

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        var chat = message.getChat();
        String userName = (chat.getUserName() != null) ? chat.getUserName() :
                String.format("%s %s", chat.getLastName(), chat.getFirstName());
        sendAnswer(absSender, chat.getId(), getCommandIdentifier(), userName, START_MESSAGE_TEXT);
    }


    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Старт";
    }
}

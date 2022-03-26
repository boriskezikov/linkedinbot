package com.tr.linkedinbot.commands;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends ServiceCommand {

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        var chat = message.getChat();
        //формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
        String userName = (chat.getUserName() != null) ? chat.getUserName() :
                String.format("%s %s", chat.getLastName(), chat.getFirstName());
        //обращаемся к методу суперкласса для отправки пользователю ответа
        sendAnswer(absSender, chat.getId(), getCommandIdentifier(), userName,
                "Давайте начнём! Присылай ссылку на свой профиль в формате https://www.linkedin.com/in/..." +
                        "\nЕсли что-то не то, жми /help!");

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

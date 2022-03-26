package com.tr.linkedinbot.commands;

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
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Я помогу обзавестить коннектами с участниками нашего сообщества в LinkedIn!\n" +
                        "Кидай ссылку на свой профиль, и я поделюсь с тобой контактами!\n\n" +
                        "Вот пример рабочей ссылки: https://www.linkedin.com/in/boriskezikov/\n" +
                        "Если что-то идет не так, пиши @kezikoff, он прикрутит пару костылей:) \n" +
                        "With love TR++");
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

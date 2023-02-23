package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.model.CommandEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Getter
public abstract class ServiceCommand implements IBotCommand {

    @Value("${bot.admin.name}")
    private String admin;

    /**
     * Отправка ответа пользователю
     */
    public void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text, String parseMode) {
        SendMessage message = new SendMessage();
        //включаем поддержку режима разметки, чтобы управлять отображением текста и добавлять эмодзи
        message.enableMarkdown(true);
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode(parseMode);
        if (commandName.equals(CommandEnum.SHOW_PROFILE.getName()) || commandName.equals(CommandEnum.CHANGE_LINK.getName())) {
            message.setReplyMarkup(KeyboardHelper.profileKeyboard);
        } else if (commandName.equals(CommandEnum.CHANGE_COUNTRY.getName())) {
            message.setReplyMarkup(KeyboardHelper.countriesKeyboard);
        } else if (commandName.equals(CommandEnum.CHANGE_ROLE_SEARCH.getName()) || commandName.equals(CommandEnum.CHANGE_ROLE.getName())) {
            message.setReplyMarkup(KeyboardHelper.rolesKeyboard);
        } else if (commandName.equals(CommandEnum.START.getName())) {
            message.setReplyMarkup(KeyboardHelper.startKeyboard);
        } else if (userName.equals(admin)) {
            message.setReplyMarkup(KeyboardHelper.adminKeyboard);
        } else {
            message.setReplyMarkup(KeyboardHelper.userKeyboard);
        }
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("command_name:{}, chatId: {}, user_name:{}, error_message:{}", commandName, chatId, userName, e.getMessage());
        }
    }

    public String getUsername(Chat chat) {
        return (chat.getUserName() != null) ? chat.getUserName() :
                String.format("%s %s", chat.getLastName(), chat.getFirstName());
    }

}
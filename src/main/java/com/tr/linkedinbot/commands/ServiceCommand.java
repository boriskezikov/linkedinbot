package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.model.ButtonNameEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public abstract class ServiceCommand implements IBotCommand {

    @Value("${bot.admin}")
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
        if (userName.equals(admin)) {
            message.setReplyMarkup(getAdminKeyboard());
        } else {
            message.setReplyMarkup(getUserKeyboard());
        }
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("command_name:{}, chatId: {}, user_name:{}, error_message:{}", commandName, chatId, userName, e.getMessage());
        }
    }

    public ReplyKeyboardMarkup getAdminKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.GET_NEXT_PROFILES.getButtonName()));
        row1.add(new KeyboardButton(ButtonNameEnum.HELP.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.ADMIN_COUNT.getButtonName()));
//        row2.add(new KeyboardButton(ButtonNameEnum.ADMIN_MESSAGE.getButtonName()));


        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getUserKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.GET_NEXT_PROFILES.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.HELP.getButtonName()));


        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}
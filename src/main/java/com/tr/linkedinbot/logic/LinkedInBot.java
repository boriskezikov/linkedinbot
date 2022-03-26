package com.tr.linkedinbot.logic;

import com.tr.linkedinbot.commands.GetProfilesCommand;
import com.tr.linkedinbot.commands.HelpCommand;
import com.tr.linkedinbot.commands.NonCommand;
import com.tr.linkedinbot.commands.StartCommand;
import com.tr.linkedinbot.config.LinkedInBotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkedInBot extends TelegramLongPollingCommandBot {

    private final LinkedInBotConfig config;

    private final NonCommand nonCommand;
    private final HelpCommand helpCommand;
    private final StartCommand startCommand;
    private final GetProfilesCommand getProfilesCommand;

    @PostConstruct
    public void init() {
        register(helpCommand);
        register(startCommand);
        register(getProfilesCommand);
    }


    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = getUserName(msg);
        String answer = nonCommand.nonCommandExecute(msg, userName);
        setAnswer(chatId, userName, answer);

    }

    /**
     * Формирование имени пользователя
     * @param msg сообщение
     */
    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    /**
     * Отправка ответа
     * @param chatId id чата
     * @param userName имя пользователя
     * @param text текст ответа
     */
    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error("chatId: {}, user_name:{}, error_message:{}", chatId, userName, e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
package com.tr.linkedinbot.logic;

import com.tr.linkedinbot.commands.GetProfilesCommand;
import com.tr.linkedinbot.commands.HelpCommand;
import com.tr.linkedinbot.commands.NonCommand;
import com.tr.linkedinbot.commands.StartCommand;
import com.tr.linkedinbot.config.LinkedInBotConfig;
import com.tr.linkedinbot.notifications.AdminMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final LinkedInBotConfig botConfig;
    private final LinkedInAccountService linkedInAccountService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    public void init() {
        register(helpCommand);
        register(startCommand);
        register(getProfilesCommand);
    }


    @Override
    public void processNonCommandUpdate(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String userName = getUserName(message);
        if (botConfig.getAdmin().equals(message.getChat().getUserName())) {
            processAdminCommands(message, chatId, userName);
        } else {
            var answer = nonCommand.nonCommandExecute(message, userName);
            setAnswer(chatId, userName, answer);
        }


    }

    private void processAdminCommands(Message message, Long chatId, String userName) {
        var adminText = message.getText();
        if (adminText.equals("/count")) {
            var count = linkedInAccountService.countUsers() + " users registered";
            setAnswer(chatId, userName, count);
            return;
        }
        if (adminText.startsWith(botConfig.getPassToSay())) {
            var adminMessageEvent = new AdminMessageEvent(this, adminText.replace(botConfig.getPassToSay(), ""));
            applicationEventPublisher.publishEvent(adminMessageEvent);
            return;
        }
        var answer = nonCommand.nonCommandExecute(message, userName);
        setAnswer(chatId, userName, answer);
    }

    /**
     * Формирование имени пользователя
     *
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
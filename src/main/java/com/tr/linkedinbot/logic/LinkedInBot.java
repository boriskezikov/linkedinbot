package com.tr.linkedinbot.logic;

import com.tr.linkedinbot.commands.ServiceCommand;
import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.config.LinkedInBotConfig;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import com.tr.linkedinbot.notifications.events.ForwardEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.lang.Math.toIntExact;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkedInBot extends TelegramLongPollingCommandBot {

    private final LinkedInBotConfig config;
    private final List<ServiceCommand> commands;

    private final InteractionManager interactionManager;

    @PostConstruct
    public void init() {
        commands.forEach(this::register);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        interactionManager.processMessage(update.getMessage());
    }

    @EventListener
    public void setAnswer(AnswerEvent answer) {
        try {
            execute(answer.getMessage());
        } catch (TelegramApiException e) {
            log.error("chatId: {}, user_name:{}, error_message:{}", answer.getMessage().getChatId(), answer.getUserName(), e.getMessage());
        }
    }

    @EventListener
    public void setAnswer(ForwardEvent forwardEvent) {
        try {
            execute(forwardEvent.getMessage());
        } catch (TelegramApiException e) {
            log.error("chatId: {}, error_message:{}", forwardEvent.getMessage().getChatId(), e.getMessage());
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
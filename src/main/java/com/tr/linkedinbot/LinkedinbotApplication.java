package com.tr.linkedinbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class LinkedinbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkedinbotApplication.class, args);
    }

}

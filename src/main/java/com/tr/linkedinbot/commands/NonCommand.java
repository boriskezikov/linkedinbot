package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class NonCommand {

    private final LinkedInAccountService accountParser;

    public String nonCommandExecute(Message message, String userName) {
        String answer;
        try {
            accountParser.createNewProfile(message, userName);
            answer = "Сохранил твой профиль. Можешь приступать к добавлению новых контактов.\n" +
                    "With love TR++";
        } catch (IllegalLinkedInProfileException e) {
                answer =  e.getMessage();
        } catch (Exception e) {
            answer = "Простите, я не понимаю Вас. Возможно, Вам поможет /help\n" +
                    "With love TR++";
        }
        return answer;
    }

}

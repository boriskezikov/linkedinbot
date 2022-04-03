package com.tr.linkedinbot.commands;

import static com.tr.linkedinbot.commands.TextConstants.DONT_UNDERSTAND_GLOBAL_ERROR_MESSAGE;
import static com.tr.linkedinbot.commands.TextConstants.PROFILE_SAVED_MESSAGE;
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
            answer = PROFILE_SAVED_MESSAGE.getText();
        } catch (IllegalLinkedInProfileException e) {
                answer =  e.getMessage();
        } catch (Exception e) {
            answer = DONT_UNDERSTAND_GLOBAL_ERROR_MESSAGE.getText();
        }
        return answer;
    }

}

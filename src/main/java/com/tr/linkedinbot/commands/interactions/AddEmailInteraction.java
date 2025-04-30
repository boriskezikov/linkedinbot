package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.commands.KeyboardHelper;
import static com.tr.linkedinbot.commands.TextConstants.DONT_UNDERSTAND_GLOBAL_ERROR_MESSAGE;
import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import com.tr.linkedinbot.notifications.events.RequireInteractionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AddEmailInteraction extends AbstractInteraction {

    private final LinkedInAccountService accountParser;

    public AddEmailInteraction(
            LinkedInAccountService accountParser,
            ApplicationEventPublisher publisher,
            MetricSender metricSender) {
        super(publisher, metricSender);
        this.accountParser = accountParser;
    }

    @Override
    @Transactional
    public void interact(Message message) {
        String answer;
        String userName = getUserName(message);
        try {
            LinkedInProfile profile = accountParser.getProfile(message.getChatId()).orElseThrow(() -> new IllegalLinkedInProfileException("No profile"));
            if (Boolean.TRUE.equals(profile.getAwaitingEmail())) {
                String text = message.getText().trim();
                if (isValidEmail(text)) {
                    profile.setAwaitingEmail(false);
                    profile.setEmail(text);
                    profile.setState(BotState.TO_PAY);
                    accountParser.saveProfile(profile);
                    answer = "✅ Email сохранён. Продолжаем…";
                    publisher.publishEvent(new RequireInteractionEvent(this, message, BotState.TO_PAY));
                } else {
                    answer = "⚠️ Некорректный формат e-mail. Пожалуйста, введите ещё раз:";
                }
            } else {
                answer = "Мы получили ваш email. Можем двигаться дальше!:)";
            }
        } catch (IllegalLinkedInProfileException e) {
            answer = e.getMessage();
        } catch (Exception e) {
            answer = DONT_UNDERSTAND_GLOBAL_ERROR_MESSAGE.getText();
        }

        publisher.publishEvent(new AnswerEvent(this, prepareAnswer(message.getChatId(), answer, KeyboardHelper.userKeyboard), userName));
    }

    @Override
    public void interact(Long chatId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.AWAITING_EMAIL;
    }

    private boolean isValidEmail(String email) {
        // можно использовать regex или Apache Commons
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

}

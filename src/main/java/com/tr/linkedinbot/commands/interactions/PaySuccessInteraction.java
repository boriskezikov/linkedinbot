package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.allpay.PaymentService;
import com.tr.linkedinbot.commands.KeyboardHelper;
import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.ws.rs.NotSupportedException;

@Component
public class PaySuccessInteraction extends AbstractInteraction {

    private final LinkedInAccountService accountParser;
    private final PaymentService paymentService;

    public PaySuccessInteraction(
            LinkedInAccountService accountParser,
            ApplicationEventPublisher publisher,
            MetricSender metricSender,
            PaymentService paymentService
    ) {
        super(publisher, metricSender);
        this.accountParser = accountParser;
        this.paymentService = paymentService;
    }

    @Override
    public void interact(Message message) {
        throw new NotSupportedException();
    }

    @Override
    public void interact(Long chatId) {
        String answer = "Ваш платеж успешно получен! Вы получили полный доступ к сервису!";
        LinkedInProfile profile = accountParser.getProfile(chatId).orElseThrow(() -> new IllegalLinkedInProfileException("No profile"));
        profile.setState(BotState.NOT_IN_INTERACTION);
        accountParser.saveProfile(profile);
        publisher.publishEvent(new AnswerEvent(this, prepareAnswer(chatId, answer, KeyboardHelper.userKeyboard), ""));
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.PAID;
    }

}

package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.allpay.PaymentService;
import com.tr.linkedinbot.allpay.model.PaymentItem;
import com.tr.linkedinbot.allpay.model.PaymentRequest;
import com.tr.linkedinbot.commands.KeyboardHelper;
import static com.tr.linkedinbot.commands.TextConstants.DONT_UNDERSTAND_GLOBAL_ERROR_MESSAGE;
import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.model.Payment;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Collections;

@Component
public class PayInteraction extends AbstractInteraction {

    private final LinkedInAccountService accountParser;
    private final PaymentService paymentService;

    public PayInteraction(
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
        String answer;
        String userName = getUserName(message);
        try {
            LinkedInProfile profile = accountParser.getProfile(message.getChatId()).orElseThrow(() -> new IllegalLinkedInProfileException("No profile"));
            answer = sendPayLink(message, profile.getEmail());
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
        return BotState.TO_PAY;
    }

    public String sendPayLink(Message message, String email) {
        Long chatId = message.getChatId();

        String userName = (message.getFrom().getUserName() != null)
                ? message.getFrom().getUserName()
                : String.format("%s %s",
                message.getFrom().getFirstName(),
                message.getFrom().getLastName());

        String approvalUrl = paymentService.createPayLink(createPaymentRequest(userName, chatId, email));
        if (approvalUrl != null) {
            paymentService.savePaymentData(chatId, Payment.Status.PENDING);
            return String.format("Для оплаты доступа к боту воспользуйтесь новым израильским платежным сервисом AllPay:\n<a href=\"%s\">Оплатить</a>", approvalUrl);

        } else {
            paymentService.savePaymentData(chatId, Payment.Status.ERROR_LINK_GENERATION);
            return "Не удалось создать заказ на AllPay. Свяжитесь с нами для помощи.";
        }
    }

    private PaymentRequest createPaymentRequest(String userName, Long chatId, String clientEmail) {
        return PaymentRequest.builder()
                .items(Collections.singletonList(
                        PaymentItem.builder()
                                .name("LinkedIn Bot Full Access")
                                .qty(1)
                                .price(15)
                                .vat(1)
                                .build()
                ))
                .order_id(chatId.toString())
                .lang("RU")
                .client_email(clientEmail)
                .show_bit("true")
                .client_name(userName)
                .notifications_url("https://5492-80-187-80-121.ngrok-free.app/notifications/allpay")
                .success_url("https://t.me/Test_linked_bot")
                .backlink_url("https://t.me/Test_linked_bot")
                .expire(System.currentTimeMillis() * 1000 + 3600)
                .build();
    }
}

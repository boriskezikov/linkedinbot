package com.tr.linkedinbot.commands;

import static com.tr.linkedinbot.commands.TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE;
import static com.tr.linkedinbot.commands.TextConstants.GET_PROFILES_NO_USERS_MESSAGE;
import com.tr.linkedinbot.config.LinkedInBotConfig;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.model.CommandEnum;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.allpay.model.PaymentItem;
import com.tr.linkedinbot.allpay.model.PaymentRequest;
import com.tr.linkedinbot.allpay.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetProfilesCommand extends ServiceCommand {

    private final LinkedInAccountService linkedInAccountService;
    private final LinkedInBotConfig config;
    private final PaymentService paymentService;

    @Override
    public String getCommandIdentifier() {
        return CommandEnum.GET_NEXT_PROFILES.getName();
    }

    @Override
    public String getDescription() {
        return CommandEnum.GET_NEXT_PROFILES.getDescription();
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        var chat = message.getChat();
        var chatId = chat.getId();
        var userName = getUsername(chat);

        boolean userLoadedHisProfile = linkedInAccountService.validateUpload(chatId, userName);
        if (!userLoadedHisProfile) {
            String msg = GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText();
            sendAnswer(absSender, chatId, this.getCommandIdentifier(), userName, msg, GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getParseMode());
            return;
        }

        boolean isSizeGood = linkedInAccountService.checkRequesterLoadSize(chatId, userName);
        if (!isSizeGood) {
            sendPayLink(absSender, message);
            return;
        }
        boolean isBillingGood = linkedInAccountService.checkRequesterBillingTime(chatId, userName);
        if (!isBillingGood) {
            sendPayLink(absSender, message);
            return;
        }
        var linkedInProfiles = linkedInAccountService.loadRandomRecords(chatId, userName, config.getRandomLimit());
        String answer;
        if (linkedInProfiles.isEmpty()) {
            answer = GET_PROFILES_NO_USERS_MESSAGE.getText();
        } else {
            var response = linkedInProfiles.stream()
                    .map(LinkedInProfile::getLinkedInUrl)
                    .collect(Collectors.joining("\n\n\uD83D\uDE80"));
            answer = "\uD83D\uDE80" + response + "\n\nWith love from Israel HiTech\uD83D\uDE09";
        }
        sendAnswer(absSender, chatId, this.getCommandIdentifier(), userName, answer, GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getParseMode());
    }

    /**
     * Метод, который создаёт PayPal-ссылку и отправляет пользователю.
     */
    public void sendPayLink(AbsSender absSender, Message message) {
        Long chatId = message.getChatId();

        String userName = (message.getFrom().getUserName() != null)
                ? message.getFrom().getUserName()
                : String.format("%s %s",
                message.getFrom().getFirstName(),
                message.getFrom().getLastName());

        String approvalUrl = paymentService.createPayLink(createPaymentRequest(userName, chatId));
        if (approvalUrl != null) {
            sendAnswer(absSender, chatId, getCommandIdentifier(), userName,
                    String.format("Для оплаты доступна к боту воспользуйтесь новым израильским платежным сервисом AllPay:\n<a href=\"%s\">Оплатить</a>", approvalUrl),
                    "HTML"
            );
        } else {
            sendAnswer(absSender, chatId, getCommandIdentifier(), userName,
                    "Не удалось создать заказ на AllPay. Свяжитесь с нами для помощи.",
                    "Markdown"
            );
        }
    }

    private PaymentRequest createPaymentRequest(String userName, Long clientId) {
        return PaymentRequest.builder()
                .items(Collections.singletonList(
                        PaymentItem.builder()
                                .name("LinkedIn Bot Full Access")
                                .qty(1)
                                .price(15)
                                .vat(1)
                                .build()
                ))
                .order_id(clientId.toString() + "-" + System.currentTimeMillis())
                .lang("RU")
                .client_email("bakezikov@gmail.com")
                .show_bit("true")
                .client_name(userName)
                .notifications_url("https://linkedin-chat-bot.herokuapp.com/notifications/allpay")
                .expire(System.currentTimeMillis() * 1000 + 3600)
                .build();
    }
}

package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.commands.KeyboardHelper;
import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.Country;
import com.tr.linkedinbot.model.Role;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.tr.linkedinbot.model.Country.*;

@Component
public class ChangeCountryInteraction extends AbstractInteraction {

    private final LinkedInProfileRepository repository;

    protected ChangeCountryInteraction(
            ApplicationEventPublisher publisher,
            LinkedInProfileRepository repository,
            MetricSender metricSender
    ) {
        super(publisher, metricSender);
        this.repository = repository;
    }

    @Override
    public void interact(Message message) {
        metricSender.registerInteraction(message);

        var linkedInProfile = repository.getByChatId(message.getChatId()).orElseThrow();
        if (ISRAEL.name().equals(message.getText())) {
            linkedInProfile.setCountry(ISRAEL);
        } else {
            linkedInProfile.setCountry(ALL);
        }
        publisher.publishEvent(new AnswerEvent(this, prepareAnswer(message.getChatId(), TextConstants.COUNTRY_CHANGED_MESSAGE.getText(), KeyboardHelper.profileKeyboard), getUserName(message)));
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.CHANGE_COUNTRY;
    }

}

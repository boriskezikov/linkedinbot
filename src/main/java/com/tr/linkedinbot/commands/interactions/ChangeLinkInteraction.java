package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ChangeLinkInteraction extends AbstractInteraction {

    private final LinkedInProfileRepository repository;

    protected ChangeLinkInteraction(ApplicationEventPublisher publisher, LinkedInProfileRepository repository) {
        super(publisher);
        this.repository = repository;
    }

    @Override
    public void interact(Message message) {
        var linkedInProfile = repository.getByChatId(message.getChatId()).orElseThrow();
        String validUrl;
        try {
            validUrl = LinkedInAccountService.checkValid(message.getText());
        } catch (IllegalLinkedInProfileException e) {
            publisher.publishEvent(new AnswerEvent(this, prepareAnswer(message.getChatId(), e.getMessage()), getUserName(message)));
            return;
        }
        linkedInProfile.setLinkedInUrl(validUrl);
        publisher.publishEvent(new AnswerEvent(this, prepareAnswer(message.getChatId(), TextConstants.LINK_CHANGED_MESSAGE.getText()), getUserName(message)));
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.CHANGE_LINK;
    }

}

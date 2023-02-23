package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.commands.TextConstants;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.model.Role;
import com.tr.linkedinbot.notifications.events.AnswerEvent;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class ChangeSearchRolesInteraction extends AbstractInteraction {

    private final LinkedInProfileRepository repository;

    protected ChangeSearchRolesInteraction(
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
        Optional.ofNullable(Role.fromString(message.getText()))
                .map(role -> updateRoleSearch(role, linkedInProfile))
                .map(repository::save)
                .orElseThrow();

        publisher.publishEvent(new AnswerEvent(this, prepareAnswer(message.getChatId(), TextConstants.SEARCH_ROLES_CHANGED_MESSAGE.getText()), getUserName(message)));
    }

    private LinkedInProfile updateRoleSearch(Role role, LinkedInProfile linkedInProfile) {
        Set<Role> searchRoles = linkedInProfile.getSearchRoles();
        if (searchRoles.contains(role)) {
            linkedInProfile.removeSearchRole(role);
        } else {
            linkedInProfile.addSearchRole(role);
        }

        return linkedInProfile;
    }

    @Override
    public BotState getBotStateForInteraction() {
        return BotState.CHANGE_ROLE_SEARCH;
    }

}

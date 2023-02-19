package com.tr.linkedinbot.logic;

import com.tr.linkedinbot.commands.interactions.Interaction;
import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InteractionManager {

    private final Map<BotState, Interaction> interactionMap;

    private final Interaction defaultInteraction;

    private final LinkedInProfileRepository repository;

    @Transactional
    public void processMessage(Message message) {
        Long chatId = message.getChatId();
        Optional<LinkedInProfile> linkedInProfile = repository.getByChatId(chatId);
        interactOnCurrentState(message, linkedInProfile);

//        linkedInProfile.ifPresent(profile -> profile.setState(BotState.NOT_IN_INTERACTION));
    }

    private void interactOnCurrentState(Message message, Optional<LinkedInProfile> linkedInProfile) {
        BotState profileState = linkedInProfile.map(LinkedInProfile::getState).orElse(BotState.EMPTY);
        interactionMap.getOrDefault(profileState, defaultInteraction).interact(message);

    }
}

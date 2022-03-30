package com.tr.linkedinbot.logic;

import static com.tr.linkedinbot.commands.TextConstants.INVALID_LINKEDIN_LINK_ERROR_TEXT;
import static com.tr.linkedinbot.commands.TextConstants.PROFILE_ALREADY_SAVED_ERROR_TEXT;
import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.notifications.LinkedInProfileCreateEvent;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import static java.util.regex.Pattern.compile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkedInAccountService {

    private final LinkedInProfileRepository linkedInProfileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    public long countUsers() {
        return linkedInProfileRepository.count();
    }

    public void createNewProfile(Message message, String username) {
        Optional<LinkedInProfile> byId = linkedInProfileRepository.findById(message.getChatId());
        if (byId.isPresent()) {
            throw new IllegalLinkedInProfileException(PROFILE_ALREADY_SAVED_ERROR_TEXT);
        }
        var text = message.getText();
        var linkedInValidUrlPattern = compile("((https?:\\/\\/)?((www|\\w\\w)\\.)?linkedin\\.com\\/)((([\\w]{2,3})?)|([^\\/]+\\/(([\\w|\\d-&#?=])+\\/?){1,}))$");
        boolean matches = linkedInValidUrlPattern.matcher(text).matches();
        if (!matches) {
            throw new IllegalLinkedInProfileException(INVALID_LINKEDIN_LINK_ERROR_TEXT);
        }
        LinkedInProfile newProfile = LinkedInProfile.builder()
                .chatId(message.getChatId())
                .linkedInUrl(text)
                .tgUser(username).build();

        linkedInProfileRepository.save(newProfile);

        log.info(">>> new member added {}", text);

        var event = new LinkedInProfileCreateEvent(this, newProfile);
        applicationEventPublisher.publishEvent(event);
    }

    public List<LinkedInProfile> loadAll() {
        return linkedInProfileRepository.findAll();
    }

    public List<LinkedInProfile> loadAll(Long chatId, String tgName) {
        var all = linkedInProfileRepository.findAll();
        removeRequester(all, tgName, chatId);
        return all;
    }

    public List<LinkedInProfile> loadRandomRecords(Long chatId, String tgName, int limit) {
        var all = linkedInProfileRepository.selectRandom(limit);
        removeRequester(all, tgName, chatId);
        return all;
    }

    private void removeRequester(List<LinkedInProfile> all, String tgName, Long chatId) {
        all.removeIf(linkedInProfile -> linkedInProfile.getTgUser().equals(tgName) ||
                linkedInProfile.getChatId().equals(chatId));
    }


    public boolean validateUpload(Long chatId, String tgName) {
        return linkedInProfileRepository.existsByChatIdOrTgUser(chatId, tgName);
    }
}

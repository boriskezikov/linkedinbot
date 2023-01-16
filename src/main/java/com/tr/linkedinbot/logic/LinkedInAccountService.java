package com.tr.linkedinbot.logic;

import static com.tr.linkedinbot.commands.TextConstants.INVALID_LINKEDIN_LINK_ERROR_MESSAGE;
import static com.tr.linkedinbot.commands.TextConstants.PROFILE_ALREADY_SAVED_ERROR_MESSAGE;
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
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkedInAccountService {

    /*
    "^" matches the start of the string, ensuring that the regular expression only matches URLs that start with the specified pattern.
    "(https?://)?" is a group that matches "http://" or "https://" and is optional, meaning it can match URLs that start with "http://" or "https://" or not.
    "(www\.)?" is a group that matches "www." and is also optional, meaning it can match URLs that contain "www." or not.
    "linkedin\.com" matches the domain name "linkedin.com"
    "/in" matches the string "/in"
    ".*" matches any character (except for a newline) zero or more times.
    "$" matches the end of the string, ensuring that the regular expression only matches URLs that end with the specified pattern.
     */
    public static final String REGEX = "^(https?://)?(www\\.)?linkedin\\.com/in.*$";
    private static final Predicate<String> LINKED_IN_VALID_URL_PATTERN_PREDICATE = compile(REGEX).asMatchPredicate();
    private final LinkedInProfileRepository linkedInProfileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public long countUsers() {
        return linkedInProfileRepository.count();
    }

    public void createNewProfile(Message message, String username) {
        Optional<LinkedInProfile> byId = linkedInProfileRepository.findById(message.getChatId());
        if (byId.isPresent()) {
            throw new IllegalLinkedInProfileException(PROFILE_ALREADY_SAVED_ERROR_MESSAGE.getText());
        }
        var validUrl = checkValid(message.getText());

        LinkedInProfile newProfile = LinkedInProfile.builder()
                .chatId(message.getChatId())
                .linkedInUrl(validUrl)
                .tgUser(username).build();

        linkedInProfileRepository.save(newProfile);

        log.info(">>> new member added {}", validUrl);

        var event = new LinkedInProfileCreateEvent(this, newProfile);
        applicationEventPublisher.publishEvent(event);
    }

    private String checkValid(String linkedInUrl) {
        return Optional.of(linkedInUrl)
                .filter(LINKED_IN_VALID_URL_PATTERN_PREDICATE)
                .map(linked -> linked.replace("/mwlite", ""))
                .orElseThrow(() -> new IllegalLinkedInProfileException(INVALID_LINKEDIN_LINK_ERROR_MESSAGE.getText()));
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

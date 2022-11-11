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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
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
            throw new IllegalLinkedInProfileException(PROFILE_ALREADY_SAVED_ERROR_MESSAGE.getText());
        }
        var validUrl = checkValid(message.getText());
        var chatId = message.getChatId();
        LinkedInProfile newProfile = LinkedInProfile.builder()
                .chatId(chatId)
                .linkedInUrl(validUrl)
                .lastProfileGet(LocalDateTime.now().minusYears(1))
                .tgUser(username).build();

        saveProfile(newProfile);
        log.info(">>> new member added {}", validUrl);

        var event = new LinkedInProfileCreateEvent(this, newProfile);
        applicationEventPublisher.publishEvent(event);
    }

    private void saveProfile(LinkedInProfile newProfile){
        linkedInProfileRepository.save(newProfile);
        //Записываем свой id в список показаных, чтобы он не попал в выдачу
        linkedInProfileRepository.writeShownChatId(newProfile.getChatId(), newProfile.getChatId());
    }

    private String checkValid(String linkedInUrl) {
        var linkedInValidUrlPattern = compile("((https?:\\/\\/)?((www|\\w\\w)\\.)?linkedin\\.com\\/)((([\\w]{2,3})?)|([^\\/]+\\/(([\\w|\\d-&#?=])+\\/?){1,}))$");
        boolean matches = linkedInValidUrlPattern.matcher(linkedInUrl).matches();
        if (!matches) {
            throw new IllegalLinkedInProfileException(INVALID_LINKEDIN_LINK_ERROR_MESSAGE.getText());
        }
        return linkedInUrl.replace("/mwlite", "");
    }

    public List<LinkedInProfile> loadAll() {
        return linkedInProfileRepository.findAll();
    }

    public List<LinkedInProfile> loadAll(Long chatId, String tgName) {
        var all = linkedInProfileRepository.findAll();
        removeRequester(all, tgName, chatId);
        return all;
    }
    //TODO: Это зачатки биллинга.
    // Пока проверяем только время последнего запроса и советвуем сделать донат.
    // Когда будет мерчант, можно строго проверять оплату.
    public boolean checkRequesterBillingTime(Long chatId, String tgName) {
        log.info("Check requester billing {} {}", chatId, tgName);

        LinkedInProfile p = linkedInProfileRepository.getById(chatId);
        return LocalDateTime.now().isAfter(p.getLastProfileGet().plusMinutes(2));
    }

    public boolean checkRequesterLoadSize(Long chatId, String tgName) {
        log.info("Check requester load size {} {}", chatId, tgName);

        Long l = linkedInProfileRepository.selectRequesterLoadSize(chatId);
        return l <= 20;
    }

    public List<LinkedInProfile> loadRandomRecords(Long chatId, String tgName, int limit) {
        log.info("Loading profiles for {} {}", chatId, tgName);

        var all = linkedInProfileRepository.selectRandomForRequester(chatId, limit);
        all.forEach(l -> linkedInProfileRepository.writeShownChatId(chatId, l.getChatId()));

        return all;
    }

    private void removeRequester(List<LinkedInProfile> all, String tgName, Long chatId) {
        all.removeIf(linkedInProfile -> linkedInProfile.getTgUser().equals(tgName) ||
                linkedInProfile.getChatId().equals(chatId));
    }


    public boolean validateUpload(Long chatId, String tgName) {
        return linkedInProfileRepository.existsByChatIdOrTgUser(chatId, tgName);
    }

    private void updateLastGetDate(Long chatId) {
        LinkedInProfile p = linkedInProfileRepository.getById(chatId);
        p.setLastProfileGet(LocalDateTime.now());
        linkedInProfileRepository.save(p);
    }
}

package com.tr.linkedinbot.logic;

import com.tr.linkedinbot.exception.IllegalLinkedInProfileException;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import static java.util.regex.Pattern.compile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkedInAccountService {

    private final LinkedInProfileRepository linkedInProfileRepository;

    public void createNewProfile(Message message, String username) {
        Optional<LinkedInProfile> byId = linkedInProfileRepository.findById(message.getChatId());
        if (byId.isPresent()) {
            throw new IllegalLinkedInProfileException("Ты уже загрузил профиль, большего не нужно :)");
        }
        var text = message.getText();
        var linkedInValidUrlPattern = compile("((https?:\\/\\/)?((www|\\w\\w)\\.)?linkedin\\.com\\/)((([\\w]{2,3})?)|([^\\/]+\\/(([\\w|\\d-&#?=])+\\/?){1,}))$");
        boolean matches = linkedInValidUrlPattern.matcher(text).matches();
        if (!matches) {
            throw new IllegalLinkedInProfileException("Похоже ссылка не корректна и/или такой профиль не существует");
        }
        LinkedInProfile newProfile = LinkedInProfile.builder()
                .chatId(message.getChatId())
                .linkedInUrl(text)
                .tgUser(username).build();

        linkedInProfileRepository.save(newProfile);

        log.info(">>> new member added {}", text);
    }

    public List<LinkedInProfile> loadAll() {
        return linkedInProfileRepository.findAll();
    }

    public boolean validateUpload(Long chatId, String tgName) {
        return linkedInProfileRepository.existsByChatIdOrTgUser(chatId, tgName);
    }
}

package com.tr.linkedinbot.commands;

import static com.tr.linkedinbot.commands.TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE_TEXT;
import static com.tr.linkedinbot.commands.TextConstants.GET_PROFILES_NO_USERS_MESSAGE_TEXT;
import com.tr.linkedinbot.config.LinkedInBotConfig;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.model.LinkedInProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetProfilesCommand extends ServiceCommand {

    private final LinkedInAccountService linkedInAccountService;
    private final LinkedInBotConfig config;

    @Override
    public String getCommandIdentifier() {
        return "get_next_profiles";
    }

    @Override
    public String getDescription() {
        return "Загрузить 10 следующий профилей";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String getProfilesMessage;
        Chat chat = message.getChat();
        var userName = (chat.getUserName() != null) ? chat.getUserName() :
                String.format("%s %s", chat.getLastName(), chat.getFirstName());
        boolean userLoadedHisProfile = linkedInAccountService.validateUpload(chat.getId(), userName);
        if (!userLoadedHisProfile) {
            getProfilesMessage = GET_PROFILES_LOAD_ACC_FIRST_MESSAGE_TEXT;
        } else {
            getProfilesMessage = getProfiles(chat, userName);
        }
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, getProfilesMessage);
    }

    private String getProfiles(Chat chat, String userName) {
        String getProfilesMessage;
        var linkedInProfiles = getLinkedInProfiles(chat, userName);
        if (linkedInProfiles.isEmpty()) {
            getProfilesMessage = GET_PROFILES_NO_USERS_MESSAGE_TEXT;
        } else {
            var response = linkedInProfiles.stream().map(LinkedInProfile::getLinkedInUrl).collect(Collectors.joining("\n\n\uD83D\uDE80"));
            getProfilesMessage = "\uD83D\uDE80" + response + "\n\nWith Love TR++\uD83D\uDE09";
        }
        return getProfilesMessage;
    }

    private List<LinkedInProfile> getLinkedInProfiles(Chat chat, String userName) {
        List<LinkedInProfile> linkedInProfiles;
        if (linkedInAccountService.countUsers() <= 100) {
            linkedInProfiles = linkedInAccountService.loadAll(chat.getId(), userName);
        } else {
            linkedInProfiles = linkedInAccountService.loadRandomRecords(chat.getId(), userName, config.getRandomLimit());
        }
        return linkedInProfiles;
    }
}

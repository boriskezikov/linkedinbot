package com.tr.linkedinbot.commands;

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

    @Override
    public String getCommandIdentifier() {
        return "get_profiles";
    }

    @Override
    public String getDescription() {
        return "Загрузить 10 следующий профилей";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String response;
        Chat chat = message.getChat();
        var userName = (chat.getUserName() != null) ? chat.getUserName() :
                String.format("%s %s", chat.getLastName(), chat.getFirstName());
        boolean userLoadedHisProfile = linkedInAccountService.validateUpload(chat.getId(), userName);
        if (!userLoadedHisProfile) {
            response = "По нашим правилам, сначала ты грузишь свой профиль, а потом мы покажем тебе чужие\n" +
                    "With love TR++";
        } else {
            List<LinkedInProfile> linkedInProfiles = linkedInAccountService.loadAll();
            response = linkedInProfiles.stream().map(LinkedInProfile::getLinkedInUrl).collect(Collectors.joining("\n"));
            if (response.isBlank()) {
                response = "Нам пока нечего тебе показать( Пингани ребят в чате, чтоб грузили свои профили!\n" +
                        "With love TR++";
            }
        }

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, response);
    }
}

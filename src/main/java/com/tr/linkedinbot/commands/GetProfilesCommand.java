package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.config.LinkedInBotConfig;
import com.tr.linkedinbot.logic.LinkedInAccountService;
import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.CommandEnum;
import com.tr.linkedinbot.model.LinkedInProfile;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.stream.Collectors;

import static com.tr.linkedinbot.commands.TextConstants.*;

@Component
@RequiredArgsConstructor
public class GetProfilesCommand extends ServiceCommand {

    private final LinkedInAccountService linkedInAccountService;
    private final LinkedInBotConfig config;

    @Override
    public String getCommandIdentifier() {
        return GET_NEXT_PROFILES.getName();
    }

    @Override
    public String getDescription() {
        return GET_NEXT_PROFILES.getDescription();
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String getProfilesMessage;
        var chat = message.getChat();
        var userName = getUsername(chat);
        boolean userLoadedHisProfile = linkedInAccountService.validateUpload(chat.getId(), userName);
        if (!userLoadedHisProfile) {
            getProfilesMessage = GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText();
        } else {
            getProfilesMessage = getProfiles(chat, userName);
        }
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, getProfilesMessage, GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getParseMode());
    }

    private String getProfiles(Chat chat, String userName) {
        //TODO:после подключения настоящего биллинга эти проверки можно обобщить и вынести
        var isSizeGood = linkedInAccountService.checkRequesterLoadSize(chat.getId(), userName);
        if(!isSizeGood)
            return NEED_PAY_SIZE_MESSAGE.getText();

        var isBillingGood = linkedInAccountService.checkRequesterBillingTime(chat.getId(), userName);
        if(!isBillingGood)
            return NEED_PAY_MESSAGE.getText();

        String getProfilesMessage;
        var linkedInProfiles = linkedInAccountService.loadRandomRecords(chat.getId(), userName);
        if (linkedInProfiles.isEmpty()) {
            getProfilesMessage = GET_PROFILES_NO_USERS_MESSAGE.getText();
        } else {
            var response = linkedInProfiles.stream().map(LinkedInProfile::getLinkedInUrl).collect(Collectors.joining("\n\n\uD83D\uDE80"));
            getProfilesMessage = "\uD83D\uDE80" + response + "\n\nWith love from Israel HiTech\uD83D\uDE09";
        }
        return getProfilesMessage;
    }
}

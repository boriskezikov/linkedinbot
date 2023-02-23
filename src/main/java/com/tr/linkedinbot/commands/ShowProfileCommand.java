package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.logic.MetricSender;
import com.tr.linkedinbot.model.*;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.SHA256Digest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tr.linkedinbot.model.CommandEnum.GET_NEXT_PROFILES;
import static com.tr.linkedinbot.model.CommandEnum.SHOW_PROFILE;

@Component
@RequiredArgsConstructor
public class ShowProfileCommand extends ServiceCommand {

    private final LinkedInProfileRepository repository;

    private final MetricSender metricSender;

    @Override
    public String getCommandIdentifier() {
        return CommandEnum.SHOW_PROFILE.getName();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        metricSender.registerCommandUse(SHOW_PROFILE, message);

        var chat = message.getChat();
        var userName = getUsername(chat);

        var optionalLinkedInProfile = repository.getByChatId(chat.getId());
        if (optionalLinkedInProfile.isEmpty()) {
            sendAnswer(absSender, message.getChatId(), SHOW_PROFILE.getName(), userName, TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText(), ParseMode.HTML);
        } else {
            var linkedInProfile = optionalLinkedInProfile.get();
            sendAnswer(absSender, message.getChatId(), SHOW_PROFILE.getName(), userName, getFormat(linkedInProfile), ParseMode.HTML);
        }

    }

    private static String getFormat(LinkedInProfile linkedInProfile) {
        return String.format(
                TextConstants.PROFILE_MESSAGE.getText(),
                linkedInProfile.getLinkedInUrl(),
                Optional.ofNullable(linkedInProfile.getCountry()).orElse(Country.EMPTY),
                Optional.ofNullable(linkedInProfile.getRole()).orElse(Role.EMPTY),
                (linkedInProfile.getSearchRoles().isEmpty() ? Set.of(Role.EMPTY_SEARCH) : linkedInProfile.getSearchRoles())
                        .stream()
                        .map(Role::toString)
                        .collect(Collectors.joining(", "))
        );
    }
}

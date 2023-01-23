package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.model.BotState;
import com.tr.linkedinbot.repository.LinkedInProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ChangeLinkCommand extends ServiceCommand{

    private final LinkedInProfileRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "change_link";
    }

    @Override
    public String getDescription() {
        return "изменить ссылку на профиль";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        var chat = message.getChat();
        var userName = (chat.getUserName() != null) ? chat.getUserName() :
                String.format("%s %s", chat.getLastName(), chat.getFirstName());

        var optionalLinkedInProfile = repository.getByChatId(chat.getId());
        if (optionalLinkedInProfile.isEmpty()) {
            sendAnswer(absSender, message.getChatId(), "announce", userName, TextConstants.GET_PROFILES_LOAD_ACC_FIRST_MESSAGE.getText(), ParseMode.HTML);
        } else {
            var linkedInProfile = optionalLinkedInProfile.get();
            linkedInProfile.setState(BotState.CHANGE_LINK);
            repository.save(linkedInProfile);
            sendAnswer(absSender, message.getChatId(), "announce", userName, TextConstants.NEW_LINK_MESSAGE.getText(), ParseMode.HTML);
        }
    }

}

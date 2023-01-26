package com.tr.linkedinbot.commands.interactions;

import com.tr.linkedinbot.model.BotState;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Interaction {
    void interact(Message message);

    BotState getBotStateForInteraction();
}

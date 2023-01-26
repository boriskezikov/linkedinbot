package com.tr.linkedinbot.logic;

import com.tr.linkedinbot.model.CommandEnum;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class MetricSender {

    private final MeterRegistry registry;

    public void registerCommandUse(CommandEnum command, Message message) {
        Counter.builder("command")
                .tag("name", command.getName())
                .tag("chatId", message.getChatId().toString())
                .register(registry)
                .increment();
    }

    public void registerInteraction(Message message) {
        Counter.builder("interaction")
                .tag("chatId", message.getChatId().toString())
                .register(registry)
                .increment();
    }

    public void registerNewUserEvent(Message message) {
        Counter.builder("new_user_event")
                .register(registry)
                .increment();
    }

}

package com.tr.linkedinbot.config;

import com.tr.linkedinbot.commands.interactions.Interaction;
import com.tr.linkedinbot.model.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class InteractionConfiguration {

    private final List<Interaction> interactions;

    @Bean
    public Map<BotState, Interaction> interactionMap() {
        return interactions.stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                Interaction::getBotStateForInteraction,
                                interaction -> interaction
                        )
                );
    }
}

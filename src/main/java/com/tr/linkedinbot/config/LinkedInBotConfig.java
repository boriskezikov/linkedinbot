package com.tr.linkedinbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(value = "bot")
public class LinkedInBotConfig {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;
}

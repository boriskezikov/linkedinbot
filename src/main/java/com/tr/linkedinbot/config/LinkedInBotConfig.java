package com.tr.linkedinbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class LinkedInBotConfig {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.admin.name}")
    private String admin;

    @Value("${bot.random.limit}")
    private Integer randomLimit;
}

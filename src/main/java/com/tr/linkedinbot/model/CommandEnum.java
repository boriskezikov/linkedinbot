package com.tr.linkedinbot.model;

import lombok.Getter;

@Getter
public enum CommandEnum {
    ADMIN_COUNT("count", "number of users", "/count"),
    ADMIN_ANNOUNCE("announce", "", "/announce"),
    CHANGE_LINK("change_link","","/change_link"),
    GET_NEXT_PROFILES("get_next_profiles","Загрузить 10 следующий профилей","/get_next_profiles"),
    HELP("help","Помощь","/help"),
    FEEDBACK("feedback","Оставить отзыв/пожелание","/feedback"),
    START("start", "Начать", "/start");

    private final String name;

    private final String description;

    private final String buttonName;

    CommandEnum(String name, String description, String buttonName) {
        this.name = name;
        this.description = description;
        this.buttonName = buttonName;
    }

}

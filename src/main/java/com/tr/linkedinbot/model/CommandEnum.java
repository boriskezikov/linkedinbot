package com.tr.linkedinbot.model;

import lombok.Getter;

@Getter
public enum CommandEnum {
    ADMIN("admin", "", "/admin"),
    ADMIN_COUNT("count", "number of users", "/count"),
    ADMIN_ANNOUNCE("announce", "", "/announce"),
    GET_NEXT_PROFILES("get_next_profiles","Загрузить 10 следующий профилей","/get_next_profiles"),
    HELP("help","Помощь","/help"),
    FEEDBACK("feedback","Оставить отзыв/пожелание","/feedback"),
    START("start", "Начать", "/start"),
    SHOW_PROFILE("show_profile", "Показать профиль", "/show_profile"),
    CHANGE_LINK("change_link","","/change_link"),
    CHANGE_COUNTRY("change_country","","/change_country"),
    CHANGE_ROLE("change_role","","/change_role"),
    CHANGE_ROLE_SEARCH("change_search_roles","","/change_search_roles"),
    EXIT("exit","","/exit");

    private final String name;

    private final String description;

    private final String buttonName;

    CommandEnum(String name, String description, String buttonName) {
        this.name = name;
        this.description = description;
        this.buttonName = buttonName;
    }

}

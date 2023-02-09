package com.tr.linkedinbot.model;

public enum ButtonNameEnum {

    ADD_PROFILE("/add_profile"),
    ADMIN_COUNT("/count"),
    ADMIN("/admin"),
    ADMIN_MESSAGE("/announce"),
    GET_NEXT_PROFILES("/get_next_profiles"),
    HELP("/help"),
    FEEDBACK("/feedback"),

    SHOW_PROFILE("/show_profile"),
    CHANGE_LINK("/change_link"),
    CHANGE_COUNTRY("/change_country"),
    CHANGE_ROLE("/change_role"),
    CHANGE_SEARCH_ROLES("/change_search_roles"),
    EXIT("/exit");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}

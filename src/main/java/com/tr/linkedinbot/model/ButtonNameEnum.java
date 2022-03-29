package com.tr.linkedinbot.model;

public enum ButtonNameEnum {

    ADD_PROFILE("/add_profile"),
    ADMIN_COUNT("/count"),
    ADMIN_MESSAGE("/admin_message"),
    GET_PROFILES("/get_profiles"),
    GET_NEXT_PROFILES("/get_next_profiles"),
    HELP("/help");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}

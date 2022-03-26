package com.tr.linkedinbot.model;

public enum ButtonNameEnum {

    ADD_PROFILE("/add_profile"),
    GET_NEXT_PROFILES("/get_profiles"),
    HELP("/help");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}

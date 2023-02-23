package com.tr.linkedinbot.model;

public enum Country {
    ISRAEL("Israel"),
    EMPTY("Страна еще не установлена"),
    ALL("All");

    Country(String description) {
        this.description = description;
    }

    private final String description;

    @Override
    public String toString() {
        return description;
    }

}

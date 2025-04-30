package com.tr.linkedinbot.model;

public enum Country {
    ISRAEL("Israel"),
    EMPTY("Страна еще не установлена"),
    ALL("All");

    private final String description;

    Country(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

}

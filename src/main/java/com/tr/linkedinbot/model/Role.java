package com.tr.linkedinbot.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    DEVELOPER("Developer"),
    QA("QA"),
    MANAGER("Manager"),
    ANALYST("Analyst"),
    PRODUCT_OWNER("Product owner"),
    DESIGNER("Designer"),
    EMPTY("Роль еще не установлена"),
    EMPTY_SEARCH("Роли поиска еще не установлены");


    Role(String description) {
        this.description = description;
    }

    private final String description;

    public static Role fromString(String roleString) {
        for (var role : Role.values()) {
            if (role.description.equals(roleString)) {
                return role;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return description;
    }

}

package com.tr.linkedinbot.model;

public enum Role {
    DEVELOPER("Developer"),
    QA("QA"),
    MANAGER("Manager"),
    ANALYST("Analyst"),
    PRODUCT_OWNER("Product owner"),
    DESIGNER("Designer"),
    EMPTY("Роль еще не установлена"),
    EMPTY_SEARCH("Роли поиска еще не установлены");


    private final String description;

    Role(String description) {
        this.description = description;
    }

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

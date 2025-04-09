package com.tr.linkedinbot.commands;

import com.tr.linkedinbot.model.ButtonNameEnum;
import com.tr.linkedinbot.model.Country;
import com.tr.linkedinbot.model.Role;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardHelper {

    public static final ReplyKeyboardMarkup adminKeyboard;
    public static final ReplyKeyboardMarkup userKeyboard;
    public static final ReplyKeyboardMarkup profileKeyboard;
    public static final ReplyKeyboardMarkup rolesKeyboard;
    public static final ReplyKeyboardMarkup countriesKeyboard;
    public static final ReplyKeyboardMarkup startKeyboard;

    static {
        adminKeyboard = buildAdminKeyboard();
        userKeyboard = buildUserKeyboard();
        profileKeyboard = buildUserProfileKeyboard();
        rolesKeyboard = buildRolesKeyboard();
        countriesKeyboard = buildCountryKeyboard();
        startKeyboard = buildStartKeyboard();
    }

    private static ReplyKeyboardMarkup buildAdminKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.GET_NEXT_PROFILES.getButtonName()));
        row1.add(new KeyboardButton(ButtonNameEnum.SHOW_PROFILE.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.HELP.getButtonName()));
        row2.add(new KeyboardButton(ButtonNameEnum.FEEDBACK.getButtonName()));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(ButtonNameEnum.ADMIN_COUNT.getButtonName()));
        row3.add(new KeyboardButton(ButtonNameEnum.ADMIN_MESSAGE.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup buildUserKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.GET_NEXT_PROFILES.getButtonName()));
        row1.add(new KeyboardButton(ButtonNameEnum.SHOW_PROFILE.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.HELP.getButtonName()));
        row2.add(new KeyboardButton(ButtonNameEnum.FEEDBACK.getButtonName()));


        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup buildUserProfileKeyboard() {
        KeyboardRow row0 = new KeyboardRow();
        row0.add(new KeyboardButton(ButtonNameEnum.SHOW_PROFILE.getButtonName()));

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.CHANGE_LINK.getButtonName()));
        row1.add(new KeyboardButton(ButtonNameEnum.CHANGE_COUNTRY.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.CHANGE_ROLE.getButtonName()));
        row2.add(new KeyboardButton(ButtonNameEnum.CHANGE_SEARCH_ROLES.getButtonName()));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(ButtonNameEnum.EXIT.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row0);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
    private static ReplyKeyboardMarkup buildCountryKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Country.ISRAEL.name()));
        row1.add(new KeyboardButton(Country.ALL.name()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.EXIT.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup buildRolesKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Role.DEVELOPER.toString()));
        row1.add(new KeyboardButton(Role.QA.toString()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Role.MANAGER.toString()));
        row2.add(new KeyboardButton(Role.ANALYST.toString()));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(Role.DESIGNER.toString()));
        row3.add(new KeyboardButton(Role.PRODUCT_OWNER.toString()));

        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton(ButtonNameEnum.EXIT.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup buildStartKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.HELP.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

}

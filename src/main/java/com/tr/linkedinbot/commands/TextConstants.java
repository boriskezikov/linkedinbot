package com.tr.linkedinbot.commands;

public interface TextConstants {


    String GET_PROFILES_LOAD_ACC_FIRST_MESSAGE_TEXT = "По нашим правилам, сначала ты грузишь свой профиль, а потом мы покажем тебе чужие\n" +
            "\n\nWith Love TR++\uD83D\uDE09";

    String GET_PROFILES_NO_USERS_MESSAGE_TEXT = "Нам пока нечего тебе показать( Пингани ребят в чате, чтоб грузили свои профили!\n" +
            "\n\nWith Love TR++\uD83D\uDE09";

    String HELP_MESSAGE_TEXT = "Я помогу обзавестить коннектами с участниками нашего сообщества в LinkedIn!\n" +
            "Кидай ссылку на свой профиль, и я поделюсь с тобой контактами!\n\n" +
            "Вот пример рабочей ссылки: https://www.linkedin.com/in/boriskezikov/\n" +
            "Если что-то идет не так, пиши @kezik_off, он прикрутит костыль\uD83D\uDE09" +
            "\n\nWith Love TR++\uD83D\uDE09";

    String DONT_UNDERSTAND_GLOBAL_ERROR_TEXT = "Простите, я не понимаю Вас. Возможно, Вам поможет /help\n" +
            "\n\nWith Love TR++\uD83D\uDE09";

    String PROFILE_SAVED_MESSAGE_TEXT = "Сохранил твой профиль. Можешь приступать к добавлению новых контактов.\n" +
            "\n\nWith Love TR++\uD83D\uDE09";

    String START_MESSAGE_TEXT = "Прежде чем начнём, я расскажу как это работает!" +
            "\n\nКоманда /get_profiles позволяет получить список профилей в системе, что удобно, если ты только присоединился.\n\nВыполни команду, добавься в друзья, а дальше бот сам будет рассылать уведомления о новых профилях!" +
            "\n\nПрисылай ссылку на свой профиль в формате https://www.linkedin.com/in/...  и поехали!" +
            "\n\nWith Love TR++\uD83D\uDE09";

    String PROFILE_ALREADY_SAVED_ERROR_TEXT = "Ты уже загрузил профиль, большего не нужно :)";

    String INVALID_LINKEDIN_LINK_ERROR_TEXT = "Похоже ссылка не корректна и/или такой профиль не существует";

    String NEW_PROFILE_NOTIFICATION_MESSAGE_TEXT = "Новый профиль! Добавляй в друзья: ";

    String ADMIN_MESSAGE = "От админов\n";
}

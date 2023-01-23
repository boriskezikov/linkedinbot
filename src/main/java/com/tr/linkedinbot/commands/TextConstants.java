package com.tr.linkedinbot.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

@Getter
@RequiredArgsConstructor
public enum TextConstants {


    GET_PROFILES_LOAD_ACC_FIRST_MESSAGE("По нашим правилам, сначала ты грузишь свой профиль, а потом мы покажем тебе чужие\n" +
            "\n\nWith love from Israel HiTech\uD83D\uDE09", ParseMode.HTML),

    GET_PROFILES_NO_USERS_MESSAGE("Нам пока нечего тебе показать( Пингани ребят в чате, чтоб грузили свои профили!\n" +
            "\n\nWith love from Israel HiTech\uD83D\uDE09", ParseMode.HTML),

    HELP_MESSAGE("Я помогу обзавестить коннектами с участниками нашего сообщества в LinkedIn\\!\n" +
            "Кидай ссылку на свой профиль, и я поделюсь с тобой контактами\\!\n\n" +
            "Вот пример рабочей ссылки: `https://www\\.linkedin\\.com/in/boriskezikov/`\n" +
            "Если что\\-то идет не так, пиши @kezik\\_off, он прикрутит костыль\uD83D\uDE09" +
            "\n\nWith love from Israel HiTech\uD83D\uDE09", ParseMode.MARKDOWNV2),


    DONT_UNDERSTAND_GLOBAL_ERROR_MESSAGE("Простите, я не понимаю Вас. Возможно, Вам поможет /help\n" +
            "\n\nWith love from Israel HiTech\uD83D\uDE09", ParseMode.HTML),

    PROFILE_SAVED_MESSAGE("Сохранил твой профиль. Можешь приступать к добавлению новых контактов.\n" +
            "\n\nWith love from Israel HiTech\uD83D\uDE09", ParseMode.HTML),

    START_MESSAGE("Прежде чем начнём, я расскажу, как это работает!" +
            "\n\nКоманда /get_next_profiles позволяет получить список 10 случайных профилей в системе, что удобно, если ты только присоединился.\n\nВыполняй команду, добавляй в друзья, а дальше бот сам будет рассылать уведомления о новых профилях!" +
            "\n\nПрисылай ссылку на свой профиль в формате https://www.linkedin.com/in/...  и поехали!" +
            "\n\nОтправляя нам ссылку на свой профиль, ты даешь согласие на обработку и хранение персональных данных." +
            "\n\nWith love from Israel HiTech\uD83D\uDE09", ParseMode.HTML),

    PROFILE_ALREADY_SAVED_ERROR_MESSAGE("Ты уже загрузил профиль, большего не нужно :)", ParseMode.HTML),

    INVALID_LINKEDIN_LINK_ERROR_MESSAGE("Похоже ссылка некорректна/не является ссылкой на профиль/такой профиль не существует", ParseMode.HTML),

    NEW_PROFILE_NOTIFICATION_MESSAGE_MESSAGE("Новый профиль! Добавляй в друзья: ", ParseMode.HTML),

    ADMIN_MESSAGE("Напиши текст для объявления\n", ParseMode.HTML),

    ADMIN_INTRODUCTION("‼️Сообщение от админов‼️:\n", ParseMode.HTML),

    FEEDBACK("Напиши ниже, чтобы ты хотел передать разработчикам: предложения по новым фичам, сообщение о багах или просто благодарности\uD83D\uDE0A\nМы все читаем и ко всему прислушиваемся!", ParseMode.HTML),

    FEEDBACK_SENT("Твое сообщение передано разрабам, большое спасибо за предоставленный фидбек\uD83D\uDE0C\n", ParseMode.HTML),

    NEW_LINK_MESSAGE("Напиши новую ссылку:\n", ParseMode.HTML),

    LINK_CHANGED_MESSAGE("Ссылка успешно изменена\uD83E\uDD73\n", ParseMode.HTML);

    private final String text;
    private final String parseMode;

}

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

    HELP_MESSAGE("Команда /get_next_profiles позволяет получить профили с учетом твоих настроек поиска!\n\n" +
            "Команда /show_profile позволяет просмотреть-изменить-заполнить свой профиль!\n\n" +
            "Команда /feedback позволяет написать админам, если что-то пошло не так или есть пожелания по работе бота\n\n" +
            "With love from Israel HiTech\uD83D\uDE09", ParseMode.HTML),


    DONT_UNDERSTAND_GLOBAL_ERROR_MESSAGE("Простите, я не понимаю Вас. Возможно, Вам поможет /help\n" +
            "\n\nWith love from Israel HiTech\uD83D\uDE09", ParseMode.HTML),

    PROFILE_SAVED_MESSAGE("Теперь нужно заполнить профиль - для этого воспользуйся командой /show_profile.\n", ParseMode.HTML),

    START_MESSAGE("Привет! Я бот, который поможет тебе обзавестить коннектами с участниками нашего сообщества в LinkedIn" +
            "\n\n- Присылай ссылку на свой LinkedIn в формате https://www.linkedin.com/in/...  " +
            "\n\n- Заполняй профиль: укажи свою страну, роль, а также роли других участников, которые тебе рекомендовать." +
            "\n Это важно, иначе я не смогу тебе подобрать нужные коннекты\uD83D\uDE14" +
            "\n\n- Команда /get_next_profiles позволяет получить новые профили с учетом твоих настроек поиска " +
            "\n\n- Также тебе будут приходить уведомления с профилями новых пользователей, не пропусти!" +
            "\n\nОтправляя нам ссылку на свой профиль, ты даешь согласие на обработку и хранение персональных данных." +
            "\n\nWith love from Israel HiTech\uD83D\uDE09", ParseMode.HTML),

    PROFILE_ALREADY_SAVED_ERROR_MESSAGE("Ты уже загрузил профиль, большего не нужно :)", ParseMode.HTML),

    INVALID_LINKEDIN_LINK_ERROR_MESSAGE("Похоже ссылка некорректна/не является ссылкой на профиль/такой профиль не существует", ParseMode.HTML),

    NEW_PROFILE_NOTIFICATION_MESSAGE_MESSAGE("Новый профиль! Добавляй в друзья: ", ParseMode.HTML),

    ADMIN_MESSAGE("Напиши текст для объявления\n", ParseMode.HTML),

    ADMIN_INTRODUCTION("‼️Сообщение от админов‼️:\n", ParseMode.HTML),

    FEEDBACK_MESSAGE("Напиши ниже, чтобы ты хотел передать разработчикам: предложения по новым фичам, сообщение о багах или просто благодарности\uD83D\uDE0A\nМы все читаем и ко всему прислушиваемся!", ParseMode.HTML),

    FEEDBACK_SENT_MESSAGE("Твое сообщение передано разрабам, большое спасибо за предоставленный фидбек\uD83D\uDE0C\n", ParseMode.HTML),

    NEW_LINK_MESSAGE("Напиши новую ссылку:\n", ParseMode.HTML),

    LINK_CHANGED_MESSAGE("Ссылка успешно изменена\uD83E\uDD73\n", ParseMode.HTML),

    NEW_COUNTRY_MESSAGE("Выбери свою страну:\n", ParseMode.HTML),
    NEW_ROLE_SEARCH_MESSAGE("Добавь роли поиска:\n", ParseMode.HTML),
    NEW_ROLE_MESSAGE("Выбери свою позицию:\n", ParseMode.HTML),
    COMPLETE_PROFILE_MESSAGE("Привет! Не забудь заполнить свой профиль, так мы сможем рекомендовать его другим участникам и подбирать тебе подходящие профили\uD83D\uDE09\n", ParseMode.HTML),

    COUNTRY_CHANGED_MESSAGE("Страна поиска успешно изменена\uD83E\uDD73\n", ParseMode.HTML),
    SEARCH_ROLES_CHANGED_MESSAGE("Роль поиска изменена\uD83E\uDD73\n", ParseMode.HTML),
    ROLE_CHANGED_MESSAGE("Роль изменена\uD83E\uDD73\n", ParseMode.HTML),
    PROFILE_MESSAGE("Твой профиль\n\uD83D\uDD17ссылка: %s\n\uD83D\uDCCD страна поиска: %s\n\uD83E\uDDD1\u200D\uD83D\uDCBB твоя роль : %s\n\uD83D\uDD0E теги поиска: %s", ParseMode.HTML),
    EXIT_MESSAGE("Изменения сохранены!", ParseMode.HTML),
    EXIT_MESSAGE_FULL_PROFILE("Молодец, ты заполнил профиль\uD83D\uDE0E\n Теперь бот будет рекомендовать тебе людей с учетом твоего выбора, также ты можешь получить новые профили по команде /get_next_profiles", ParseMode.HTML);

    private final String text;
    private final String parseMode;

}

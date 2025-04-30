alter table linked_in_profile
    add column showed_ids bigint[];

alter table linked_in_profile
    add column last_profile_get timestamp default now() not null;

--Добавим самого себя в список выданных профилей, чтобы мой профиль не появился в выдаче.
update linked_in_profile set showed_ids = ARRAY_APPEND (showed_ids, chat_id);
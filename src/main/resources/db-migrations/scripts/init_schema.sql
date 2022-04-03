create table if not exists linked_in_profile
(
    chat_id       bigint                  not null
        primary key,
    linked_in_url varchar(255)            not null,
    tg_user       varchar(255)            not null,
    registered_at timestamp default now() not null,
    to_remove     boolean   default false not null
);

alter table linked_in_profile
    add column if not exists registered_at timestamp default now() not null;
alter table linked_in_profile
    add column if not exists to_remove boolean default false not null;
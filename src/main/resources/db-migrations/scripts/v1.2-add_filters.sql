alter table linked_in_profile
    add column country varchar(32),
    add column role varchar(32),
    add column search_roles varchar[];

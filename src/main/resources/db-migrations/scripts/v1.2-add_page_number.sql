alter table linked_in_profile
    add column page_number integer default 0;
update linked_in_profile set page_number = 0 where page_number is null;

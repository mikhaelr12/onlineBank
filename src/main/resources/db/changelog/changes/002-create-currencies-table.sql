create table currencies
(
    id            bigint       not null primary key,
    currency_name varchar(255) not null unique
);
alter table currencies owner to root;
create sequence currency_id_seq;
alter sequence currency_id_seq owner to root
create table accounts
(
    id             bigint      not null primary key,
    account_number varchar(20) not null unique,
    created_at     date        not null default current_date,
    balance        decimal(20, 2)       default 0,
    user_id        bigint      not null,
    currency_id    int         not null,
    constraint FK_USER_ACCOUNT foreign key (user_id) references users (id),
    constraint FK_CURRENCY_ACCOUNT foreign key (currency_id) references currencies (id)
);

alter table accounts owner to root;
create sequence account_id_seq;
alter sequence account_id_seq owner to root;
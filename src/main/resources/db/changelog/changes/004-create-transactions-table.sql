create table transactions
(
    id               bigint         not null primary key,
    sender_id        bigint         not null,
    receiver_id      bigint         not null,
    amount           decimal(20, 2) not null,
    transaction_date date           not null,
    transaction_time time           not null,
    constraint FK_SENDER_TRANSACTION foreign key (sender_id) references accounts (id),
    constraint FK_RECEIVER_TRANSACTION foreign key (receiver_id) references accounts (id)
);

alter table transactions owner to root;
create sequence transaction_id_seq;
alter sequence transaction_id_seq owner to root;
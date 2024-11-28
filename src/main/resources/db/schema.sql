create table USER_INFO
(
    USER_SEQ   int auto_increment
        primary key,
    EMAIL      varchar(100)                  not null,
    PASSWORD   varchar(2000)                 not null,
    USER_NAME  varchar(100)                  not null,
    CREATED_AT timestamp                     not null,
    UPDATED_AT timestamp                     not null,
    EXPIRE_AT  datetime                      not null,
    IS_LOCK    varchar(2)  default 'N'       not null,
    USER_ROLE  varchar(10) default 'NO_CERT' not null,
    constraint IDX_USER_INFO_LOGIN_ID
        unique (EMAIL)
)
    comment 'USER_INFO';

create index IDX_USER_INFO_SEQ
    on USER_INFO (USER_SEQ);



create table LOGIN_HISTORY
(
    LOGIN_HISTORY_SEQ int auto_increment
        primary key,
    LOGIN_ACCESS_TIME timestamp default CURRENT_TIMESTAMP null,
    ACCESS_CD         varchar(10)                         not null,
    EMAIL             varchar(100)                        null
);

create index IDX_LOGIN_HISTORY_LOGIN_ACCESS_TIME
    on LOGIN_HISTORY (LOGIN_ACCESS_TIME desc);

create index LOGIN_HISTORY_IDX
    on LOGIN_HISTORY (EMAIL);






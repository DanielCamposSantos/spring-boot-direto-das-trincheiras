create table `anime-service`.anime
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);

create table `anime-service`.producer
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  not null,
    name       varchar(255) not null
);


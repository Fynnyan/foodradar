create type workdays as enum ('monday', 'tuesday', 'wednesday', 'thursday', 'friday');

create table food_truck
(
    id       uuid primary key,
    name     varchar(100),
    web      varchar(256),
    location varchar(100),
    days     int array
);

create table place
(
    id   uuid primary key,
    name varchar(100),
    web  varchar(256)
);

create table course
(
    id       uuid primary key,
    place_id uuid references place,
    date     date,
    name     varchar(100),
    price    varchar(100)
);
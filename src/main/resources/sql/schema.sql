create type gender as enum ('MALE', 'FEMALE');
create type occupation as enum ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

create table member (
    id serial primary key,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    birthdate date not null,
    gender gender not null,
    address varchar(255) not null,
    phone integer not null,
    profession varchar(255) not null,
    email varchar(255) not null,
    occupation occupation not null
);

create table reference (
    id serial primary key,
    id_member_refered integer not null references member(id),
    id_member_referer integer not null references member(id)
);

create table collectivity (
    id serial primary key,
    location varchar(255) not null,
    president_id integer references member(id),
    treasurer_id integer references member(id),
    vice_president_id integer references member(id),
    secretary_id integer references member(id)
);

create table member_collectivity (
    id_member integer not null references member(id),
    id_collectivity integer not null references collectivity(id)
);


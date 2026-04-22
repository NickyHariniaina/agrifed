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
    id_collectivity integer not null references collectivity(id),

    primary key (id_member, id_collectivity)
);

alter table member ADD COLUMN joined_at TIMESTAMP NOT NULL DEFAULT NOW();
alter table collectivity
    add column federation_number integer unique,
    add column name varchar(255) unique;

create type freqency as enum ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');

create type status as enum ('ACTIVE', 'INACTIVE');

create table membership_fee (
    id serial primary key,
    eligible_from date not null,
    frequency freqency not null,
    amount numeric(10,2) not null check (amount >= 0),
    label varchar(255) not null,
    status status not null
);

alter table membership_fee add column id_collectivity integer references collectivity(id);
alter type freqency rename to frequency;

create type payment_mode          as enum ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER');
create type account_type          as enum ('CASH', 'MOBILE_BANKING', 'BANK');
create type mobile_banking_service as enum ('AIRTEL_MONEY', 'MVOLA', 'ORANGE_MONEY');
create type bank_name             as enum ('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BAQUE', 'BAOBAB', 'SIPEM');

create table financial_account (
                                   id                    serial primary key,
                                   account_type          account_type    not null,
                                   amount                numeric(15, 2)  not null default 0,

                                   holder_name           varchar(255),
                                   mobile_banking_service mobile_banking_service,
                                   mobile_number         bigint,

                                   bank_name             bank_name,
                                   bank_code             integer,
                                   bank_branch_code      integer,
                                   bank_account_number   bigint,
                                   bank_account_key      integer
);



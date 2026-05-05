create type gender as enum ('MALE', 'FEMALE');
create type occupation as enum ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

create table member (
    id varchar(36) primary key default gen_random_uuid(),
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
    id varchar(36) primary key default gen_random_uuid(),
    id_member_refered varchar(36) not null references member(id),
    id_member_referer varchar(36) not null references member(id)
);

create table collectivity (
    id varchar(36) primary key default gen_random_uuid(),
    location varchar(255) not null,
    president_id varchar(36) references member(id),
    treasurer_id varchar(36) references member(id),
    vice_president_id varchar(36) references member(id),
    secretary_id varchar(36) references member(id)
);

create table member_collectivity (
    id_member varchar(36) not null references member(id),
    id_collectivity varchar(36) not null references collectivity(id),

    primary key (id_member, id_collectivity)
);

alter table member ADD COLUMN joined_at TIMESTAMP NOT NULL DEFAULT NOW();
alter table collectivity
    add column federation_number integer unique,
    add column name varchar(255) unique;

create type freqency as enum ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');

create type status as enum ('ACTIVE', 'INACTIVE');

create table membership_fee (
    id varchar(36) primary key default gen_random_uuid(),
    eligible_from date not null,
    frequency freqency not null,
    amount numeric(10,2) not null check (amount >= 0),
    label varchar(255) not null,
    status status not null
);

alter table membership_fee add column id_collectivity varchar(36) references collectivity(id);
alter type freqency rename to frequency;

create type payment_mode          as enum ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER');
create type account_type          as enum ('CASH', 'MOBILE_BANKING', 'BANK');
create type mobile_banking_service as enum ('AIRTEL_MONEY', 'MVOLA', 'ORANGE_MONEY');
create type bank_name             as enum ('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BAQUE', 'BAOBAB', 'SIPEM');

create table financial_account (
                                   id                    varchar(36) primary key default gen_random_uuid(),
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

create table member_payment (
                                id                    varchar(36) primary key default gen_random_uuid(),
                                id_member             varchar(36)        not null references member(id),
                                id_membership_fee     varchar(36)        not null references membership_fee(id),
                                id_financial_account  varchar(36)        not null references financial_account(id),
                                amount                numeric(15, 2) not null,
                                payment_mode          payment_mode   not null,
                                creation_date         date           not null default current_date
);

create table collectivity_transaction (
                                          id                    varchar(36) primary key default gen_random_uuid(),
                                          id_collectivity       varchar(36)        not null references collectivity(id),
                                          id_member             varchar(36)        not null references member(id),
                                          id_financial_account  varchar(36)        not null references financial_account(id),
                                          amount                numeric(15, 2) not null,
                                          payment_mode          payment_mode   not null,
                                          creation_date         date           not null default current_date
);
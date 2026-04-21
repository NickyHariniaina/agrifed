create database agrifed;

create user agrifed_db_manager with password '123456';

grant connect on database agrifed to agrifed_db_manager;

\c agrifed

grant usage, create on schema public to agrifed_db_manager;

alter default privileges in schema public
grant select, insert, update, delete on tables to agrifed_db_manager;

alter default privileges in schema public
grant usage, update, select on sequences to agrifed_db_manager;

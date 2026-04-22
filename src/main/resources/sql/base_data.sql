insert into member (firstname, lastname, birthdate, gender, address, phone, profession, email, occupation)
values ('Nicky', 'Hariniaina', '2007-04-12', 'MALE', '123 Main Street', '0381584053', 'Agriculture', 'ferrissushi@gmail.com', 'SENIOR');

insert into collectivity (location, president_id, treasurer_id, vice_president_id, secretary_id)
values ('Ambatondrazaka', 1, 1, 1, 1);

insert into member_collectivity (id_member, id_collectivity)
values (1, 1);

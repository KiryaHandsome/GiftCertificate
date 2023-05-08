UPDATE tag
SET name = ?
WHERE id = ?;

insert into tag(name)
values ('first tag name');
insert into tag(name)
values ('beauty');
insert into tag(name)
values ('sport');

insert into gift_certificate(name, description, duration, create_date, last_update_date)
values ('some description', 56, '2023-4-27 10:23:54+02', '2023-4-27 10:23:54+02');
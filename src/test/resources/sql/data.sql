insert into tag(name)
values ('first tag name');
insert into tag(name)
values ('beauty');
insert into tag(name)
values ('sport');
insert into tag(name)
values ('entertainment');
insert into tag(name)
values ('gambling');
insert into tag(name)
values ('food');


insert into gift_certificate(name, description, duration, create_date, last_update_date)
values ('firstName', 'some description', 56, '2023-4-27 10:23:54+02', '2023-4-27 10:23:54+02');

insert into gift_certificate(name, description, duration, create_date, last_update_date)
values ('secondName', 'awesome certificate', 3, '2023-4-26 11:22:54+02', '2023-4-26 11:22:54+02');

insert into gift_certificate(name, description, duration, create_date, last_update_date)
values ('thirdName', '3description', 20, '2023-4-22 15:23:42+02', '2023-4-22 15:23:42+02');

insert into gift_certificate_tag values (1, 1);
insert into gift_certificate_tag values (1, 2);
insert into gift_certificate_tag values (2, 1);
insert into gift_certificate_tag values (3, 4);
insert into gift_certificate_tag values (3, 3);
insert into gift_certificate_tag values (3, 2);
CREATE SCHEMA IF NOT EXISTS gift_shop;

CREATE TABLE IF NOT EXISTS gift_shop.users
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS gift_shop.gift_certificates
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255)             NOT NULL,
    description      VARCHAR(255),
    price            DOUBLE PRECISION         NOT NULL,
    duration         INT                      NOT NULL CHECK (duration > 0),
    create_date      TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS gift_shop.orders
(
    id             SERIAL PRIMARY KEY,
    total_cost     DOUBLE PRECISION         NOT NULL,
    purchase_date  TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id        SERIAL REFERENCES gift_shop.users (id),
    certificate_id SERIAL REFERENCES gift_shop.gift_certificates (id)
);

CREATE TABLE IF NOT EXISTS gift_shop.tags
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS gift_shop.gift_certificate_tag
(
    gift_certificate_id SERIAL REFERENCES gift_shop.gift_certificates (id),
    tag_id              SERIAL REFERENCES gift_shop.tags (id)
);

insert into gift_shop.tags(name)
values ('first tag name');
insert into gift_shop.tags(name)
values ('beauty');
insert into gift_shop.tags(name)
values ('sport');
insert into gift_shop.tags(name)
values ('entertainment');
insert into gift_shop.tags(name)
values ('gambling');
insert into gift_shop.tags(name)
values ('food');


insert into gift_shop.gift_certificates(name, description, price, duration, create_date, last_update_date)
values ('firstName', 'some description', 56, 11.20, '2023-4-27 10:23:54+02', '2023-4-27 10:23:54+02');

insert into gift_shop.gift_certificates(name, description, price, duration, create_date, last_update_date)
values ('secondName', 'awesome certificate', 55.32, 3, '2023-4-26 11:22:54+02', '2023-4-26 11:22:54+02');

insert into gift_shop.gift_certificates(name, description, price, duration, create_date, last_update_date)
values ('thirdName', '3description', 20.12, 20, '2023-4-22 15:23:42+02', '2023-4-22 15:23:42+02');

insert into gift_shop.gift_certificate_tag
values (1, 1);
insert into gift_shop.gift_certificate_tag
values (1, 2);
insert into gift_shop.gift_certificate_tag
values (2, 1);
insert into gift_shop.gift_certificate_tag
values (3, 4);
insert into gift_shop.gift_certificate_tag
values (3, 3);
insert into gift_shop.gift_certificate_tag
values (3, 2);

insert into gift_shop.users("name")
values ('first name');
insert into gift_shop.users("name")
values ('second name');

insert into gift_shop.orders(total_cost, purchase_date, user_id, certificate_id)
values (55.32, '2023-4-26 11:22:54+02', 1, 2);
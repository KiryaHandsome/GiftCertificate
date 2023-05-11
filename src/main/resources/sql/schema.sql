CREATE SCHEMA IF NOT EXISTS gift_shop;

CREATE TABLE IF NOT EXISTS gift_shop.users
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS gift_shop.orders
(
    id            SERIAL PRIMARY KEY,
    total_cost    DOUBLE PRECISION         NOT NULL,
    purchase_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS gift_shop.order_certificate
(
    order_id         SERIAL REFERENCES gift_shop.orders (id),
    gift_certificate SERIAL REFERENCES gift_shop.gift_certificates (id)
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
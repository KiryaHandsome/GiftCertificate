CREATE TABLE IF NOT EXISTS gift_certificate
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255)             NOT NULL,
    description      VARCHAR(255),
    duration         INT                      NOT NULL CHECK (duration > 0),
    create_date      TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS tag
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS gift_certificate_tag
(
    gift_certificate_id SERIAL REFERENCES gift_certificate (id),
    tag_id              SERIAL REFERENCES tag (id)
);
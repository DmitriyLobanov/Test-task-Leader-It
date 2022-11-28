CREATE TABLE IF NOT EXISTS clients
(
    id          BIGSERIAL    NOT NULL,
    first_name  varchar(255) NOT NULL,
    second_name varchar(255) NOT NULL,
    surname     varchar(255) NOT NULL,
    secret_key  varchar(255) NOT NULL,
    PRIMARY KEY (id)
);


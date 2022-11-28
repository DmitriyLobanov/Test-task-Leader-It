CREATE TABLE IF NOT EXISTS client_bank_accounts
(
    id             BIGSERIAL   NOT NULL,
    account_number BIGINT      NOT NULL UNIQUE,
    amount         numeric     NOT NULL,
    account_type   varchar(20) NOT NULL,
    created_date   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    validity       date        NOT NULL,
    client_id      BIGINT      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients (id)
);


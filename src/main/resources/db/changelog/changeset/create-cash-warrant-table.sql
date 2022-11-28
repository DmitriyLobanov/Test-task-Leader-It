CREATE TABLE IF NOT EXISTS cash_warrants
(
    id               BIGSERIAL   NOT NULL,
    warrant_type     varchar(20) NOT NULL,
    amount           numeric     NOT NULL,
    execution_result varchar(40) NOT NULL,
    created_date     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    account_id       bigint      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES client_bank_accounts (id)


);


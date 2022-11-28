CREATE TABLE transactions
(
    id                          BIGSERIAL   NOT NULL,
    amount                      numeric     NOT NULL,
    transaction_type            varchar(20) NOT NULL,
    execution_result            varchar(40) NOT NULL,
    created_date                timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    beneficiary_bank_account_id bigint      NOT NULL,
    sender_bank_account_id      bigint,
    cash_warrant_id             bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (beneficiary_bank_account_id) REFERENCES client_bank_accounts (id),
    FOREIGN KEY (sender_bank_account_id) REFERENCES client_bank_accounts (id),
    FOREIGN KEY (cash_warrant_id) REFERENCES cash_warrants (id)
);


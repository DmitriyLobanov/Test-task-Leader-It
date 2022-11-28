INSERT INTO clients(first_name, second_name, surname, secret_key)
VALUES ('Dmitriy', 'Lobanov', 'Alexandrovich', '$2a$10$CQWEBgNF15.ABEVHh/lM3.Sfmbm1QAfxIwfoK.WXl/NQNk0DRglc.'),
       ('Sergei', 'Simonov', 'Evgenyevich', '$2a$10$ZdhmWoyqHHwYddhHAYWFie8CMfvu8l.BWQEcQ.6kCa53zcOPubOHe');

INSERT INTO client_bank_accounts(account_number, amount, account_type, validity, client_id)
VALUES (10452313123, 150.00, 'CURRENT', '2022-12-28', 1),
       (12315545131, 450.00, 'DEPOSIT', '2022-12-28', 1),
       (12315513512, 100.00, 'CREDIT', '2022-1-28', 1),
       (3425513512, 200.00, 'CURRENT', '2022-1-28', 2);

INSERT INTO cash_warrants(warrant_type, amount, execution_result, account_id)
VALUES ('REPLENISHMENT', 100.00, 'SUCCESS', 1),
       ('WITHDRAWAL', 10000.00, 'FAILED_NOT_ENOUGH_MONEY', 1);

INSERT INTO transactions(amount, transaction_type, execution_result, beneficiary_bank_account_id, sender_bank_account_id, cash_warrant_id)
VALUES (10000.00, 'WITHDRAWAL', 'FAILED_NOT_ENOUGH_MONEY', 1, null, 1);



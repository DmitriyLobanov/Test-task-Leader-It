# Для запуска
Нужна база данных `PostgreSQL`.  
  
а) Если есть docker, то в корне проекта находится файл `docker-compose.yaml`. Прописываем комманду docker-compose up -d.  
б) Если docker-a нет, то устанавливаем вручную `url`, `username`, `password` в файле `application-dev.yaml`.  
  
В файле src/main/resources/db/changelog/changeset/fill-tables.yaml находится файл миграции, который заполнит таблицу данными.  

________________________
# REST API


## 1. Получить информацию обо всех клиентах

### Запрос 

### `GET http://localhost:8080/api/v1/clients`

### Ответ
```json
[
    {
        "id": 1,
        "firstName": "Dmitriy",
        "secondName": "Lobanov",
        "surname": "Alexandrovich"
    },
    {
        "id": 2,
        "firstName": "Sergei",
        "secondName": "Simonov",
        "surname": "Evgenyevich"
    }
]
```

## 2. Получить информацию о клиенте по его идентификатору

### Запрос 

### `GET http://localhost:8080/api/v1/clients/1`

### Ответ
```json
{
    "id": 1,
    "firstName": "Dmitriy",
    "secondName": "Lobanov",
    "surname": "Alexandrovich"
}
```

## 3.  Получить информацию о счетах клиента

### Запрос 

### `GET http://localhost:8080/api/v1/bank_account/1`

### Ответ
```json
[
    {
        "id": 1,
        "accountNumber": 10452313123,
        "amount": 150.00,
        "accountType": "CURRENT",
        "createdDate": "2022-11-28T10:21:09.416843Z",
        "validity": "2022-12-28",
        "clientId": 1
    },
    {
        "id": 2,
        "accountNumber": 12315545131,
        "amount": 450.00,
        "accountType": "DEPOSIT",
        "createdDate": "2022-11-28T10:21:09.416843Z",
        "validity": "2022-12-28",
        "clientId": 1
    },
    {
        "id": 3,
        "accountNumber": 12315513512,
        "amount": 100.00,
        "accountType": "CREDIT",
        "createdDate": "2022-11-28T10:21:09.416843Z",
        "validity": "2022-01-28",
        "clientId": 1
    }
]
```

## 4.  - Получить информацию о транзакциях по счету клиента

### Запрос 

### `GET http://localhost:8080/api/v1/bank_account/1/transactions`

# Ответ
```json
[
    {
        "id": 1,
        "createdDate": "2022-11-28T10:21:09.416843Z",
        "amount": 10000.00,
        "transactionType": "WITHDRAWAL",
        "beneficiaryClientBankAccountId": 1,
        "cashWarrantId": 1,
        "executionResult": "FAILED_NOT_ENOUGH_MONEY"
    }
]
```

## 5. Получить информацию о кассовых ордерах по счету клиента

### Запрос 

### `GET http://localhost:8080/api/v1/bank_account/1/warrants`

# Ответ
```json
[
    {
        "id": 1,
        "warrantType": "REPLENISHMENT",
        "amount": 100.00,
        "clientBankAccountId": 1,
        "executionResult": "SUCCESS",
        "createdDate": "2022-11-28T10:21:09.416843Z"
    },
    {
        "id": 2,
        "warrantType": "WITHDRAWAL",
        "amount": 10000.00,
        "clientBankAccountId": 1,
        "executionResult": "FAILED_NOT_ENOUGH_MONEY",
        "createdDate": "2022-11-28T10:21:09.416843Z"
    },
    {
        "id": 3,
        "warrantType": "WITHDRAWAL",
        "amount": 1000,
        "clientBankAccountId": 1,
        "executionResult": "FAILED_WRONG_SECRET_KEY",
        "createdDate": "2022-11-28T10:24:12.630Z"
    },
    {
        "id": 4,
        "warrantType": "WITHDRAWAL",
        "amount": 1000,
        "clientBankAccountId": 1,
        "executionResult": "FAILED_WRONG_SECRET_KEY",
        "createdDate": "2022-11-28T10:29:16.253Z"
    }
]
```

## 6.  Создать кассовый ордер

### Запрос 

### `POST localhost:8080/api/v1/bank_account/warrant`

```json
{
    "secretKey": "secret_key_test",
    "warrantType": "WITHDRAWAL",
    "amount": 1000,
    "beneficiaryClientAccount": 1
}
```

# Ответ 
```json
{
    "executionResult": "FAILED_WRONG_SECRET_KEY"
}
```

## 7.  Создать перевод между счетами одного пользователя

### Запрос 

### `POST http://localhost:8080/api/v1/transfer`

```json
{
    "beneficiaryClientBankAccountId": 1,
    "senderClientBankAccountId": 2,
    "amount": 19.9999,
    "secretKey": "test"
}
```

### Ответ

```json
{
    "executionResult": "SUCCESS"
}
```


## 8.  Создать перевод между счетами разных пользователей

### Запрос 

### `POST http://localhost:8080/api/v1/transfer`

```json
{
    "beneficiaryClientBankAccountId": 1,
    "senderClientBankAccountId": 3,
    "amount": 19.9999,
    "secretKey": "test"
}
```

### Ответ

```json
{
    "executionResult": "FAILED_VALIDITY_EXPIRED"
}
```

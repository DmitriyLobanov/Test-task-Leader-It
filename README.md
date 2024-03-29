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

### `GET http://localhost:8080/api/v1/bank-accounts/1`

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

### `GET  http://localhost:8080/api/v1/bank-accounts/1/transactions`

### Ответ
```json
[
    {
        "id": 1,
        "createdDate": "2022-11-29T06:42:22.894137Z",
        "amount": 100.00,
        "transactionType": "REPLENISHMENT",
        "beneficiaryClientBankAccountId": 1,
        "cashWarrantId": 1,
        "executionResult": "SUCCESS"
    },
    {
        "id": 2,
        "createdDate": "2022-11-29T06:42:22.894137Z",
        "amount": 10000.00,
        "transactionType": "WITHDRAWAL",
        "beneficiaryClientBankAccountId": 1,
        "cashWarrantId": 2,
        "executionResult": "FAILED_NOT_ENOUGH_MONEY"
    }
]
```

## 5. Получить информацию о кассовых ордерах по счету клиента

### Запрос 

### `GET http://localhost:8080/api/v1/bank-accounts/1/warrants`

### Ответ 
```json
[
    {
        "id": 1,
        "warrantType": "REPLENISHMENT",
        "amount": 100.00,
        "clientBankAccountId": 1,
        "executionResult": "SUCCESS",
        "createdDate": "2022-11-29T06:42:22.894137Z"
    },
    {
        "id": 2,
        "warrantType": "WITHDRAWAL",
        "amount": 10000.00,
        "clientBankAccountId": 1,
        "executionResult": "FAILED_NOT_ENOUGH_MONEY",
        "createdDate": "2022-11-29T06:42:22.894137Z"
    }
]
```

## 6.  Создать кассовый ордер

### Создание кассового ордера на пополнение 

### `POST localhost:8080/api/v1/bank-accounts/warrants`

```json
{
    "warrantType": "REPLENISHMENT",
    "amount": 100,
    "beneficiaryClientAccount": 1
}
```

### Ответ
```json
{
    "executionResult": "SUCCESS"
}
```

### Создание кассового ордера на снятие

### В случае успеха:

### `POST localhost:8080/api/v1/bank-accounts/warrants`

```json
{
  "secretKey": "test",
  "warrantType": "WITHDRAWAL",
  "amount": 100,
  "beneficiaryClientAccount": 1
}
```
### Ответ
```json
{
    "executionResult": "SUCCESS"
}
```

### В случае указания неправильного секретного слова

### `POST localhost:8080/api/v1/bank-accounts/warrants`

```json
{
    "secretKey": "wrong_secret_key",
    "warrantType": "WITHDRAWAL",
    "amount": 100,
    "beneficiaryClientAccount": 1
}

```
### Ответ
```json
{
    "timeStamp": "2022-11-29T13:46:50.719",
    "message": "Invalid secret key",
    "errors": {
        "exception": "com.lobanov.financeservice.exceptions.WrongSecretKeyException"
    }
}
```

### В случае когда сумма снятия больше, чем сумма имеющаяся на счету клиента. 

### `POST localhost:8080/api/v1/bank-accounts/warrants`

```json
{
  "secretKey": "test",
  "warrantType": "WITHDRAWAL",
  "amount": 10000,
  "beneficiaryClientAccount": 1
}
``` 
### Ответ

```json
{
    "timeStamp": "2022-11-29T13:46:37.598",
    "message": "Not enough money on the account with id 1",
    "errors": {
        "exception": "com.lobanov.financeservice.exceptions.NotEnoughMoneyException"
    }
}
```

### В случае когда срок действия счета клиента истек

### `POST localhost:8080/api/v1/bank-accounts/warrants`

```json
{
  "secretKey": "test",
  "warrantType": "WITHDRAWAL",
  "amount": 10000,
  "beneficiaryClientAccount": 3
}
``` 
### Ответ

```json
{
  "timeStamp": "2022-11-29T15:44:11.6",
  "message": "Client bank account with id 3 overdue",
  "errors": {
    "exception": "com.lobanov.financeservice.exceptions.ValidityExpiredException"
  }
}
```

## 7.  Создать перевод между счетами одного пользователя

### Запрос в случае успеха

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

### В случае указания неправильного пароля

### `POST http://localhost:8080/api/v1/transfer`

````json
{
    "beneficiaryClientBankAccountId": 1,
    "senderClientBankAccountId": 2,
    "amount": 19.9999,
    "secretKey": "wrong_secret_key"
}
````

### Ответ
```json
{
  "timeStamp": "2022-11-29T15:46:52.133",
  "message": "Wrong secret key",
  "errors": {
    "exception": "com.lobanov.financeservice.exceptions.WrongSecretKeyException"
  }
}
```

### В случае, если у счета отправителя недостаточно денег 
### `POST http://localhost:8080/api/v1/transfer`

````json
{
  "beneficiaryClientBankAccountId": 1,
  "senderClientBankAccountId": 2,
  "amount": 1009.9999,
  "secretKey": "test"
}
````
### Ответ
```json
{
    "timeStamp": "2022-11-29T13:54:20.889",
    "message": "Not enough money on the account with id 2",
    "errors": {
        "exception": "com.lobanov.financeservice.exceptions.NotEnoughMoneyException"
    }
}
```

### В случае, если срок действия счета истек
### `POST http://localhost:8080/api/v1/transfer`

````json
{
  "beneficiaryClientBankAccountId": 1,
  "senderClientBankAccountId": 3,
  "amount": 19.99,
  "secretKey": "test"
}
````
### Ответ
```json
{
    "timeStamp": "2022-11-29T13:54:55.084",
    "message": "Client  bank account is overdue",
    "errors": {
        "exception": "com.lobanov.financeservice.exceptions.ValidityExpiredException"
    }
}
```


## 8.  Создать перевод между счетами разных пользователей

### Логика обработки запроса такая же  как в 7 пункте. Для перевода межу разными счетами в теле запроса указываются соответсующие   beneficiaryClientBankAccountId и senderClientBankAccountId

### `POST http://localhost:8080/api/v1/transfer`

```json
{
    "beneficiaryClientBankAccountId": 1,
    "senderClientBankAccountId": 4,
    "amount": 1.9999,
    "secretKey": "Sergei"
}
```
### Ответ
```json
{
    "executionResult": "SUCCESS"
}
```


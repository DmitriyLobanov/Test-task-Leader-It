package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.requests.CashWarrantDtoRequest;
import com.lobanov.financeservice.dtos.responses.CashWarrantDtoResponse;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.TransactionType;
import com.lobanov.financeservice.enums.WarrantType;
import com.lobanov.financeservice.exceptions.NotEnoughMoneyException;
import com.lobanov.financeservice.exceptions.ResourceNotFoundException;
import com.lobanov.financeservice.exceptions.ValidityExpiredException;
import com.lobanov.financeservice.exceptions.WrongSecretKeyException;
import com.lobanov.financeservice.mappers.CashWarrantMapper;
import com.lobanov.financeservice.models.CashWarrantEntity;
import com.lobanov.financeservice.models.ClientBankAccountEntity;
import com.lobanov.financeservice.models.TransactionEntity;
import com.lobanov.financeservice.repositories.CashWarrantRepository;
import com.lobanov.financeservice.repositories.ClientBankAccountRepository;
import com.lobanov.financeservice.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.lobanov.financeservice.enums.ExecutionResult.*;

@Service
@RequiredArgsConstructor
public class CashWarrantService {
    private final CashWarrantRepository cashWarrantRepository;

    private final ClientBankAccountRepository clientBankAccountRepository;

    private final TransactionRepository transactionRepository;

    private final CashWarrantMapper cashWarrantMapper;

    private final BCryptPasswordEncoder encoder;

    public List<CashWarrantDtoResponse> findAllCashWarrantsByClientBankAccountId(Long clientBankAccountId) {
        return cashWarrantRepository.findCashWarrantsByClientBankAccountId(clientBankAccountId).stream()
                .map(cashWarrantMapper::toDto).collect(Collectors.toList());
    }

    public ExecutionResult createReplenishmentCashWarrant(CashWarrantDtoRequest cashWarrantDtoRequest) {
        ExecutionResult executionResult;
        ClientBankAccountEntity clientBankAccountEntity = clientBankAccountRepository
                .findById(cashWarrantDtoRequest.getBeneficiaryClientAccount())
                .orElseThrow(() -> new ResourceNotFoundException("Client Bank account with id " + cashWarrantDtoRequest.getBeneficiaryClientAccount() + " not found"));

        executionResult = SUCCESS;
        CashWarrantEntity cashWarrantEntity = cashWarrantMapper.toEntity(cashWarrantDtoRequest);
        cashWarrantEntity.setClientBankAccountEntity(clientBankAccountEntity);
        cashWarrantEntity.setWarrantType(WarrantType.REPLENISHMENT);
        cashWarrantEntity.setExecutionResult(executionResult);
        cashWarrantEntity.setCreatedDate(Instant.now());

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(cashWarrantDtoRequest.getAmount());
        transactionEntity.setBeneficiaryClientAccount(clientBankAccountEntity);
        transactionEntity.setTransactionType(TransactionType.REPLENISHMENT);
        transactionEntity.setCreatedDate(Instant.now());
        transactionEntity.setExecutionResult(executionResult);
        transactionEntity.setCashWarrantEntity(cashWarrantEntity);

        if (!isNotValidityExpired(clientBankAccountEntity.getValidity())) {
            saveResourcesInDataBase(FAILED_VALIDITY_EXPIRED, transactionEntity, cashWarrantEntity);
            throw new ValidityExpiredException("Client account with id " + clientBankAccountEntity.getId() + " overdue");
        }
        clientBankAccountEntity.setAmount(clientBankAccountEntity.getAmount().add(cashWarrantDtoRequest.getAmount()));
        cashWarrantRepository.save(cashWarrantEntity);
        transactionRepository.save(transactionEntity);
        return executionResult;

    }


    public ExecutionResult createWithdrawalCashWarrant(CashWarrantDtoRequest cashWarrantDtoRequest) {
        ClientBankAccountEntity clientBankAccountEntity = clientBankAccountRepository
                .findById(cashWarrantDtoRequest.getBeneficiaryClientAccount())
                .orElseThrow(() -> new ResourceNotFoundException("Client Bank account with id " + cashWarrantDtoRequest.getBeneficiaryClientAccount() + " not found"));

        ExecutionResult executionResult;
        CashWarrantEntity cashWarrantEntity = cashWarrantMapper.toEntity(cashWarrantDtoRequest);
        cashWarrantEntity.setClientBankAccountEntity(clientBankAccountEntity);
        cashWarrantEntity.setWarrantType(WarrantType.WITHDRAWAL);
        cashWarrantEntity.setCreatedDate(Instant.now());

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionType(TransactionType.WITHDRAWAL);
        transactionEntity.setAmount(cashWarrantDtoRequest.getAmount());
        transactionEntity.setCreatedDate(Instant.now());
        transactionEntity.setBeneficiaryClientAccount(clientBankAccountEntity);
        transactionEntity.setCashWarrantEntity(cashWarrantEntity);

        boolean secretKeyMatches = isSecretKeyMatches(cashWarrantDtoRequest.getSecretKey(), clientBankAccountEntity.getClientEntity().getSecretKey());
        boolean isEnoughMoney = clientBankAccountEntity.getAmount().compareTo(cashWarrantDtoRequest.getAmount()) > 0;

        //TODO логгирование

        if (!secretKeyMatches) {
            saveResourcesInDataBase(FAILED_WRONG_SECRET_KEY, transactionEntity, cashWarrantEntity);
            throw new WrongSecretKeyException("Invalid secret key");
        }

        if (!isNotValidityExpired(clientBankAccountEntity.getValidity())) {
            saveResourcesInDataBase(FAILED_VALIDITY_EXPIRED, transactionEntity, cashWarrantEntity);
            throw new ValidityExpiredException("Client account with id " + clientBankAccountEntity.getId() + " overdue");
        }

        if (!isEnoughMoney) {
            saveResourcesInDataBase(FAILED_NOT_ENOUGH_MONEY, transactionEntity, cashWarrantEntity);
            throw new NotEnoughMoneyException("Not enough money on the account with id " + clientBankAccountEntity.getId());
        }
        clientBankAccountEntity.setAmount(clientBankAccountEntity.getAmount().subtract(cashWarrantDtoRequest.getAmount()));
        executionResult = SUCCESS;
        saveResourcesInDataBase(executionResult, transactionEntity, cashWarrantEntity);
        return executionResult;
    }

    private void saveResourcesInDataBase(ExecutionResult exRes, TransactionEntity transaction, CashWarrantEntity cashWarrant) {
        transaction.setExecutionResult(exRes);
        cashWarrant.setExecutionResult(exRes);
        cashWarrantRepository.save(cashWarrant);
        transactionRepository.save(transaction);
    }

    private boolean isNotValidityExpired(LocalDate validity) {
        return LocalDate.now().isBefore(validity);
    }

    private boolean isSecretKeyMatches(String rawSecretKey, String encodedSecretKey) {
        return encoder.matches(rawSecretKey, encodedSecretKey);
    }
}

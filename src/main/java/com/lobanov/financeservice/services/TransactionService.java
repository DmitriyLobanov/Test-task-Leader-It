package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.responses.TransactionDtoResponse;
import com.lobanov.financeservice.dtos.requests.TransferDtoRequest;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.exceptions.NotEnoughMoneyException;
import com.lobanov.financeservice.exceptions.ResourceNotFoundException;
import com.lobanov.financeservice.exceptions.ValidityExpiredException;
import com.lobanov.financeservice.exceptions.WrongSecretKeyException;
import com.lobanov.financeservice.mappers.TransactionMapper;
import com.lobanov.financeservice.models.ClientBankAccountEntity;
import com.lobanov.financeservice.models.TransactionEntity;
import com.lobanov.financeservice.repositories.ClientBankAccountRepository;
import com.lobanov.financeservice.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.lobanov.financeservice.enums.ExecutionResult.*;
import static com.lobanov.financeservice.enums.TransactionType.TRANSFER;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    private final BCryptPasswordEncoder encoder;

    private final ClientBankAccountRepository clientBankAccountRepository;

    private final TransactionMapper transactionMapper;

    public List<TransactionDtoResponse> findAllTransactionsByClientBankAccount(Long clientBankAccountId) {
        log.info("Finding all transactions by client bank account with id: {}", clientBankAccountId);
        List<TransactionEntity> allTransactionsByClientAccountNumber
                = transactionRepository.findAllTransactionsByClientBankAccount(clientBankAccountId);
        return allTransactionsByClientAccountNumber.stream()
                .map(transactionMapper::toDto).collect(Collectors.toList());
    }

    public ExecutionResult createTransferBetweenClientBankAccounts(TransferDtoRequest transferDtoRequest) {

        ExecutionResult executionResult = null;
        log.info("Finding the beneficiary client's bank account by id:{}", transferDtoRequest.getBeneficiaryClientBankAccountId());
        ClientBankAccountEntity beneficiaryClientBankAccountEntity = clientBankAccountRepository
                .findById(transferDtoRequest.getBeneficiaryClientBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("ClientBankAccount not found"));

        log.info("Finding the sender client's bank account by id:{}", transferDtoRequest.getSenderClientBankAccountId());
        ClientBankAccountEntity senderClientBankAccountEntity = clientBankAccountRepository
                .findById(transferDtoRequest.getSenderClientBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("ClientBankAccount not found"));

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionType(TRANSFER);
        transactionEntity.setAmount(transferDtoRequest.getAmount());
        transactionEntity.setCreatedDate(Instant.now());
        transactionEntity.setBeneficiaryClientAccount(beneficiaryClientBankAccountEntity);
        transactionEntity.setSenderBankAccount(senderClientBankAccountEntity);

        boolean secretKeyMatches = isSecretKeyMatches(transferDtoRequest.getSecretKey(), beneficiaryClientBankAccountEntity.getClientEntity().getSecretKey());
        boolean validThruBeneficiary = validThru(beneficiaryClientBankAccountEntity.getValidity());
        boolean validThruSender = validThru(senderClientBankAccountEntity.getValidity());
        boolean isSenderClientBankAccountHasEnoughMoney = senderClientBankAccountEntity.getAmount().compareTo(transferDtoRequest.getAmount()) > 0;

        if (!secretKeyMatches) {
            executionResult = FAILED_WRONG_SECRET_KEY;
            saveResourcesInDataBase(beneficiaryClientBankAccountEntity, senderClientBankAccountEntity, transactionEntity, executionResult);
            throw new WrongSecretKeyException("Wrong secret key");
        }
        if (!(validThruBeneficiary && validThruSender)) {
            executionResult = FAILED_VALIDITY_EXPIRED;
            saveResourcesInDataBase(beneficiaryClientBankAccountEntity, senderClientBankAccountEntity, transactionEntity, executionResult);
            throw new ValidityExpiredException("Client account is overdue");

        }
        if (!isSenderClientBankAccountHasEnoughMoney) {
            executionResult = FAILED_NOT_ENOUGH_MONEY;
            saveResourcesInDataBase(beneficiaryClientBankAccountEntity, senderClientBankAccountEntity, transactionEntity, executionResult);
            throw new NotEnoughMoneyException("Not enough money on the account with id " + senderClientBankAccountEntity.getId());
        }

        executionResult = SUCCESS;
        beneficiaryClientBankAccountEntity.setAmount(beneficiaryClientBankAccountEntity.getAmount().add(transferDtoRequest.getAmount()));
        senderClientBankAccountEntity.setAmount(senderClientBankAccountEntity.getAmount().subtract(transferDtoRequest.getAmount()));
        saveResourcesInDataBase(beneficiaryClientBankAccountEntity, senderClientBankAccountEntity, transactionEntity, executionResult);
        return executionResult;
    }

    private void saveResourcesInDataBase(ClientBankAccountEntity beneficiaryClientBankAccountEntity,
                                         ClientBankAccountEntity senderClientBankAccountEntity,
                                         TransactionEntity transactionEntity,
                                         ExecutionResult executionResult) {
        transactionEntity.setExecutionResult(executionResult);
        clientBankAccountRepository.save(beneficiaryClientBankAccountEntity);
        clientBankAccountRepository.save(senderClientBankAccountEntity);
        transactionRepository.save(transactionEntity);
    }

    private boolean validThru(LocalDate validity) {
        return LocalDate.now().isBefore(validity);
    }

    public boolean isSecretKeyMatches(String rawSecretKey, String encodedSecretKey) {
        return encoder.matches(rawSecretKey, encodedSecretKey);
    }

}

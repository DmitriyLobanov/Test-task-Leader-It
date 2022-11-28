package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.TransactionDto;
import com.lobanov.financeservice.dtos.TransferDto;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.TransactionType;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.lobanov.financeservice.enums.ExecutionResult.*;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    private final BCryptPasswordEncoder encoder;

    private final ClientBankAccountRepository clientBankAccountRepository;
    private final TransactionMapper transactionMapper;

    public List<TransactionDto> findAllTransactionsByClientBankAccount(Long clientBankAccountId) {
        List<TransactionEntity> allTransactionsByClientAccountNumber
                = transactionRepository.findAllTransactionsByClientBankAccount(clientBankAccountId);
        return allTransactionsByClientAccountNumber.stream()
                .map(transactionMapper::toDto).collect(Collectors.toList());
    }

    public ExecutionResult createTransferBetweenClientBankAccounts(TransferDto transferDto) {

        ExecutionResult executionResult = null;
        ClientBankAccountEntity beneficiaryClientBankAccountEntity = clientBankAccountRepository
                .findById(transferDto.getBeneficiaryClientBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("ClientBankAccount not found"));

        ClientBankAccountEntity senderClientBankAccountEntity = clientBankAccountRepository
                .findById(transferDto.getSenderClientBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("ClientBankAccount not found"));

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionType(TransactionType.TRANSFER);
        transactionEntity.setAmount(transferDto.getAmount());
        transactionEntity.setCreatedDate(Instant.now());
        transactionEntity.setBeneficiaryClientAccount(beneficiaryClientBankAccountEntity);
        transactionEntity.setSenderBankAccount(senderClientBankAccountEntity);

        boolean secretKeyMatches = isSecretKeyMatches(transferDto.getSecretKey(), beneficiaryClientBankAccountEntity.getClientEntity().getSecretKey());
        boolean validThruBeneficiary = validThru(beneficiaryClientBankAccountEntity.getValidity());
        boolean validThruSender = validThru(senderClientBankAccountEntity.getValidity());
        boolean isSenderClientBankAccountHasEnoughMoney = senderClientBankAccountEntity.getAmount().compareTo(transferDto.getAmount()) > 0;

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
        beneficiaryClientBankAccountEntity.setAmount(beneficiaryClientBankAccountEntity.getAmount().add(transferDto.getAmount()));
        senderClientBankAccountEntity.setAmount(senderClientBankAccountEntity.getAmount().subtract(transferDto.getAmount()));
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

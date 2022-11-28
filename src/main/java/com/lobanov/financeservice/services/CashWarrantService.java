package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.CashWarrantDto;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.TransactionType;
import com.lobanov.financeservice.enums.WarrantType;
import com.lobanov.financeservice.exceptions.ResourceNotFoundException;
import com.lobanov.financeservice.mappers.CashWarrantMapper;
import com.lobanov.financeservice.models.CashWarrant;
import com.lobanov.financeservice.models.ClientBankAccount;
import com.lobanov.financeservice.models.Transaction;
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

    public List<CashWarrantDto> findAllCashWarrantsByClientBankAccountId(Long clientBankAccountId) {
        return cashWarrantRepository.findCashWarrantsByClientBankAccountId(clientBankAccountId).stream()
                .map(cashWarrantMapper::toDto).collect(Collectors.toList());
    }



    private boolean validThru(LocalDate validity) {
        return LocalDate.now().isBefore(validity);
    }

    public boolean isSecretKeyMatches(String rawSecretKey, String encodedSecretKey) {
        return encoder.matches(rawSecretKey, encodedSecretKey);
    }

    public ExecutionResult createReplenishmentCashWarrant(CashWarrantDto cashWarrantDto) {
        ExecutionResult executionResult;
        // ТУТ ИЛИ ЗАПРОС ЧЕРЕЗ ДЖОИНЫ ИДИ EAGER делать
        ClientBankAccount clientBankAccount = clientBankAccountRepository
                .findById(cashWarrantDto.getClientBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Client Bank account with id " + cashWarrantDto.getClientBankAccountId() + " not found"));

        if (!validThru(clientBankAccount.getValidity()))  {
            executionResult = FAILED_VALIDITY_EXPIRED;
        } else {
            clientBankAccount.setAmount(clientBankAccount.getAmount().add(cashWarrantDto.getAmount()));
            executionResult = SUCCESS;
        }

        CashWarrant cashWarrant = cashWarrantMapper.toEntity(cashWarrantDto);
        cashWarrant.setClientBankAccount(clientBankAccount);
        cashWarrant.setWarrantType(WarrantType.REPLENISHMENT);
        cashWarrant.setExecutionResult(executionResult);
        cashWarrant.setCreatedDate(Instant.now());

        Transaction transaction = new Transaction();
        transaction.setAmount(cashWarrantDto.getAmount());
        transaction.setBeneficiaryClientAccount(clientBankAccount);
        transaction.setTransactionType(TransactionType.REPLENISHMENT);
        transaction.setCreatedDate(Instant.now());
        transaction.setExecutionResult(executionResult);
        transaction.setCashWarrant(cashWarrant);

        cashWarrantRepository.save(cashWarrant);
        transactionRepository.save(transaction);
        return  executionResult;

    }

    public ExecutionResult createWithdrawalCashWarrant(CashWarrantDto cashWarrantDto) {
        // ТУТ ИЛИ ЗАПРОС ЧЕРЕЗ ДЖОИНЫ ИДИ EAGER делать
        ClientBankAccount clientBankAccount = clientBankAccountRepository
                .findById(cashWarrantDto.getClientBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Client Bank account with id " + cashWarrantDto.getClientBankAccountId() + " not found"));

        ExecutionResult executionResult;
        CashWarrant cashWarrant = cashWarrantMapper.toEntity(cashWarrantDto);
        cashWarrant.setClientBankAccount(clientBankAccount);
        cashWarrant.setWarrantType(WarrantType.WITHDRAWAL);
        cashWarrant.setCreatedDate(Instant.now());

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(cashWarrantDto.getAmount());
        transaction.setCreatedDate(Instant.now());
        transaction.setBeneficiaryClientAccount(clientBankAccount);

        boolean secretKeyMatches = isSecretKeyMatches(cashWarrantDto.getSecretKey(), clientBankAccount.getClient().getSecretKey());
        boolean isNotEnoughMoney = clientBankAccount.getAmount().compareTo(cashWarrantDto.getAmount()) < 0;

        if (!secretKeyMatches) {
            executionResult = FAILED_WRONG_SECRET_KEY;
            transaction.setExecutionResult(executionResult);//secretKeyFailed
            cashWarrant.setExecutionResult(executionResult);
        } else {
            if (isNotEnoughMoney) {
                executionResult = FAILED_NOT_ENOUGH_MONEY;
                transaction.setExecutionResult(executionResult);// недостаточно денег
                cashWarrant.setExecutionResult(executionResult);
            } else {
                clientBankAccount.setAmount(clientBankAccount.getAmount().subtract(cashWarrantDto.getAmount()));
                executionResult = SUCCESS;
                cashWarrant.setExecutionResult(executionResult);
                transaction.setExecutionResult(executionResult);
            }
        }
        cashWarrantRepository.save(cashWarrant);
        transactionRepository.save(transaction);
        return  executionResult;
    }
}

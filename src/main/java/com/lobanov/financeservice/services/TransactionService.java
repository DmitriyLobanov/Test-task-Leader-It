package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.TransactionDto;
import com.lobanov.financeservice.dtos.TransferDto;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.TransactionType;
import com.lobanov.financeservice.exceptions.ResourceNotFoundException;
import com.lobanov.financeservice.mappers.TransactionMapper;
import com.lobanov.financeservice.models.ClientBankAccount;
import com.lobanov.financeservice.models.Transaction;
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

    //@TODO ПО АЙДИ
    public List<TransactionDto> findAllTransactionsByClientBankAccount(Long clientBankAccountId) {
        List<Transaction> allTransactionsByClientAccountNumber
                = transactionRepository.findAllTransactionsByClientBankAccount(clientBankAccountId);
        return allTransactionsByClientAccountNumber.stream()
                .map(transactionMapper::toDto).collect(Collectors.toList());
    }

    private boolean validThru(LocalDate validity) {
        return LocalDate.now().isBefore(validity);
    }

    public boolean isSecretKeyMatches(String rawSecretKey, String encodedSecretKey) {
        return encoder.matches(rawSecretKey, encodedSecretKey);
    }

    public ExecutionResult createTransferBetweenClientBankAccounts(TransferDto transferDto) {
        ExecutionResult executionResult = null;
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(transferDto.getAmount());
        transaction.setCreatedDate(Instant.now());

        ClientBankAccount beneficiaryClientBankAccount = clientBankAccountRepository
                .findById(transferDto.getBeneficiaryClientBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("ClientBankAccount not found"));

        ClientBankAccount senderClientBankAccount = clientBankAccountRepository
                .findById(transferDto.getSenderClientBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("ClientBankAccount not found"));

        transaction.setBeneficiaryClientAccount(beneficiaryClientBankAccount);
        transaction.setSenderBankAccount(senderClientBankAccount);

        boolean secretKeyMatches = isSecretKeyMatches(transferDto.getSecretKey(), beneficiaryClientBankAccount.getClient().getSecretKey());
        boolean validThruBeneficiary = validThru(beneficiaryClientBankAccount.getValidity());
        boolean validThruSender = validThru(senderClientBankAccount.getValidity());
        boolean isSenderClientBankAccountHasMoney = senderClientBankAccount.getAmount().compareTo(transferDto.getAmount()) > 0;

        if (!isSenderClientBankAccountHasMoney) {
            executionResult = FAILED_NOT_ENOUGH_MONEY;
        }

        if (!secretKeyMatches) {
            executionResult = FAILED_WRONG_SECRET_KEY;

        }
        if (!(validThruBeneficiary && validThruSender)) {
            executionResult = FAILED_VALIDITY_EXPIRED;
        }


        if (secretKeyMatches && validThruBeneficiary && validThruSender && isSenderClientBankAccountHasMoney) {
            executionResult = SUCCESS;
            beneficiaryClientBankAccount.setAmount(beneficiaryClientBankAccount.getAmount().add(transferDto.getAmount()));
            senderClientBankAccount.setAmount(senderClientBankAccount.getAmount().subtract(transferDto.getAmount()));
        }

        transaction.setExecutionResult(executionResult);
        clientBankAccountRepository.save(beneficiaryClientBankAccount);
        clientBankAccountRepository.save(senderClientBankAccount);
        transactionRepository.save(transaction);
        return executionResult;
    }

    public ExecutionResult createTransferBetweenDiffClientBankAccounts(TransferDto transferDto) {
        return null;
    }
}

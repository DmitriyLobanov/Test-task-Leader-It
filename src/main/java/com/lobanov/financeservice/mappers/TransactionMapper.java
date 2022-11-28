package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.TransactionDto;
import com.lobanov.financeservice.models.Transaction;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TransactionMapper {

    public TransactionDto toDto(Transaction entity) {
        Long cashWarrantId = (entity.getCashWarrant()) != null ? entity.getCashWarrant().getId() : null;
        Long senderBankAccountId = (entity.getSenderBankAccount()) != null ? entity.getSenderBankAccount().getId() : null;
        return TransactionDto.builder()
                .id(entity.getId())
                .createdDate(entity.getCreatedDate())
                .amount(entity.getAmount())
                .transactionType(entity.getTransactionType())
                .cashWarrantId(cashWarrantId)
                .beneficiaryClientBankAccountId(entity.getBeneficiaryClientAccount().getId())
                .senderBankAccountId(senderBankAccountId)
                .executionResult(entity.getExecutionResult())
                .build();
    }
}

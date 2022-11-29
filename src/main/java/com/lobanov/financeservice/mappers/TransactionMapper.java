package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.responses.TransactionDtoResponse;
import com.lobanov.financeservice.models.TransactionEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TransactionMapper {

    public TransactionDtoResponse toDto(TransactionEntity entity) {
        Long cashWarrantId = (entity.getCashWarrantEntity()) != null ? entity.getCashWarrantEntity().getId() : null;
        Long senderBankAccountId = (entity.getSenderBankAccount()) != null ? entity.getSenderBankAccount().getId() : null;
        return TransactionDtoResponse.builder()
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

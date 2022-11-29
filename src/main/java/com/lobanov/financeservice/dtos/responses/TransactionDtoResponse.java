package com.lobanov.financeservice.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDtoResponse {
    private Long id;
    private Instant createdDate;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Long beneficiaryClientBankAccountId;
    private Long cashWarrantId;
    private Long senderBankAccountId;
    private ExecutionResult executionResult;
}

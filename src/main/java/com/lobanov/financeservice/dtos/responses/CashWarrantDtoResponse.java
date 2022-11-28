package com.lobanov.financeservice.dtos.responses;

import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.WarrantType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CashWarrantDtoResponse {
    private Long id;
    private WarrantType warrantType;
    private BigDecimal amount;
    private Long clientBankAccountId;
    private ExecutionResult executionResult;
    private Instant createdDate;
}

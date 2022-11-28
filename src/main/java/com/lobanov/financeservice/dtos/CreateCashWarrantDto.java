package com.lobanov.financeservice.dtos;

import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.WarrantType;
import com.lobanov.financeservice.models.ClientBankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCashWarrantDto {
    private Long id;
    private WarrantType warrantType;
    private BigDecimal amount;
    private ClientBankAccount clientBankAccount;
    private ExecutionResult executionResult;
    private Instant createdDate ;
}
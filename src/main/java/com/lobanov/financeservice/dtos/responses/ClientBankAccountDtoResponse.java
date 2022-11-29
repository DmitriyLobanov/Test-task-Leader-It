package com.lobanov.financeservice.dtos.responses;

import com.lobanov.financeservice.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientBankAccountDtoResponse {
    private Long id;
    private Long accountNumber;
    private BigDecimal amount;
    private AccountType accountType;
    private Instant createdDate;
    private LocalDate validity;
    private Long clientId;
}

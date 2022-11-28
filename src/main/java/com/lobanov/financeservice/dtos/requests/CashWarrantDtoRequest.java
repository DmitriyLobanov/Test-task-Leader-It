package com.lobanov.financeservice.dtos.requests;

import com.lobanov.financeservice.enums.WarrantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashWarrantDtoRequest {
    private Long id;
    private String secretKey;
    private WarrantType warrantType;
    private BigDecimal amount;
    private Long beneficiaryClientAccount;
}

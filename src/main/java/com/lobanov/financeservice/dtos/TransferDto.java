package com.lobanov.financeservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//request
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    private Long beneficiaryClientBankAccountId;
    private Long senderClientBankAccountId;
    private BigDecimal amount;
    private String secretKey;
}

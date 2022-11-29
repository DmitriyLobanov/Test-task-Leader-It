package com.lobanov.financeservice.dtos.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//request
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDtoRequest {
    private Long beneficiaryClientBankAccountId;
    private Long senderClientBankAccountId;
    private BigDecimal amount;
    private String secretKey;
}

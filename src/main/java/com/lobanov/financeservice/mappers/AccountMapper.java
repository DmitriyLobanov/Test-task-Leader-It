package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.ClientBankAccountDto;
import com.lobanov.financeservice.models.ClientBankAccount;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AccountMapper {

    public ClientBankAccountDto toDto(ClientBankAccount entity) {
        return ClientBankAccountDto.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .amount(entity.getAmount())
                .accountType(entity.getAccountType())
                .createdDate(entity.getCreatedDate())
                .validity(entity.getValidity())
                .clientId(entity.getClient().getId())
                .build();
    }
}

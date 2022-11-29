package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.responses.ClientBankAccountDtoResponse;
import com.lobanov.financeservice.models.ClientBankAccountEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AccountMapper {

    public ClientBankAccountDtoResponse toDto(ClientBankAccountEntity entity) {
        return ClientBankAccountDtoResponse.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .amount(entity.getAmount())
                .accountType(entity.getAccountType())
                .createdDate(entity.getCreatedDate())
                .validity(entity.getValidity())
                .clientId(entity.getClientEntity().getId())
                .build();
    }
}

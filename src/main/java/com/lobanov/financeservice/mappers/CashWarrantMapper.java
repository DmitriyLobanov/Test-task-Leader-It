package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.CashWarrantDto;
import com.lobanov.financeservice.models.CashWarrant;
import org.springframework.stereotype.Component;

@Component
public class CashWarrantMapper {

    public CashWarrantDto toDto(CashWarrant entity) {
        return CashWarrantDto.builder()
                .id(entity.getId())
                .warrantType(entity.getWarrantType())
                .amount(entity.getAmount())
                .clientBankAccountId(entity.getClientBankAccount().getId())
                .build();
    }

    public CashWarrant toEntity(CashWarrantDto dto) {
        return CashWarrant.builder()
                .id(dto.getId())
                .warrantType(dto.getWarrantType())
                .clientBankAccount(null)
                .amount(dto.getAmount())
                .build();

    }
}

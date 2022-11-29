package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.requests.CashWarrantDtoRequest;
import com.lobanov.financeservice.dtos.responses.CashWarrantDtoResponse;
import com.lobanov.financeservice.models.CashWarrantEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CashWarrantMapper {

    public CashWarrantDtoResponse toDto(CashWarrantEntity entity) {
        return CashWarrantDtoResponse.builder()
                .id(entity.getId())
                .warrantType(entity.getWarrantType())
                .amount(entity.getAmount())
                .executionResult(entity.getExecutionResult())
                .createdDate(entity.getCreatedDate())
                .clientBankAccountId(entity.getClientBankAccountEntity().getId())
                .build();
    }

    public CashWarrantEntity toEntity(CashWarrantDtoRequest dto) {
        return CashWarrantEntity.builder()
                .id(dto.getId())
                .warrantType(dto.getWarrantType())
                .clientBankAccountEntity(null)
                .amount(dto.getAmount())
                .build();

    }
}

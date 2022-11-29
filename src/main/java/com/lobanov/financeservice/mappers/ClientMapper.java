package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.responses.ClientDtoResponse;
import com.lobanov.financeservice.models.ClientEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ClientMapper {

    public ClientDtoResponse toDto(ClientEntity entity) {
        return ClientDtoResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstname())
                .secondName(entity.getSecondname())
                .surname(entity.getSurname())
                .build();
    }
}

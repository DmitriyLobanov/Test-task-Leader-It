package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.ClientDto;
import com.lobanov.financeservice.dtos.CreateClientDto;
import com.lobanov.financeservice.models.ClientEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ClientMapper {

    public ClientDto toDto(ClientEntity entity) {
        return ClientDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstname())
                .secondName(entity.getSecondname())
                .surname(entity.getSurname())
                .build();
    }

    public ClientEntity toEntity(CreateClientDto createClientDto) {
        return ClientEntity.builder()
                .firstname(createClientDto.getFirstName())
                .secondname(createClientDto.getSecondName())
                .surname(createClientDto.getSurname())
                .secretKey(createClientDto.getSecretKey())
                .build();
    }
}

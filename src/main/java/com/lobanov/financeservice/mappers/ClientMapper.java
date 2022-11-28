package com.lobanov.financeservice.mappers;

import com.lobanov.financeservice.dtos.ClientDto;
import com.lobanov.financeservice.dtos.CreateClientDto;
import com.lobanov.financeservice.models.Client;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ClientMapper {

    public ClientDto toDto(Client entity) {
        return ClientDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstname())
                .secondName(entity.getSecondname())
                .surname(entity.getSurname())
                .build();
    }

    public Client toEntity(CreateClientDto createClientDto) {
        return Client.builder()
                .firstname(createClientDto.getFirstName())
                .secondname(createClientDto.getSecondName())
                .surname(createClientDto.getSurname())
                .secretKey(createClientDto.getSecretKey())
                .build();
    }
}

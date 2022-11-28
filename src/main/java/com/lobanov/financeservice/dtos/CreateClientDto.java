package com.lobanov.financeservice.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateClientDto {
    private Long id;

    private String firstName;

    private String secondName;

    private String surname;

    private String secretKey;
}

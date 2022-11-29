package com.lobanov.financeservice.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDtoResponse {

    private Long id;

    private String firstName;

    private String secondName;

    private String surname;

}

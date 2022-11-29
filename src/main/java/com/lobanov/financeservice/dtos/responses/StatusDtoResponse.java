package com.lobanov.financeservice.dtos.responses;

import com.lobanov.financeservice.enums.ExecutionResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDtoResponse {
    private ExecutionResult executionResult;
}

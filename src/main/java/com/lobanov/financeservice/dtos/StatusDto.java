package com.lobanov.financeservice.dtos;

import com.lobanov.financeservice.enums.ExecutionResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {
    private ExecutionResult executionResult;
}

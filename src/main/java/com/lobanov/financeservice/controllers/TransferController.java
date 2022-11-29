package com.lobanov.financeservice.controllers;

import com.lobanov.financeservice.dtos.responses.StatusDtoResponse;
import com.lobanov.financeservice.dtos.requests.TransferDtoRequest;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfer")
public class TransferController {

    private final TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<StatusDtoResponse> createTransferBetweenClientBankAccounts(@RequestBody TransferDtoRequest transferDtoRequest) {
        ExecutionResult executionResult = transactionService.createTransferBetweenClientBankAccounts(transferDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new StatusDtoResponse(executionResult));
    }
}

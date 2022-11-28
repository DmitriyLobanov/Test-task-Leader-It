package com.lobanov.financeservice.controllers;

import com.lobanov.financeservice.dtos.StatusDto;
import com.lobanov.financeservice.dtos.TransferDto;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<StatusDto> createTransferBetweenClientBankAccounts(@RequestBody TransferDto transferDto) {
        ExecutionResult executionResult = transactionService.createTransferBetweenClientBankAccounts(transferDto);
        return ResponseEntity.ok(new StatusDto(executionResult));
    }

//    @PostMapping("/between/different")
//    public ResponseEntity<ExecutionResult> createTransferBetweenDiffClientBankAccounts(@RequestBody TransferDto transferDto) {
//        ExecutionResult executionResult = transactionService.createTransferBetweenDiffClientBankAccounts(transferDto);
//        return ResponseEntity.ok(executionResult);
//    }

}

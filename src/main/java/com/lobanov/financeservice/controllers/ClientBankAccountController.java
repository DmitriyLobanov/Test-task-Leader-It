package com.lobanov.financeservice.controllers;

import com.lobanov.financeservice.dtos.CashWarrantDto;
import com.lobanov.financeservice.dtos.ClientBankAccountDto;
import com.lobanov.financeservice.dtos.TransactionDto;
import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.WarrantType;
import com.lobanov.financeservice.services.CashWarrantService;
import com.lobanov.financeservice.services.ClientBankAccountService;
import com.lobanov.financeservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank_account")
public class ClientBankAccountController {
    private final ClientBankAccountService clientBankAccountService;

    private final CashWarrantService cashWarrantService;

    private final TransactionService transactionService;

    @GetMapping("/{clientId}")
    public ResponseEntity<List<ClientBankAccountDto>> getClientBankAccountsByClientId(@PathVariable(name = "clientId") Long clientId) {
        List<ClientBankAccountDto> clientAccountsByClientId = clientBankAccountService.getClientAccountsByClientId(clientId);
        return ResponseEntity.ok(clientAccountsByClientId);
    }
    @GetMapping("/{clientBankAccountId}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactionsByClientBankAccountId(@PathVariable Long clientBankAccountId) {
        List<TransactionDto> allTransactionsByClientBankAccount
                = transactionService.findAllTransactionsByClientBankAccount(clientBankAccountId);
        return ResponseEntity.ok(allTransactionsByClientBankAccount);
    }
    @GetMapping("/{clientBankAccountId}/warrants")
    public ResponseEntity<List<CashWarrantDto>> getAllCashWarrantsByClientBankAccountId(
            @PathVariable(name = "clientBankAccountId") Long clientBankAccountId) {
        List<CashWarrantDto> allCashWarrantsByClientBankAccountId
                = cashWarrantService.findAllCashWarrantsByClientBankAccountId(clientBankAccountId);
        return ResponseEntity.ok(allCashWarrantsByClientBankAccountId);
    }

    @PostMapping("/warrant")
    public  ResponseEntity<ExecutionResult> createCashWarrant(@RequestBody CashWarrantDto cashWarrantDto) {
        ExecutionResult executionResult = null;
        if (cashWarrantDto.getWarrantType().equals(WarrantType.REPLENISHMENT))
             executionResult = cashWarrantService.createReplenishmentCashWarrant(cashWarrantDto);

        if (cashWarrantDto.getWarrantType().equals(WarrantType.WITHDRAWAL))
            executionResult = cashWarrantService.createWithdrawalCashWarrant(cashWarrantDto);

        return ResponseEntity.status(HttpStatus.OK).body(executionResult);
    }

}

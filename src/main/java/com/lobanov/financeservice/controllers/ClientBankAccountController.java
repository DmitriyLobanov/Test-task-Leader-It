package com.lobanov.financeservice.controllers;

import com.lobanov.financeservice.dtos.requests.CashWarrantDtoRequest;
import com.lobanov.financeservice.dtos.responses.ClientBankAccountDtoResponse;
import com.lobanov.financeservice.dtos.responses.StatusDtoResponse;
import com.lobanov.financeservice.dtos.responses.TransactionDtoResponse;
import com.lobanov.financeservice.dtos.responses.CashWarrantDtoResponse;
import com.lobanov.financeservice.enums.ExecutionResult;
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
@RequestMapping("/api/v1/bank-account")
public class ClientBankAccountController {
    private final ClientBankAccountService clientBankAccountService;

    private final CashWarrantService cashWarrantService;

    private final TransactionService transactionService;

    @GetMapping("/{clientId}")
    public ResponseEntity<List<ClientBankAccountDtoResponse>> getClientBankAccountsByClientId(@PathVariable(name = "clientId") Long clientId) {
        List<ClientBankAccountDtoResponse> clientAccountsByClientId = clientBankAccountService.getClientAccountsByClientId(clientId);
        return ResponseEntity.ok(clientAccountsByClientId);
    }

    @GetMapping("/{clientBankAccountId}/transactions")
    public ResponseEntity<List<TransactionDtoResponse>> getTransactionsByClientBankAccountId(@PathVariable Long clientBankAccountId) {
        List<TransactionDtoResponse> allTransactionsByClientBankAccount
                = transactionService.findAllTransactionsByClientBankAccount(clientBankAccountId);
        return ResponseEntity.ok(allTransactionsByClientBankAccount);
    }

    @GetMapping("/{clientBankAccountId}/warrants")
    public ResponseEntity<List<CashWarrantDtoResponse>> getAllCashWarrantsByClientBankAccountId(
            @PathVariable(name = "clientBankAccountId") Long clientBankAccountId) {
        List<CashWarrantDtoResponse> allCashWarrantsByClientBankAccountId
                = cashWarrantService.findAllCashWarrantsByClientBankAccountId(clientBankAccountId);
        return ResponseEntity.ok(allCashWarrantsByClientBankAccountId);
    }

    @PostMapping("/warrant")
    public ResponseEntity<StatusDtoResponse> createCashWarrant(@RequestBody CashWarrantDtoRequest cashWarrantDtoRequest) {
        ExecutionResult executionResult = null;

        executionResult = cashWarrantService.createCashWarrant(cashWarrantDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new StatusDtoResponse(executionResult));
    }

}

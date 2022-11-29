package com.lobanov.financeservice.controllers;

import com.lobanov.financeservice.dtos.responses.ClientDtoResponse;
import com.lobanov.financeservice.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDtoResponse>> getAllClients() {
        List<ClientDtoResponse> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDtoResponse> getClientById(@PathVariable(name = "clientId") Long clientId) {
        ClientDtoResponse clientDtoResponse = clientService.getClientById(clientId);
        return ResponseEntity.ok(clientDtoResponse);
    }
}

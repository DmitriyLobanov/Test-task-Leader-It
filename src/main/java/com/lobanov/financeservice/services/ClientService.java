package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.responses.ClientDtoResponse;
import com.lobanov.financeservice.exceptions.ResourceNotFoundException;
import com.lobanov.financeservice.mappers.ClientMapper;
import com.lobanov.financeservice.models.ClientEntity;
import com.lobanov.financeservice.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public List<ClientDtoResponse> getAllClients() {
        log.info("Finding all clients");
        List<ClientEntity> clientEntities = clientRepository.findAll();
        return clientEntities.stream().map(clientMapper::toDto).collect(Collectors.toList());
    }

    public ClientDtoResponse getClientById(Long clientId) {
        log.info("Finding  client with id: {}", clientId);
        ClientEntity clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + clientId + " not found."));
        return clientMapper.toDto(clientEntity);
    }
}

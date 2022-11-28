package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.ClientDto;
import com.lobanov.financeservice.dtos.CreateClientDto;
import com.lobanov.financeservice.exceptions.ResourceNotFoundException;
import com.lobanov.financeservice.mappers.ClientMapper;
import com.lobanov.financeservice.models.ClientEntity;
import com.lobanov.financeservice.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public List<ClientDto> getAllClients() {
        List<ClientEntity> clientEntities = clientRepository.findAll();
        return clientEntities.stream().map(clientMapper::toDto).collect(Collectors.toList());
    }

    public ClientDto getClientById(Long clientId) {
        ClientEntity clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + clientId + " not found."));
        return clientMapper.toDto(clientEntity);
    }
}

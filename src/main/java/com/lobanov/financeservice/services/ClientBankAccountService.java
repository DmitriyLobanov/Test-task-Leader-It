package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.responses.ClientBankAccountDtoResponse;
import com.lobanov.financeservice.mappers.AccountMapper;
import com.lobanov.financeservice.models.ClientBankAccountEntity;
import com.lobanov.financeservice.repositories.ClientBankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientBankAccountService {
    private final ClientBankAccountRepository clientBankAccountRepository;

    private final AccountMapper accountMapper;

    public List<ClientBankAccountDtoResponse> getClientAccountsByClientId(Long clientId) {
        log.info("Finding client bank accounts with client id: {}", clientId);
        List<ClientBankAccountEntity> clientBankAccountEntityDtoList = clientBankAccountRepository.findAllByClientId(clientId);
        return clientBankAccountEntityDtoList.stream().map(accountMapper::toDto).collect(Collectors.toList());
    }
}

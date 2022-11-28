package com.lobanov.financeservice.services;

import com.lobanov.financeservice.dtos.ClientBankAccountDto;
import com.lobanov.financeservice.mappers.AccountMapper;
import com.lobanov.financeservice.models.ClientBankAccount;
import com.lobanov.financeservice.repositories.ClientBankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientBankAccountService {
    private final ClientBankAccountRepository clientBankAccountRepository;

    private final AccountMapper accountMapper;


    public List<ClientBankAccountDto> getClientAccountsByClientId(Long clientId) {
        List<ClientBankAccount> clientBankAccountDtoList = clientBankAccountRepository.findAllByClientId(clientId);
        return clientBankAccountDtoList.stream().map(accountMapper::toDto).collect(Collectors.toList());
    }


}

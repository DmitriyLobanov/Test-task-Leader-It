package com.lobanov.financeservice.repositories;

import com.lobanov.financeservice.models.ClientBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientBankAccountRepository extends JpaRepository<ClientBankAccount, Long> {

    @Query("select CBA from ClientBankAccount CBA  where CBA.client.id = :id")
    List<ClientBankAccount> findAllByClientId(Long id);

}

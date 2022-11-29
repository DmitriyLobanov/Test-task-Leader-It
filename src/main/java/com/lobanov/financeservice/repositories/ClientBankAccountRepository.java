package com.lobanov.financeservice.repositories;

import com.lobanov.financeservice.models.ClientBankAccountEntity;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientBankAccountRepository extends JpaRepository<ClientBankAccountEntity, Long> {

    @Override
    @Query("select cbae from ClientBankAccountEntity cbae join ClientEntity ce on cbae.clientEntity.id = ce.id where cbae.id =:clientBankAccountId")
    Optional<ClientBankAccountEntity> findById(Long clientBankAccountId);

    @Query("select cbae from ClientBankAccountEntity cbae  join ClientEntity ce on cbae.clientEntity.id = ce.id where cbae.clientEntity.id =:clientId")
    List<ClientBankAccountEntity> findAllByClientId(Long clientId);

}
package com.lobanov.financeservice.repositories;

import com.lobanov.financeservice.models.CashWarrantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashWarrantRepository extends JpaRepository<CashWarrantEntity, Long> {

    @Query("select cwe from CashWarrantEntity cwe where  cwe.clientBankAccountEntity.id =:clientBankAccountId")
    List<CashWarrantEntity> findCashWarrantsByClientBankAccountId(Long clientBankAccountId);
}

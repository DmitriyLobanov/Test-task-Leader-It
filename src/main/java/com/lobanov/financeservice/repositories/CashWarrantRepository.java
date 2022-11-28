package com.lobanov.financeservice.repositories;

import com.lobanov.financeservice.models.CashWarrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashWarrantRepository extends JpaRepository<CashWarrant, Long> {

    @Query("select CW from CashWarrant CW where  CW.clientBankAccount.id =:clientBankAccountId")
    List<CashWarrant> findCashWarrantsByClientBankAccountId(Long clientBankAccountId);
}

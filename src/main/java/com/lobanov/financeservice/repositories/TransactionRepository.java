package com.lobanov.financeservice.repositories;

import com.lobanov.financeservice.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select T from Transaction T where T.beneficiaryClientAccount.id =:clientBankAccountId")
    List<Transaction> findAllTransactionsByClientBankAccount(Long clientBankAccountId);
}
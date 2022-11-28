package com.lobanov.financeservice.repositories;

import com.lobanov.financeservice.models.TransactionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @EntityGraph(attributePaths = {"senderBankAccount", "beneficiaryClientAccount", "cashWarrantEntity" })
    @Query("select te from TransactionEntity te where te.beneficiaryClientAccount.id =:clientBankAccountId")
    List<TransactionEntity> findAllTransactionsByClientBankAccount(Long clientBankAccountId);
}
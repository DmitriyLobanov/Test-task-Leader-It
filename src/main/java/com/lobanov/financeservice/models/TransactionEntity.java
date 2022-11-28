package com.lobanov.financeservice.models;

import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name ="amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "beneficiary_bank_account_id", nullable = false)
    private ClientBankAccountEntity beneficiaryClientAccount;

    @ManyToOne
    @JoinColumn(name = "cash_warrant_id")
    private CashWarrantEntity cashWarrantEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_bank_account_id")
    private ClientBankAccountEntity senderBankAccount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "execution_result", nullable = false)
    private ExecutionResult executionResult;
}

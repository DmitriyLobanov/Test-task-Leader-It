package com.lobanov.financeservice.models;

import com.lobanov.financeservice.enums.ExecutionResult;
import com.lobanov.financeservice.enums.WarrantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cash_warrants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashWarrantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "warrant_type", nullable = false)
    private WarrantType warrantType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private ClientBankAccountEntity clientBankAccountEntity;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "execution_result", nullable = false)
    private ExecutionResult executionResult;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;
}

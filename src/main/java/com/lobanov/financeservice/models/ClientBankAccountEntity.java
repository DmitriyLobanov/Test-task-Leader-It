package com.lobanov.financeservice.models;

import com.lobanov.financeservice.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
//TODO ИЗМЕНИТЬ НАЗВАНИЕ ТАБЛИТЦЫ
@Table(name = "client_bank_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientBankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "created_date",nullable = false)
    private Instant createdDate;

    @Column(name = "validity", nullable = false)
    private LocalDate validity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity clientEntity;
}

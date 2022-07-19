package com.paymybuddy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User debtor;

    @ManyToOne
    private User creditor;

    @NotNull
    private String description;

    @NotNull
    private double amount;

    @NotNull
    private double percentOfFreshTransaction;

    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private LocalDate date;

    public Transaction() {
    }
}

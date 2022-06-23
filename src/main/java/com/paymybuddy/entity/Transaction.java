package com.paymybuddy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    private String description;
    private Float amount;
    private Float percentOfFreshTransaction;

    @Column(name="date", columnDefinition="TIMESTAMP")
    private LocalDate date;

    public Transaction() {}
}

package com.paymybuddy.dto;

import com.paymybuddy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private long id;
    private User debtor;
    private User creditor;
    private String creditorEmailAddress;
    private String description;
    private double amount;
    private double percentOfFreshTransaction;
    private LocalDate date;

    public TransactionDTO() {
    }
}

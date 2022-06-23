package com.paymybuddy.dto;

import com.paymybuddy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private long id;
    private User debtor;
    private User creditor;
    private String description;
    private Float amount;
    private Float percentOfFreshTransaction;
}

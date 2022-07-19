package com.paymybuddy.dto;

import com.paymybuddy.entity.Account;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private Account account;
    private Set<User> friends;
    private Set<Transaction> debtorTransaction;
    private Set<Transaction> creditorTransaction;
    private String iban;
    private Double initialBalance;

    public UserDTO() {
    }
}

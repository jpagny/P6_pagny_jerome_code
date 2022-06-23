package com.paymybuddy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", unique = true)
    private Account account;

    @OneToMany()
    Set<User> friends;

    @OneToMany(mappedBy = "debtor")
    Set<Transaction> debtorTransaction;

    @OneToMany(mappedBy = "creditor")
    Set<Transaction> creditorTransaction;

    public User() {}
}

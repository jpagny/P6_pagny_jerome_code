package com.paymybuddy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Column(unique = true)
    private String emailAddress;

    @NotNull
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "iban", unique = true)
    private Account account;
    @ManyToMany()
    private Set<User> friends;
    @OneToMany(mappedBy = "debtor")
    private Set<Transaction> debtorTransaction;
    @OneToMany(mappedBy = "creditor")
    private Set<Transaction> creditorTransaction;

    public User() {
    }
}

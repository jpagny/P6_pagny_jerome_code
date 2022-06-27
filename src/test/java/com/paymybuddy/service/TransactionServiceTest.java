package com.paymybuddy.service;

import com.paymybuddy.entity.Account;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import com.paymybuddy.repository.AccountRepository;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.implement.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void initTransactionService() {
        transactionService = new TransactionService(transactionRepository,userRepository,accountRepository);
    }

    @Test
    @DisplayName("Should be returned transaction when a new transaction is created")
    public void should_BeReturnedNewUser_When_ANewUserIsCreated() throws Exception {
        User debtor = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(1,200), new HashSet<>(), new HashSet<>(), new HashSet<>());
        User creditor = new User(1, "nicolas", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(2,300), new HashSet<>(), new HashSet<>(), new HashSet<>());

        Transaction transaction = new Transaction(1, debtor, creditor, "xxx", 50, 0.05, LocalDate.now());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(debtor));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction newTransaction = transactionService.create(transaction);

        assertEquals(newTransaction, transaction);
    }

}

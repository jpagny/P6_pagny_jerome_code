package com.paymybuddy.service;

import com.paymybuddy.entity.Account;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
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
        transactionService = new TransactionService(transactionRepository, userRepository, accountRepository);
    }

    @Test
    @DisplayName("Should be returned transaction when a new transaction is created")
    public void should_BeReturnedNewUser_When_ANewUserIsCreated() throws Exception {
        User debtor = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account("xxx", 200), new HashSet<>(), new HashSet<>(), new HashSet<>());
        User creditor = new User(1, "nicolas", "pagny", "pagny.jerome@gmail.com", "xxx", new Account("ccc", 300), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Transaction transaction = new Transaction(1, debtor, creditor, "xxx", 50, 0.05, LocalDate.now());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(debtor));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction newTransaction = transactionService.create(transaction);

        assertEquals(newTransaction, transaction);
    }

    @Test
    @DisplayName("Should be exception when debtor doesn't exist in our database")
    public void should_beException_when_debtorDoesntExistInOurDatabase() {
        User debtor = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account("xxx", 200), new HashSet<>(), new HashSet<>(), new HashSet<>());
        User creditor = new User(1, "nicolas", "pagny", "pagny.nicolas@gmail.com", "xxx", new Account("ccc", 300), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Transaction transaction = new Transaction(1, debtor, creditor, "xxx", 50, 0.05, LocalDate.now());
        when(userRepository.findByEmailAddress("pagny.jerome@gmail.com")).thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                transactionService.create(transaction)
        );

        String expectedMessage = "Debtor doesn't exist with given email : " + debtor.getEmailAddress();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should be exception when creditor doesn't exist in our database")
    public void should_beException_when_creditorDoesntExistInOurDatabase() {
        User debtor = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account("xxx", 200), new HashSet<>(), new HashSet<>(), new HashSet<>());
        User creditor = new User(1, "nicolas", "pagny", "pagny.nicolas@gmail.com", "xxx", new Account("ccc", 300), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Transaction transaction = new Transaction(1, debtor, creditor, "xxx", 50, 0.05, LocalDate.now());
        when(userRepository.findByEmailAddress("pagny.jerome@gmail.com")).thenReturn(Optional.of(debtor));
        when(userRepository.findByEmailAddress("pagny.nicolas@gmail.com")).thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                transactionService.create(transaction)
        );

        String expectedMessage = "Creditor doesn't exist with given email : " + creditor.getEmailAddress();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should be exception when debtor haven't enough money")
    public void should_BeException_when_debtorHaventEnoughMoney() {
        User debtor = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account("xxx", 50), new HashSet<>(), new HashSet<>(), new HashSet<>());
        User creditor = new User(1, "nicolas", "pagny", "pagny.jerome@gmail.com", "xxx", new Account("ccc", 300), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Transaction transaction = new Transaction(1, debtor, creditor, "xxx", 100, 0.05, LocalDate.now());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(debtor));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Exception exception = assertThrows(Exception.class, () ->
                transactionService.create(transaction)
        );

        String expectedMessage = "Not enough money on account";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should be valid sold when there is a transaction")
    public void should_beValidSold_when_thereIsATransaction() throws Exception {
        User debtor = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account("xxx", 300), new HashSet<>(), new HashSet<>(), new HashSet<>());
        User creditor = new User(2, "nicolas", "pagny", "pagny.nicolas@gmail.com", "xxx", new Account("ccc", 50), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Transaction transaction = new Transaction(1, debtor, creditor, "xxx", 100, 0.05, LocalDate.now());
        when(userRepository.findByEmailAddress("pagny.jerome@gmail.com")).thenReturn(Optional.of(debtor));
        when(userRepository.findByEmailAddress("pagny.nicolas@gmail.com")).thenReturn(Optional.of(creditor));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        transactionService.create(transaction);

        double newSoldDebtor = 300 - 100 - 5;
        double newSoldCreditor = 50 + 100;

        assertEquals(newSoldDebtor, debtor.getAccount().getBalance());
        assertEquals(newSoldCreditor, creditor.getAccount().getBalance());
    }


}

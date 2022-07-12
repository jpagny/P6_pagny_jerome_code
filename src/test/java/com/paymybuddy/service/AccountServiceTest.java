package com.paymybuddy.service;

import com.paymybuddy.entity.Account;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.repository.AccountRepository;
import com.paymybuddy.service.implement.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void initAccountService() {
        accountService = new AccountService(accountRepository);
    }

    @Test
    @DisplayName("Should be returned account when a new account is created")
    public void should_BeReturnedNewAccount_When_ANewAccountIsCreated() throws ResourceIsAlreadyPresentException {
        Account account = new Account("xxx", 100);
        when(accountRepository.findById(any(String.class))).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account newAccount = accountService.create(account);

        assertEquals(newAccount, account);
    }

    @Test
    @DisplayName("Should be exception when account created is already exist in our database")
    public void should_beException_When_AccountCreatedIsAlreadyExistInOurDatabase() {
        Account account = new Account("xxx", 100);
        when(accountRepository.findById(any(String.class))).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        Exception exception = assertThrows(ResourceIsAlreadyPresentException.class, () ->
                accountService.create(account)
        );

        String expectedMessage = "Account already exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}

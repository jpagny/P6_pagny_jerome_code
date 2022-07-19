package com.paymybuddy.service.implement;

import com.paymybuddy.entity.Account;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.repository.AccountRepository;
import com.paymybuddy.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account create(Account account) throws ResourceIsAlreadyPresentException {

        Optional<Account> accountToCreate = accountRepository.findById(account.getIban());

        if (accountToCreate.isPresent()) {
            throw new ResourceIsAlreadyPresentException("Account with iban " + account.getIban() + " is already exist.");
        }

        return accountRepository.save(account);
    }
}

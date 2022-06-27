package com.paymybuddy.service.implement;

import com.paymybuddy.entity.Account;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.repository.AccountRepository;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public Transaction create(Transaction transaction) throws Exception {

        Optional<User> debtor = userRepository.findByEmailAddress(transaction.getDebtor().getEmailAddress());
        Optional<User> creditor = userRepository.findByEmailAddress(transaction.getCreditor().getEmailAddress());

        if (debtor.isEmpty()) {
            throw new ResourceNotFoundException("Debtor doesn't exist with given email : " + transaction.getDebtor().getEmailAddress());
        }

        if (creditor.isEmpty()) {
            throw new ResourceNotFoundException("Creditor doesn't exist with given email : " + transaction.getCreditor().getEmailAddress());
        }

        Account debtorAccount = debtor.get().getAccount();
        Account creditorAccount = creditor.get().getAccount();

        double soldeDebtor = debtorAccount.getBalance();
        double soldeCreditor = creditorAccount.getBalance();

        double newSoldeDebtor = soldeDebtor - transaction.getAmount(); // add free
        double newSoldeCreditor = soldeCreditor + transaction.getAmount();

        if (soldeDebtor - transaction.getAmount() < 0) {
            throw new Exception("Not enough money on account");
        }

        debtorAccount.setBalance(newSoldeDebtor);
        creditorAccount.setBalance(newSoldeCreditor);

        accountRepository.save(debtorAccount);
        accountRepository.save(creditorAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

}

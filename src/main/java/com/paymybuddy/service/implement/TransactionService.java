package com.paymybuddy.service.implement;

import com.paymybuddy.constant.Free;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.repository.AccountRepository;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    private final String DEBTOR = "debtor";
    private final String CREDITOR = "creditor";

    @Override
    public Transaction create(Transaction transaction) throws Exception {

        User debtor = userRepository.findByEmailAddress(transaction.getDebtor().getEmailAddress())
                .orElseThrow(() -> new ResourceNotFoundException("Debtor doesn't exist with given email : " + transaction.getDebtor().getEmailAddress()));

        User creditor = userRepository.findByEmailAddress(transaction.getCreditor().getEmailAddress())
                .orElseThrow(() -> new ResourceNotFoundException("Creditor doesn't exist with given email : " + transaction.getCreditor().getEmailAddress()));

        double newSoldDebtor;
        double newSoldCreditor;

        newSoldDebtor = calculateNewSold(DEBTOR, debtor.getAccount().getBalance(), transaction.getAmount());
        newSoldCreditor = calculateNewSold(CREDITOR, creditor.getAccount().getBalance(), transaction.getAmount());

        if (newSoldDebtor < 0) {
            throw new Exception("Not enough money on account");
        }

        debtor.getAccount().setBalance(newSoldDebtor);
        creditor.getAccount().setBalance(newSoldCreditor);

        accountRepository.save(debtor.getAccount());
        accountRepository.save(creditor.getAccount());
        transactionRepository.save(transaction);

        return transaction;
    }

    private double calculateNewSold(String type, double soldActual, double amount) throws Exception {
        if (type.equalsIgnoreCase(DEBTOR)) {
            return calculateNewSoldDebtor(soldActual, amount);
        } else if (type.equalsIgnoreCase(CREDITOR)) {
            return calculateNewSoldCreditor(soldActual, amount);
        } else {
            throw new Exception("Unknown user type : " + type);
        }
    }

    private double calculateNewSoldDebtor(double soldActual, double amount) {
        return soldActual - amount - calculateFreeTransaction(amount);
    }

    private double calculateNewSoldCreditor(double soldActual, double amount) {
        return soldActual + amount;
    }

    private double calculateFreeTransaction(double amountTransaction) {
        return amountTransaction * Free.FREE_0_05;
    }


}

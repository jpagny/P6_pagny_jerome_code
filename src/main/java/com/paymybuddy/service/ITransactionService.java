package com.paymybuddy.service;

import com.paymybuddy.entity.Transaction;

public interface ITransactionService {

    Transaction create(Transaction transaction) throws Exception;

}

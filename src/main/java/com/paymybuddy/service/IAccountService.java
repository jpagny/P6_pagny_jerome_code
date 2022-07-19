package com.paymybuddy.service;

import com.paymybuddy.entity.Account;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;

public interface IAccountService {

    Account create(Account account) throws ResourceIsAlreadyPresentException;

}

package com.paymybuddy.service;

import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentInOurDatabaseException;
import com.paymybuddy.exception.ResourceNotFoundException;

public interface IUserService {
    User create(User user) throws ResourceIsAlreadyPresentInOurDatabaseException;
    User update(User user) throws ResourceNotFoundException;

}

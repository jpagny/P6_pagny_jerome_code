package com.paymybuddy.service;

import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentInOurDatabaseException;

public interface IUserService {

    User create(User user) throws ResourceIsAlreadyPresentInOurDatabaseException;

}

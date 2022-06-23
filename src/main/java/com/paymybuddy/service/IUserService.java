package com.paymybuddy.service;

import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.exception.ResourceNotFoundException;

public interface IUserService {
    User create(User user) throws ResourceIsAlreadyPresentException;
    User update(User user) throws ResourceNotFoundException;
    void delete(User user) throws ResourceNotFoundException;
    User addFriend(User user, User friend) throws ResourceNotFoundException, ResourceIsAlreadyPresentException;


}

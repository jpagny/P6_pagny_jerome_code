package com.paymybuddy.service;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.exception.ResourceNotFoundException;

import java.util.Optional;

public interface IUserService {

    Optional<User> findByAddressEmail(String addressEmail);

    User create(User user) throws ResourceIsAlreadyPresentException;

    User create(UserDTO user) throws ResourceIsAlreadyPresentException;

    User update(User user) throws ResourceNotFoundException;

    User addFriend(User user, User friend) throws ResourceNotFoundException, ResourceIsAlreadyPresentException;

}

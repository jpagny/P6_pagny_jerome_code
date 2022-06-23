package com.paymybuddy.service;

import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentInOurDatabaseException;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) throws ResourceIsAlreadyPresentInOurDatabaseException {

        Optional<User> savedUser = userRepository.findByEmailAddress(user.getEmailAddress());

        if (savedUser.isPresent()) {
            throw new ResourceIsAlreadyPresentInOurDatabaseException("User already exist with given email : " + user.getEmailAddress());
        }

        return userRepository.save(user);
    }
}

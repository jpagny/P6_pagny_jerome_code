package com.paymybuddy.service;

import com.paymybuddy.entity.Account;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentInOurDatabaseException;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void initUserService() {
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should be returned user when a new user is created")
    public void should_BeReturnedNewUser_When_ANewUserIsCreated() throws ResourceIsAlreadyPresentInOurDatabaseException {
        User user = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User newUser = userService.create(user);

        assertEquals(newUser, user);
    }

    @Test
    @DisplayName("Should be exception when user created is already exist in our database")
    public void should_beException_When_UserCreatedIsAlreadyExistInOurDatabase() {
        User user = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        Exception exception = assertThrows(ResourceIsAlreadyPresentInOurDatabaseException.class, () ->
                userService.create(user)
        );

        String expectedMessage = "User already exist with given email : " + user.getEmailAddress();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should be returned user when user is updated")
    public void should_beReturnedUser_when_userIsUpdated() throws ResourceNotFoundException {
        User userToUpdate = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        User userUpdated = userService.update(userToUpdate);

        assertEquals(userUpdated, userRepository);
    }

    @Test
    @DisplayName("Should be exception when user updated doesn't exist in our database")
    public void should_beException_When_UserUpdatedDoesntExistInOurDatabase() {
        User user = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.update(user)
        );

        String expectedMessage = "User doesn't exist with given email : " + user.getEmailAddress();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }






}

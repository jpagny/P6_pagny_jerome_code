package com.paymybuddy.service;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.Account;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.implement.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public void should_BeReturnedNewUser_When_ANewUserIsCreated() throws ResourceIsAlreadyPresentException {
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
        Exception exception = assertThrows(ResourceIsAlreadyPresentException.class, () ->
                userService.create(user)
        );

        String expectedMessage = "User already exist with given email : " + user.getEmailAddress();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should be returned user when user is updated")
    public void should_beReturnedUser_when_userIsUpdated() throws ResourceNotFoundException {
        User userToUpdate = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "ccc", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        userToUpdate.setPassword("xxx");
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        User userUpdated = userService.update(userToUpdate);

        assertEquals(userUpdated, userToUpdate);
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

    @Test
    @DisplayName("Should be returned nothing when user is deleted")
    public void should_beReturnedNothing_when_userIsDeleted() {
        User user = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userRepository.delete(user);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Should be exception when user deleted doesn't exist in our database")
    public void should_beException_When_UserDeletedDoesntExistInOurDatabase() {
        User user = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.delete(user)
        );

        String expectedMessage = "User doesn't exist with given email : " + user.getEmailAddress();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should be returned the friend added when a new friend is added")
    public void should_beReturnedTheFriendAdded_when_aNewFriendIsAdded() throws ResourceNotFoundException, ResourceIsAlreadyPresentException {
        User user = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        User friend = new User(4, "nicolas", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(friend));

        User friendAdded = userService.addFriend(user, friend);

        assertEquals(friendAdded, friend);
        assertTrue(user.getFriends().contains(friend));
    }

    @Test
    @DisplayName("should be returned the user when user is exist")
    public void should_beReturnedTheUser_when_userIsExist() {
        User user = new User(1, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(userRepository.findByEmailAddress("pagny.jerome@gmail.com")).thenReturn(Optional.of(user));

        Optional<User> userFound = userService.findByAddressEmail("pagny.jerome@gmail.com");

        assertTrue(userFound.isPresent());
        assertEquals(userFound.get(), user);
    }

    @Test
    @DisplayName("should be returned user when a new user is created with DTO")
    public void should_beReturnedTheNewUser_when_userIsCreatedWithDTO() throws ResourceIsAlreadyPresentException {
        UserDTO userDTO = new UserDTO(1L, "jerome", "pagny", "pagny.jerome@gmail.com", "xxx", new Account(), new HashSet<User>(), new HashSet<Transaction>(), new HashSet<Transaction>(), "xxx-bbb-ccc", 1000.00);
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName((userDTO.getLastName()));
        user.setEmailAddress(userDTO.getEmailAddress());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setAccount(userDTO.getAccount());

        when(userRepository.findByEmailAddress("pagny.jerome@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User userSaved = userService.create(userDTO);

        assertEquals(userSaved, user);
    }


}

package com.paymybuddy.service.implement;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findByAddressEmail(String addressEmail) {
        return userRepository.findByEmailAddress(addressEmail);
    }

    @Override
    public User create(User user) throws ResourceIsAlreadyPresentException {

        userRepository.findByEmailAddress(user.getEmailAddress()).orElseThrow(
                () -> new ResourceIsAlreadyPresentException("User already exist with given email : " + user.getEmailAddress()));

        return userRepository.save(user);
    }

    @Override
    public User create(UserDTO userDTO) {
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setFirstName(userDTO.getFirstName());
        user.setLastName((userDTO.getLastName()));
        user.setEmailAddress(userDTO.getEmailAddress());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setAccount(userDTO.getAccount());

        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws ResourceNotFoundException {

        User updatedUser = userRepository.findByEmailAddress(user.getEmailAddress()).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress()));

        String email = user.getEmailAddress() == null ? updatedUser.getEmailAddress() : user.getEmailAddress();
        String password = user.getPassword() == null ? updatedUser.getPassword() : user.getPassword();

        updatedUser.setEmailAddress(email);
        updatedUser.setPassword(password);

        return userRepository.save(updatedUser);
    }

    @Override
    public void delete(User user) throws ResourceNotFoundException {
        User deletedUser = userRepository.findByEmailAddress(user.getEmailAddress()).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress()));

        userRepository.delete(deletedUser);
    }

    @Override
    public User addFriend(User user, User friend) throws ResourceNotFoundException, ResourceIsAlreadyPresentException {

        User theUser = userRepository.findByEmailAddress(user.getEmailAddress()).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress()));
        User theFriend = userRepository.findByEmailAddress(friend.getEmailAddress()).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress()));

        if (theUser.getFriends().contains(theFriend)) {
            throw new ResourceIsAlreadyPresentException("User " + user.getEmailAddress() + " is already friend with " + friend.getEmailAddress());
        }

        user.getFriends().add(friend);
        userRepository.friends(user);

        return friend;
    }


}

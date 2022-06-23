package com.paymybuddy.service;

import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) throws ResourceIsAlreadyPresentException {

        Optional<User> savedUser = userRepository.findByEmailAddress(user.getEmailAddress());

        if (savedUser.isPresent()) {
            throw new ResourceIsAlreadyPresentException("User already exist with given email : " + user.getEmailAddress());
        }

        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws ResourceNotFoundException {

        Optional<User> updatedUser = userRepository.findByEmailAddress(user.getEmailAddress());

        if (!updatedUser.isPresent()){
            throw new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress());
        }

        String email = user.getEmailAddress() == null ? updatedUser.get().getEmailAddress() : user.getEmailAddress();
        String password = user.getPassword() == null ? updatedUser.get().getPassword() : user.getPassword();

        updatedUser.get().setEmailAddress(email);
        updatedUser.get().setPassword(password);

        return userRepository.save(updatedUser.get());
    }

    @Override
    public void delete(User user) throws ResourceNotFoundException {

        Optional<User> deletedUser = userRepository.findByEmailAddress(user.getEmailAddress());

        if (!deletedUser.isPresent()){
            throw new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress());
        }

        userRepository.delete(user);
    }

    @Override
    public User addFriend(User user, User friend) throws ResourceNotFoundException, ResourceIsAlreadyPresentException {

        Optional<User> theUser = userRepository.findByEmailAddress(user.getEmailAddress());
        Optional<User> theFriend = userRepository.findByEmailAddress(friend.getEmailAddress());

        if ( !theUser.isPresent() ){
            throw new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress());
        }

        if ( !theFriend.isPresent() ){
            throw new ResourceNotFoundException("User doesn't exist with given email : " + friend.getEmailAddress());
        }

        if ( user.getFriends().contains(theFriend) ){
            throw new ResourceIsAlreadyPresentException("User "  + user.getEmailAddress() + " is already friend with " + friend.getEmailAddress());
        }

        user.getFriends().add(friend);
        userRepository.friends(user);

        return friend;
    }


}

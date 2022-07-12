package com.paymybuddy.service.implement;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.Transaction;
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

        Optional<User> savedUser = userRepository.findByEmailAddress(user.getEmailAddress());

        if (savedUser.isPresent()) {
            throw new ResourceIsAlreadyPresentException("User already exist with given email : " + user.getEmailAddress());
        }

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

        Optional<User> updatedUser = userRepository.findByEmailAddress(user.getEmailAddress());

        if (updatedUser.isEmpty()) {
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

        if (deletedUser.isEmpty()) {
            throw new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress());
        }

        userRepository.delete(user);
    }

    @Override
    public User addFriend(User user, User friend) throws ResourceNotFoundException, ResourceIsAlreadyPresentException {

        Optional<User> theUser = userRepository.findByEmailAddress(user.getEmailAddress());
        Optional<User> theFriend = userRepository.findByEmailAddress(friend.getEmailAddress());

        if (theUser.isEmpty()) {
            throw new ResourceNotFoundException("User doesn't exist with given email : " + user.getEmailAddress());
        }

        if (theFriend.isEmpty()) {
            throw new ResourceNotFoundException("User doesn't exist with given email : " + friend.getEmailAddress());
        }

        if (user.getFriends().contains(friend)) {
            throw new ResourceIsAlreadyPresentException("User " + user.getEmailAddress() + " is already friend with " + friend.getEmailAddress());
        }

        user.getFriends().add(friend);
        userRepository.friends(user);

        return friend;
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        Optional<User> debtor = userRepository.findByEmailAddress(transaction.getDebtor().getEmailAddress());
        Optional<User> creditor = userRepository.findByEmailAddress(transaction.getCreditor().getEmailAddress());

        debtor.get().getDebtorTransaction().add(transaction);
        creditor.get().getCreditorTransaction().add(transaction);

        userRepository.debtorTransaction(debtor.get());
        userRepository.creditorTransaction((creditor.get()));

        return transaction;
    }


}

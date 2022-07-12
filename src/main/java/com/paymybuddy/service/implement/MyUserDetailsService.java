package com.paymybuddy.service.implement;

import com.paymybuddy.entity.User;
import com.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String addressEmail) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmailAddress(addressEmail);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with Email: " + addressEmail);
        }

        return new org.springframework.security.core.userdetails.User(user.get().getEmailAddress(), user.get().getPassword(), new ArrayList<>());
    }

}

package com.paymybuddy.controller;

import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.service.implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String signupView(Authentication authentication, Model model) throws ResourceNotFoundException {

        User user = userService.findByAddressEmail(authentication.getName()).orElseThrow(ResourceNotFoundException::new);

        model.addAttribute("user", user);
        model.addAttribute("account",user.getAccount());

        return "profile";
    }


}

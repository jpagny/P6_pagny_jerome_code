package com.paymybuddy.controller;

import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.service.implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String getHomePage(Authentication authentication, Model model) throws ResourceNotFoundException {

        User user = userService.findByAddressEmail(authentication.getName()).orElseThrow(ResourceNotFoundException::new);

        model.addAttribute("userName", StringUtils.capitalize(user.getFirstName()
                + " "
                + StringUtils.capitalize(user.getLastName())));
        model.addAttribute("account", user.getAccount());
        model.addAttribute("transactionDebtorList", user.getCreditorTransaction());
        model.addAttribute("transactionCreditorList", user.getDebtorTransaction());

        return "home";
    }

}

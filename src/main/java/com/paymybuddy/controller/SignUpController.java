package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.Account;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.service.implement.AccountService;
import com.paymybuddy.service.implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public SignUpController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping
    public String signupView() {
        return "signup";
    }

    @PostMapping
    private String signupUser(@ModelAttribute UserDTO user, Model model, RedirectAttributes redirectAttributes) throws ResourceIsAlreadyPresentException {
        String signupError = null;
        Optional<User> existsUser = userService.findByAddressEmail(user.getEmailAddress());

        if (existsUser.isPresent()) {
            signupError = "The email already exists";
        } else {
            Account account = new Account(user.getIban(),user.getInitialBalance());
            Account accountSaved = accountService.create(account);
            user.setAccount(accountSaved);
            userService.create(user);
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("message", "You've successfully signed up, please login.");
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", true);
        }

        return "signup";
    }

}

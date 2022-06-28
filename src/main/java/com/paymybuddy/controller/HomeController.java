package com.paymybuddy.controller;

import com.paymybuddy.entity.User;
import com.paymybuddy.service.implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String getHome(Authentication authentication, Model model) {

        Optional<User> user = userService.findByAddressEmail(authentication.getName());

        model.addAttribute("userName", StringUtils.capitalize(user.get().getFirstName()
                + " "
                + StringUtils.capitalize(user.get().getLastName())));
        model.addAttribute("listFriend", user.get().getFriends());

        return "home";
    }

}

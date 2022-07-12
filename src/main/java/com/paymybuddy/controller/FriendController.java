package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.service.implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class FriendController {

    @Autowired
    private UserService userService;

    @PostMapping("/addFriend")
    public String addFriend(Authentication authentication,
                            @ModelAttribute("friend") UserDTO friend, RedirectAttributes redirectAttributes, Model model) throws Exception {

        String addFriendError = null;

        User auth = userService.findByAddressEmail(authentication.getName()).orElseThrow(ResourceNotFoundException::new);
        Optional<User> friendToAdd = userService.findByAddressEmail(friend.getEmailAddress());

        try {
            userService.addFriend(auth, friendToAdd.get());
        } catch (ResourceIsAlreadyPresentException ex) {
            addFriendError = "You are already friend with this address email : " + friend.getEmailAddress() + ".";

        } catch (NoSuchElementException ex){
            addFriendError = ex.getMessage();

        }

        if (addFriendError != null) {
            redirectAttributes.addFlashAttribute("addFriendError", addFriendError);

        } else {
            redirectAttributes.addFlashAttribute("addFriendSuccess", "You've successfully added friend.");

        }

        return "redirect:/transfer";
    }


}

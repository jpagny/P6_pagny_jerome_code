package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceIsAlreadyPresentException;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.service.implement.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class FriendController {

    private final UserService userService;

    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addFriend")
    public String addFriend(Authentication authentication,
                            @ModelAttribute("friend") UserDTO friend, RedirectAttributes redirectAttributes) throws Exception {

        String addFriendError = null;

        try {
            User auth = userService.findByAddressEmail(authentication.getName()).orElseThrow(ResourceNotFoundException::new);
            Optional<User> friendToAdd = userService.findByAddressEmail(friend.getEmailAddress());
            userService.addFriend(auth, friendToAdd.get());
        } catch (ResourceIsAlreadyPresentException ex) {
            addFriendError = "You are already friend with this address email : " + friend.getEmailAddress() + ".";

        } catch (NoSuchElementException ex) {
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

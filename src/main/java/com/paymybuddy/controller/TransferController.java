package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.service.implement.TransactionService;
import com.paymybuddy.service.implement.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
public class TransferController {

    private final UserService userService;

    private final TransactionService transactionService;

    public TransferController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/transfer")
    public String getTransferPage(Authentication authentication, Model model) throws ResourceNotFoundException {

        User user = userService.findByAddressEmail(authentication.getName()).orElseThrow(ResourceNotFoundException::new);
        Set<User> friends = user.getFriends() != null ? user.getFriends() : new HashSet<>();

        model.addAttribute("userName", StringUtils.capitalize(user.getFirstName()
                + " "
                + StringUtils.capitalize(user.getLastName())));

        model.addAttribute("friendList", friends);

        model.addAttribute("transactionList", user.getDebtorTransaction());

        return "transfer";
    }

    @PostMapping("/transfer")
    public String sendMoney(Authentication authentication,
                            @ModelAttribute("transaction") TransactionDTO transactionDTO, RedirectAttributes redirectAttributes) {

        String transactionError = null;

        try {

            User auth = userService.findByAddressEmail(authentication.getName()).orElseThrow(ResourceNotFoundException::new);
            User creditor = userService.findByAddressEmail(transactionDTO.getCreditorEmailAddress()).orElseThrow(ResourceNotFoundException::new);

            transactionDTO.setDebtor(auth);
            transactionDTO.setCreditor(creditor);
            Transaction transaction = new ModelMapper().map(transactionDTO, Transaction.class);
            transactionService.create(transaction);

        } catch (Exception ex) {
            transactionError = ex.getMessage();

        }

        if (transactionError != null) {
            redirectAttributes.addFlashAttribute("transactionError", transactionError);

        } else {
            redirectAttributes.addFlashAttribute("transactionSuccess", "You've successfully added transaction.");

        }

        return "redirect:/transfer";
    }

}

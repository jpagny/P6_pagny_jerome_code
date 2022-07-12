package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import com.paymybuddy.exception.ResourceNotFoundException;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.service.implement.TransactionService;
import com.paymybuddy.service.implement.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class TransferController {

    @Autowired
    private UserService userService;

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
                            @ModelAttribute("newTransfer") TransactionDTO transactionDTO, Model model) throws Exception {

        boolean isFriend = false;

        // check if debtor exists
        User auth = userService.findByAddressEmail(authentication.getName()).orElseThrow(ResourceNotFoundException::new);

        // check if creditor exists
        User creditor = userService.findByAddressEmail(transactionDTO.getCreditor().getEmailAddress()).orElseThrow(ResourceNotFoundException::new);

        // check if they are friends
        isFriend = auth.getFriends().contains(creditor);

        if (isFriend){
            Transaction transaction = new ModelMapper().map(transactionDTO, Transaction.class);
            userService.addTransaction(transaction);
        }

        return "result";
    }

}

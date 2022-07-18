package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

@SpringBootTest
@Sql("/data.sql")
@Transactional
public class TransferControllerTestIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("pagny.jerome@gmail.com")
    @DisplayName("Should be able to visit transfer page when user is authenticated ")
    public void should_beAbleToVisitHomePage_when_userIsAuthenticated() throws Exception {
        mockMvc.perform(get("/transfer"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("pagny.jerome@gmail.com")
    @DisplayName("Should be returned 302 and redirect to transfer page when transfer is success")
    public void should_beReturned302AndRedirectToTransferPage_when_transferIsSuccess() throws Exception {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setCreditorEmailAddress("bouilly.pauline@gmail.com");
        transactionDTO.setAmount(100);
        transactionDTO.setDescription("Rent pizza");

        mockMvc.perform(post("/transfer")
                        .with(csrf())
                        .flashAttr("transaction", transactionDTO))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("transactionSuccess"))
                .andExpect(flash().attribute("transactionSuccess", "You've successfully added transaction."))
                .andExpect(redirectedUrl("/transfer"));
    }

    @Test
    @WithUserDetails("pagny.jerome@gmail.com")
    @DisplayName("Should be returned message transactionError and redirect to transfer page when debtor don't have enough money")
    public void should_beReturnedMessageTransactionErrorAndRedirectToTransferPage_when_debtorDontHaveEnoughMoney() throws Exception {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setCreditorEmailAddress("bouilly.pauline@gmail.com");
        transactionDTO.setAmount(10000);
        transactionDTO.setDescription("Rent pizza");

        mockMvc.perform(post("/transfer")
                        .with(csrf())
                        .flashAttr("transaction", transactionDTO))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("transactionError"))
                .andExpect(flash().attribute("transactionError", "Not enough money on account"))
                .andExpect(redirectedUrl("/transfer"));
    }


}

package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Sql("/data.sql")
@Transactional
public class SignUpControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should be able visit signup page when get /signup url")
    public void should_beAbleVisitSignUpPageWhenGetSignUpUrl() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should be returned 302 and redirect to login page when registration is success")
    public void should_beReturned302AndRedirectToLoginPage_when_registrationIsSuccess() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1000L);
        userDTO.setFirstName("john");
        userDTO.setLastName("pagny");
        userDTO.setEmailAddress("pagny.john");
        userDTO.setPassword("xxx");
        userDTO.setIban("zzz-xxx-vvv");
        userDTO.setInitialBalance(3000.0);

        mockMvc.perform(post("/signup")
                        .flashAttr("user", userDTO))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser
    @DisplayName("Should be returned message signupError when user to register is already exist")
    public void should_beReturnedMessageSignupError_when_userToRegisterIsAlreadyExist() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1000L);
        userDTO.setFirstName("jerome");
        userDTO.setLastName("pagny");
        userDTO.setEmailAddress("pagny.jerome@gmail.com");
        userDTO.setPassword("xxx");
        userDTO.setIban("zzz-xxx-vvv");
        userDTO.setInitialBalance(3000.0);

        mockMvc.perform(post("/signup")
                        .flashAttr("user", userDTO))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(model().attributeExists("signupError"))
                .andExpect(model().attribute("signupError", "The email already exists"));
    }


}

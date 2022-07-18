package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)

public class SignUpControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void signUpTest() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1000L);
        userDTO.setFirstName("john");
        userDTO.setLastName("pagny");
        userDTO.setEmailAddress("pagny.john");
        userDTO.setPassword("xxx");
        userDTO.setIban("zzz-xxx-ccc");
        userDTO.setInitialBalance(3000.0);

        mockMvc.perform(post("/signup")
                        .flashAttr("user", userDTO))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));

    }


}

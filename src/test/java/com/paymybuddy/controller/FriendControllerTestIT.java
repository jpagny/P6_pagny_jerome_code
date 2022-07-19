package com.paymybuddy.controller;


import com.paymybuddy.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Sql("/data.sql")
@Transactional
public class FriendControllerTestIT {

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
    @DisplayName("Should be added a friend when user add a new friend")
    public void should_beAddedANewFriend_when_userAddANewFriend() throws Exception {

        UserDTO friendToAdd = new UserDTO();
        friendToAdd.setEmailAddress("pagny.nicolas@gmail.com");

        mockMvc.perform(post("/addFriend")
                        .with(csrf())
                        .flashAttr("friend", friendToAdd))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("addFriendSuccess"))
                .andExpect(flash().attribute("addFriendSuccess", "You've successfully added friend."))
                .andExpect(redirectedUrl("/transfer"));
    }

    @Test
    @WithUserDetails("pagny.jerome@gmail.com")
    @DisplayName("Should be returned message addFriendError when friend to add doesn't exist")
    public void should_beReturnedMessageAddFriendErrorWhenFriendToAddDoesntExist() throws Exception {

        UserDTO friendToAdd = new UserDTO();
        friendToAdd.setEmailAddress("x@gmail.com");

        mockMvc.perform(post("/addFriend")
                        .with(csrf())
                        .flashAttr("friend", friendToAdd))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(flash().attributeExists("addFriendError"))
                .andExpect(flash().attribute("addFriendError", "No value present"))
                .andExpect(redirectedUrl("/transfer"));
    }

    @Test
    @WithUserDetails("pagny.jerome@gmail.com")
    @DisplayName("Should be returned message addFriendError when friend to add is already added")
    public void should_beReturnedMessageAddFriendErrorWhenFriendToAddIsAlreadyAdded() throws Exception {

        UserDTO friendToAdd = new UserDTO();
        friendToAdd.setEmailAddress("bouilly.pauline@gmail.com");

        mockMvc.perform(post("/addFriend")
                        .with(csrf())
                        .flashAttr("friend", friendToAdd))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(flash().attributeExists("addFriendError"))
                .andExpect(flash().attribute("addFriendError", "You are already friend with this address email : bouilly.pauline@gmail.com."))
                .andExpect(redirectedUrl("/transfer"));
    }


}

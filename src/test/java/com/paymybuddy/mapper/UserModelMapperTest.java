package com.paymybuddy.mapper;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.entity.Account;
import com.paymybuddy.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserModelMapperTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    @DisplayName("Should be mapped by User entity to User DTO when ModelMapper is called")
    public void shouldBeMappedByEntityToDTO_WhenModelMapperIsCalled() {
        User user = new User(1, "Jerome", "Pagny", "pagny.jerome@", "xxx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getLastName(), user.getLastName());
        assertEquals(userDTO.getEmailAddress(), user.getEmailAddress());
        assertEquals(userDTO.getPassword(), user.getPassword());
        assertEquals(userDTO.getAccount(), user.getAccount());
        assertEquals(userDTO.getFriends(), user.getFriends());
        assertEquals(userDTO.getDebtorTransaction(), user.getDebtorTransaction());
        assertEquals(userDTO.getCreditorTransaction(), user.getCreditorTransaction());
    }

    @Test
    @DisplayName("Should be mapped by User DTO to User entity when ModelMapper is called")
    public void shouldBeMappedByDTOToEntity_WhenModelMapperIsCalled() {
        UserDTO userDTO = new UserDTO(1, "Pagny", "Jerome", "pagny.jerome@gmail.com", "xx", new Account(), new HashSet<>(), new HashSet<>(), new HashSet<>());

        User user = modelMapper.map(userDTO, User.class);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(user.getEmailAddress(), userDTO.getEmailAddress());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.getAccount(), userDTO.getAccount());
        assertEquals(user.getFriends(), userDTO.getFriends());
        assertEquals(user.getDebtorTransaction(), userDTO.getDebtorTransaction());
        assertEquals(user.getCreditorTransaction(), userDTO.getCreditorTransaction());
    }


}

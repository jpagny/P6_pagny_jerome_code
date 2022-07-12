package com.paymybuddy.mapper;

import com.paymybuddy.dto.AccountDTO;
import com.paymybuddy.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountModelMapperTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    @DisplayName("Should be mapped by Account entity to Account DTO when ModelMapper is called")
    public void shouldBeMappedByEntityToDTO_WhenModelMapperIsCalled() {
        Account account = new Account("XXX-CCC-BBB", 100);

        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);

        assertEquals(accountDTO.getIban(), account.getIban());
        assertEquals(accountDTO.getBalance(), account.getBalance());
    }

    @Test
    @DisplayName("Should be mapped by Account DTO to Account entity when ModelMapper is called")
    public void shouldBeMappedByDTOToEntity_WhenModelMapperIsCalled() {
        AccountDTO accountDTO = new AccountDTO("xxx", 100);

        Account account = modelMapper.map(accountDTO, Account.class);

        assertEquals(account.getIban(), accountDTO.getIban());
        assertEquals(account.getBalance(), accountDTO.getBalance());
    }
}

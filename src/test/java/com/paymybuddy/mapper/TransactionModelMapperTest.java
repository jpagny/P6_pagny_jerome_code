package com.paymybuddy.mapper;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.entity.Transaction;
import com.paymybuddy.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionModelMapperTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    @DisplayName("Should be mapped by Transaction entity to Transaction DTO when ModelMapper is called")
    public void shouldBeMappedByEntityToDTO_WhenModelMapperIsCalled() {

        Transaction transaction = new Transaction(1, new User(), new User(), "Test", 100, 0.05, LocalDate.now());

        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        assertEquals(transactionDTO.getId(), transaction.getId());
        assertEquals(transactionDTO.getDebtor(), transaction.getDebtor());
        assertEquals(transactionDTO.getCreditor(), transaction.getCreditor());
        assertEquals(transactionDTO.getDescription(), transaction.getDescription());
        assertEquals(transactionDTO.getAmount(), transaction.getAmount());
        assertEquals(transactionDTO.getDate(), transaction.getDate());
    }

    @Test
    @DisplayName("Should be mapped by Transaction DTO to Transaction entity when ModelMapper is called")
    public void shouldBeMappedByDTOToEntity_WhenModelMapperIsCalled() {
        TransactionDTO transactionDTO = new TransactionDTO(1, new User(), new User(), "Test", 100, 0.05, LocalDate.now());

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);

        assertEquals(transaction.getId(), transactionDTO.getId());
        assertEquals(transaction.getDebtor(), transactionDTO.getDebtor());
        assertEquals(transaction.getCreditor(), transactionDTO.getCreditor());
        assertEquals(transaction.getDescription(), transactionDTO.getDescription());
        assertEquals(transaction.getAmount(), transactionDTO.getAmount());
        assertEquals(transaction.getDate(), transactionDTO.getDate());
    }
}

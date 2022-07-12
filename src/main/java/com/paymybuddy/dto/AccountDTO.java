package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
    private String iban;
    private float balance;

    public AccountDTO() {
    }
}

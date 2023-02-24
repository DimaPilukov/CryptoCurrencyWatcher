package com.example.dto;

import com.example.domain.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {
    private Long id;
    private String username;
    private BigDecimal coinPricePerRegistration;
    private Currency currency;
}

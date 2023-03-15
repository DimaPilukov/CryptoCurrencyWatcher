package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CryptoCurrencyDTO {

    private String username;
    private String password;
    private String symbol;
    private Long currencyId;

}

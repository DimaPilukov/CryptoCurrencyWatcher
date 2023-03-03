package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CryptoCurrency {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String symbol;
}

package com.example.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String username;
    private String symbol;
    private double fixedPrice;

    private boolean isDone;

    public User(String username, String symbol, double fixedPrice) {
        this.username = username;
        this.symbol = symbol;
        this.fixedPrice = fixedPrice;
    }
}

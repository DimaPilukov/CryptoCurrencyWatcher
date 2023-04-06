package com.example.domain;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Id;

@Getter
public class CoinLoreResponse {
    @Id
    @JsonProperty("id")
    private Long id;
    @JsonProperty("price_usd")
    private double price;
}
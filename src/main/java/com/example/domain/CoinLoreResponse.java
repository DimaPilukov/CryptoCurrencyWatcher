package com.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.persistence.Id;

@Getter
public class CoinLoreResponse {
    @Id
    @JsonProperty("id")
    private Long id;
    @JsonProperty("price_usd")
    private double price;
}
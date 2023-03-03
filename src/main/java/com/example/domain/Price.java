package com.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Id;

@Getter
public class Price {

    @Id
    @JsonProperty("id")
    private Long id;

    @JsonProperty("price_usd")
    private double price;

}

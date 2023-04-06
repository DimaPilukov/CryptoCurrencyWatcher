package com.example.service;

import com.example.domain.CoinLoreResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.Optional;

@Configuration
public class CoinLoreClient {

    @Value("${coinlore.url}")
    private String URL;
    private final Json json;

    public CoinLoreClient(Json json) {
        this.json = json;
    }

    public Optional<CoinLoreResponse> getCurrentPrice(Long currencyId) {
        CoinLoreResponse[] coinLoreResponse = json.getForObject(URL + currencyId, CoinLoreResponse[].class);
        if (coinLoreResponse == null) {
            return Optional.empty();
        }
        for (CoinLoreResponse item : coinLoreResponse) {
            if (Objects.equals(currencyId, item.getId())) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
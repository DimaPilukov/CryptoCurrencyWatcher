package com.example.service;

import com.example.domain.CoinLoreResponse;
import com.example.domain.CryptoCurrency;
import com.example.domain.UserCryptoCurrency;
import com.example.repository.CryptoCurrencyRepository;
import com.example.repository.UserCryptoCurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CryptoService {
    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final UserCryptoCurrencyRepository userCryptoCurrencyRepository;
    private final CoinLoreClient coinLoreClient;

    public CryptoService(CryptoCurrencyRepository currencyRepository,
                         UserCryptoCurrencyRepository userCryptoCurrencyRepository,
                         CoinLoreClient coinLoreClient) {
        this.cryptoCurrencyRepository = currencyRepository;
        this.userCryptoCurrencyRepository = userCryptoCurrencyRepository;
        this.coinLoreClient = coinLoreClient;
    }


    @PostConstruct
    @Scheduled(cron = "0 * * * * *")
    public void updateAndNotify() {
        List<UserCryptoCurrency> userCryptoCurrencyList = userCryptoCurrencyRepository.findAll();
        if (userCryptoCurrencyList.size() == 0) {
            addData();
        }
        Set<Long> cryptoCurrencyId = userCryptoCurrencyList.stream()
                .map(UserCryptoCurrency::getCryptoCurrency)
                .map(CryptoCurrency::getId)
                .collect(Collectors.toSet());
        for (Long item : cryptoCurrencyId) {
            List<UserCryptoCurrency> filterList = userCryptoCurrencyList.stream()
                    .filter(userCryptoCurrency -> userCryptoCurrency.getCryptoCurrency().getId().equals(item))
                    .collect(Collectors.toList());
            for (UserCryptoCurrency item1 : filterList) {
                Optional<CoinLoreResponse> price = coinLoreClient.getCurrentPrice(item1.getCryptoCurrency().getId());
                if (price.isPresent()) {
                    double currentPrice = price.get().getPrice();
                    double fixedPrice = item1.getPriceCoinLore();
                    double difference = Math.abs((fixedPrice - currentPrice) / fixedPrice) * 100;
                    if (difference >= 1) {
                        log.warn(String.format("Username: %s, symbol: %s, percentage change %f%%",
                                item1.getUser(), item1.getCryptoCurrency(), difference));
                    }
                }
            }
        }
    }

    private void addData() {
        List<CryptoCurrency> data = new ArrayList<>();
        data.add(new CryptoCurrency(90L, "BTC"));
        data.add(new CryptoCurrency(80L, "ETH"));
        data.add(new CryptoCurrency(48543L, "SOL"));
        cryptoCurrencyRepository.saveAll(data);
    }
}
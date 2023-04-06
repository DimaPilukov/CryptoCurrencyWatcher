package com.example.service;

import com.example.domain.CryptoCurrency;
import com.example.domain.UserCryptoCurrency;
import com.example.repository.CryptoCurrencyRepository;
import com.example.repository.UserCryptoCurrencyRepository;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CryptoService {
    private final CoinLoreClient coinLoreClient;
    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final UserCryptoCurrencyRepository userCryptoCurrencyRepository;
    private final Logger log = LogManager.getLogger(CryptoService.class);

    public CryptoService(CoinLoreClient coinLoreClient,
                         CryptoCurrencyRepository currencyRepository,
                         UserCryptoCurrencyRepository userCryptoCurrencyRepository) {
        this.coinLoreClient = coinLoreClient;
        this.cryptoCurrencyRepository = currencyRepository;
        this.userCryptoCurrencyRepository = userCryptoCurrencyRepository;
    }


    @PostConstruct
    @Scheduled(cron = "0 * * * * *")
    public void updateAndNotify() {
        List<UserCryptoCurrency> userCryptoCurrencyList = userCryptoCurrencyRepository.findAll();
        List<CryptoCurrency> cryptoCurrencyList = cryptoCurrencyRepository.findAll();
        if (cryptoCurrencyList.size() == 0) {
            addData();
        }
        Set<Long> cryptoCurrencyId = userCryptoCurrencyList.stream()
                .map(UserCryptoCurrency::getCryptoCurrency)
                .map(CryptoCurrency::getId)
                .collect(Collectors.toSet());
        cryptoCurrencyId.forEach(currencyID -> {
            coinLoreClient.getCurrentPrice(currencyID)
                    .ifPresent(loreResponse -> {
                        double currentPrice = loreResponse.getPrice();
                        userCryptoCurrencyList.stream()
                                .filter(ucc -> Objects.equals(ucc.getCryptoCurrency().getId(), currencyID))
                                .forEach(ucc -> currencyPriceCheck(currentPrice, ucc));
                    });
        });
    }

    private void currencyPriceCheck(double currentPrice, @NonNull UserCryptoCurrency ucc) {
        double fixedPrice = ucc.getPriceCoinLore();
        double difference = Math.abs((fixedPrice - currentPrice) / fixedPrice) * 100;
        if (difference >= 1) {
            log.warn("Username: {} Symbol: {} percentage change: {}.",
                    ucc.getUser().getUsername(), ucc.getCryptoCurrency().getSymbol(), difference);
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
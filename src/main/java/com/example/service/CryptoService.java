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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        List<UserCryptoCurrency> list = userCryptoCurrencyRepository.findAll();
        if (list.size() == 0) {
            addData();
        }
        for (UserCryptoCurrency item : list) {
            double fixedPrice = item.getPriceCoinLore();
            Optional<CoinLoreResponse> coinLoreResponse =
                    coinLoreClient.getCurrentPrice(item.getCryptoCurrency().getId());
            if (coinLoreResponse.isPresent()) {
                double currentPrice = coinLoreResponse.get().getPrice();
                double difference = Math.abs((fixedPrice - currentPrice) / fixedPrice) * 100;
                if (difference >= 1) {
                    log.warn(String.format("Username: %s, symbol: %s, percentage change %f%%",
                            item.getUser(), item.getCryptoCurrency(), difference));
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
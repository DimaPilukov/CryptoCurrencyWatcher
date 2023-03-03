package com.example.service;

import com.example.domain.CryptoCurrency;
import com.example.domain.Price;

import com.example.domain.User;
import com.example.domain.UserCryptoCurrency;
import com.example.repository.CryptoCurrencyRepository;
import com.example.repository.UserCryptoCurrencyRepository;
import com.example.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CryptoService {

    @Value("${coinlore.url}")
    private String URL;

    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final UserCryptoCurrencyRepository userCryptoCurrencyRepository;
    private final UserRepository userRepository;
    private final Json json;

    private final String urlBTC = "90";
    private final String urlSOL = "80";
    private final String urlETH = "48543";

    public CryptoService(CryptoCurrencyRepository currencyRepository,
                         UserRepository userRepository,
                         UserCryptoCurrencyRepository userCryptoCurrencyRepository,
                         Json json) {
        this.userCryptoCurrencyRepository = userCryptoCurrencyRepository;
        this.cryptoCurrencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.json = json;
    }

    Price[] price;
    public double getPRICE(String symbol) {
        if (symbol.equals("BTH")){
            price = json.getForObject(URL + urlBTC, Price[].class);
            return price[0].getPrice();
        } else if (symbol.equals("SOL")){
            price = json.getForObject(URL + urlSOL, Price[].class);
            return price[0].getPrice();
        } else {
            price = json.getForObject(URL + urlETH, Price[].class);
            return price[0].getPrice();
        }
    }

    @Scheduled(cron = "* * * * * *")
    public void updateAndNotify() {
        if (cryptoCurrencyRepository.findAll().size() == 0) {
            addData();
        }
        for (UserCryptoCurrency userCryptoCurrency : userCryptoCurrencyRepository.findAll()) {
            double fixedPrice = userCryptoCurrency.getPriceCoinLore();
            double currentPrice = price[0].getPrice();
            double difference = Math.abs((fixedPrice - currentPrice) / fixedPrice) * 100;
            if (difference >= 1) {
                log.warn(String.format("Username: %s, symbol: %s, percentage change %f%%",
                        userCryptoCurrency.getUser(), userCryptoCurrency.getCryptoCurrency(), difference));
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

//    public double getPRICE(String symbol) {
//        if (symbol.equals("BTC")) {
//            Price[] priceBTC = json.getForObject(URL + urlBTC, Price[].class);
//            return priceCoinLoreBTC = priceBTC[0].getPrice();
//
//        } else if (symbol.equals("SOL")) {
//            Price[] priceSOL = json.getForObject(URL + urlSOL, Price[].class);
//            return priceCoinLoreSOL = priceSOL[0].getPrice();
//
//        } else {
//            Price[] priceETH = json.getForObject(URL + urlETH, Price[].class);
//            return priceCoinLoreETH = priceETH[0].getPrice();
//        }
//    }
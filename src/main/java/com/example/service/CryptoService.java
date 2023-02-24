package com.example.service;

import com.example.domain.Currency;
import com.example.domain.Tickers;
import com.example.domain.User;
import com.example.repo.CurrencyRepository;
import com.example.repo.UserRepository;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Slf4j
@Service
public class CryptoService {

    private CurrencyRepository currencyRepository;
    private UserRepository userRepository;
    private Json json;

    private static final String COIN_LORE_URL = "https://api.coinlore.net/api/tickers/";


//    private static Logger log = Logger.getLogger(String.valueOf(CryptoService.class));


    public CryptoService(CurrencyRepository currencyRepository,
                         UserRepository userRepository,
                         Json json) {
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.json = json;
    }

    //    @PostConstruct
    @Scheduled(cron = "* * * * * *")
    public void updateAndNotify() {

        ResponseEntity<Tickers> response = json.getForEntity(COIN_LORE_URL, Tickers.class);
        currencyRepository.saveAll(response.getBody().getData());

        for (User notification : userRepository.findAll()) {
            double fixedPrice = notification.getFixedPrice();
            double currentPrice = findBySymbol(notification.getSymbol()).getPrice();
            double difference = Math.abs((fixedPrice - currentPrice) / fixedPrice) * 100;
            if (difference >= 1) {

                log.warn(String.format("Username: %s, symbol: %s, percentage change %f%%",
                        notification.getUsername(), notification.getSymbol(), difference));

                notification.setDone(true);
                notification.setFixedPrice(currentPrice);
                userRepository.save(notification);
            }
        }
    }

    public Currency findBySymbol(String symbol) {
        return currencyRepository.findBySymbol(symbol.toUpperCase());
    }

    public void notify(String username, String symbol) {
        userRepository.save(new User(username, symbol, findBySymbol(symbol).getPrice()));
    }
}

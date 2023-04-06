package com.example.controller;

import com.example.domain.CryptoCurrency;
import com.example.domain.UserCryptoCurrency;
import com.example.dto.CryptoCurrencyDTO;
import com.example.repository.CryptoCurrencyRepository;
import com.example.repository.UserCryptoCurrencyRepository;
import com.example.repository.UserRepository;
import com.example.service.CoinLoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
//@RequestMapping(value = "CryptoCurrency")
public class CryptoCurrencyController {

    private final UserRepository userRepository;
    private final CoinLoreClient coinLoreClient;
    private final CryptoCurrencyRepository currencyRepository;
    private final UserCryptoCurrencyRepository userCryptoCurrencyRepository;

    public CryptoCurrencyController(UserRepository userRepository,
                                    CoinLoreClient coinLoreClient,
                                    CryptoCurrencyRepository currencyRepository,
                                    UserCryptoCurrencyRepository userCryptoCurrencyRepository) {
        this.userRepository = userRepository;
        this.coinLoreClient = coinLoreClient;
        this.currencyRepository = currencyRepository;
        this.userCryptoCurrencyRepository = userCryptoCurrencyRepository;
    }

    @PostMapping("/notify")
    public ResponseEntity<?> notify(@RequestBody CryptoCurrencyDTO data) {
        UserCryptoCurrency userCryptoCurrency = new UserCryptoCurrency();
        String usernameDTO = data.getUsername();
        String symbolDTO = data.getSymbol();
        if (userRepository.existsByUsername(usernameDTO)) {
            return userRepository.findByUsername(usernameDTO)
                    .map(userMap -> {
                        if (Objects.equals(userMap.getPassword(), data.getPassword())) {
                            Optional<CryptoCurrency> bySymbol = currencyRepository.findBySymbol(data.getSymbol());
                            if (bySymbol.isPresent()) {
                                if (userCryptoCurrencyRepository.existsByCurrency(userMap.getId(), bySymbol.get().getId()) != null) {
                                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                                }
                            }
                            coinLoreClient.getCurrentPrice(data.getCurrencyId()).ifPresent(price ->
                                    userCryptoCurrency.setPriceCoinLore(price.getPrice()));
                            userRepository.findByUsername(data.getUsername())
                                    .ifPresent(userCryptoCurrency::setUser);
                            currencyRepository.findBySymbol(symbolDTO)
                                    .ifPresent(userCryptoCurrency::setCryptoCurrency);
                            userCryptoCurrencyRepository.save(userCryptoCurrency);
                        }
                        return ResponseEntity.status(HttpStatus.CREATED).body(userCryptoCurrency);
                    })
                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
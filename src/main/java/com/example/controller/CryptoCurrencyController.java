package com.example.controller;

import com.example.domain.CoinLoreResponse;
import com.example.domain.User;
import com.example.domain.UserCryptoCurrency;
import com.example.dto.CryptoCurrencyDTO;
import com.example.repository.CryptoCurrencyRepository;
import com.example.repository.UserCryptoCurrencyRepository;
import com.example.repository.UserRepository;
import com.example.service.CoinLoreClient;
import com.example.service.CryptoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
//@RequestMapping(value = "CryptoCurrency")
public class CryptoCurrencyController {

    private final CryptoService cryptoService;
    private final UserRepository userRepository;
    private final CoinLoreClient coinLoreClient;
    private final CryptoCurrencyRepository currencyRepository;
    private final UserCryptoCurrencyRepository userCryptoCurrencyRepository;

    public CryptoCurrencyController(CryptoService cryptoService,
                                    UserRepository userRepository,
                                    CoinLoreClient coinLoreClient,
                                    CryptoCurrencyRepository currencyRepository,
                                    UserCryptoCurrencyRepository userCryptoCurrencyRepository) {
        this.cryptoService = cryptoService;
        this.userRepository = userRepository;
        this.coinLoreClient = coinLoreClient;
        this.currencyRepository = currencyRepository;
        this.userCryptoCurrencyRepository = userCryptoCurrencyRepository;
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<CryptoCurrency>> findAll() throws CurrenciesNotFoundException {
//        List<CryptoCurrency> currencyList = currencyRepository.findAll();
//        return ResponseEntity.ok(currencyList);
//    }

//    @GetMapping("find/{symbol}")
//    public ResponseEntity<CryptoCurrency> findBySymbol(@PathVariable String symbol) {
//        CryptoCurrency currency = currencyRepository.findBySymbol(symbol.toUpperCase());
//        return ResponseEntity.ok(currency);
//    }


    @PostMapping
    public ResponseEntity<UserCryptoCurrency> notify(@RequestBody CryptoCurrencyDTO data) {
        UserCryptoCurrency userCryptoCurrency = new UserCryptoCurrency();
        if (userRepository.existsByUsername(data.getUsername())) {
            User userSQL = userRepository.findByUsername(data.getUsername());
            if (userSQL.getPassword().equals(data.getPassword())) {
                Long userId = userSQL.getId();
                Long currencyId = currencyRepository.findBySymbol(data.getSymbol()).getId();
                UserCryptoCurrency userCrypto = userCryptoCurrencyRepository.existsByCurrency(userId, currencyId);
                if (userCrypto != null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                Optional<CoinLoreResponse> coinLoreResponse = coinLoreClient.getCurrentPrice(data.getCurrencyId());
                if (coinLoreResponse.isPresent()) {
                    userCryptoCurrency.setPriceCoinLore(coinLoreResponse.get().getPrice());
                    userCryptoCurrency.setUser(userRepository.findByUsername(data.getUsername()));
                    userCryptoCurrency.setCryptoCurrency(currencyRepository.findBySymbol(data.getSymbol()));
                    userCryptoCurrencyRepository.save(userCryptoCurrency);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userCryptoCurrency);
    }
}
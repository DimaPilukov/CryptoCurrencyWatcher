package com.example.controller;

import com.example.domain.CryptoCurrency;
import com.example.domain.User;
import com.example.domain.UserCryptoCurrency;
import com.example.dto.Dto;
import com.example.exception.CurrenciesNotFoundException;
import com.example.repository.CryptoCurrencyRepository;
import com.example.repository.UserCryptoCurrencyRepository;
import com.example.repository.UserRepository;
import com.example.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping(value = "CryptoCurrency")
public class CryptoCurrencyController {

    private final CryptoService cryptoService;
    private final UserRepository userRepository;
    private final CryptoCurrencyRepository currencyRepository;
    private final UserCryptoCurrencyRepository userCryptoCurrencyRepository;

    public CryptoCurrencyController(CryptoService cryptoService,
                                    UserRepository userRepository,
                                    CryptoCurrencyRepository currencyRepository,
                                    UserCryptoCurrencyRepository userCryptoCurrencyRepository) {
        this.cryptoService = cryptoService;
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
        this.userCryptoCurrencyRepository = userCryptoCurrencyRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CryptoCurrency>> findAll() throws CurrenciesNotFoundException {
        List<CryptoCurrency> currencyList = currencyRepository.findAll();
        return ResponseEntity.ok(currencyList);
    }

    @GetMapping("find/{symbol}")
    public ResponseEntity<CryptoCurrency> findBySymbol(@PathVariable String symbol) {
        CryptoCurrency currency = currencyRepository.findBySymbol(symbol.toUpperCase());
        return ResponseEntity.ok(currency);
    }

    @PostMapping
    public void notify(@RequestBody Dto dto) {
        UserCryptoCurrency userCryptoCurrency = new UserCryptoCurrency();
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        if (userRepository.existsByUsername(user.getUsername())) {
            User userSQL = userRepository.findByUsername(user.getUsername());
            if (userSQL.getPassword().equals(user.getPassword())) {
                userCryptoCurrency.setUser(user);
                userCryptoCurrency.setPriceCoinLore(cryptoService.getPRICE(dto.getSymbol()));
                userCryptoCurrency.setCryptoCurrency(currencyRepository.findBySymbol(dto.getSymbol()));
                userCryptoCurrencyRepository.save(userCryptoCurrency);

            }
        }
    }
}

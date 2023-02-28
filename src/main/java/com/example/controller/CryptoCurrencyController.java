package com.example.controller;


import com.example.domain.Currency;
import com.example.exception.CurrencyNotFoundException;
import com.example.exception.CurrenciesNotFoundException;
import com.example.repo.CurrencyRepository;
import com.example.service.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping(value = "CryptoCurrency")
public class CryptoCurrencyController {

    private CryptoService cryptoService;
    private CurrencyRepository currencyRepository;

    public CryptoCurrencyController(CryptoService cryptoService, CurrencyRepository currencyRepository) {
        this.cryptoService = cryptoService;
        this.currencyRepository = currencyRepository;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Currency>> findAll() throws CurrenciesNotFoundException {
        List<Currency> currencyList = currencyRepository.findAll();
        return ResponseEntity.ok(currencyList);
    }

    @GetMapping(value = "find/{symbol}")
    public ResponseEntity<Currency> findBySymbol(@PathVariable String symbol) throws CurrencyNotFoundException {
        Currency currency = cryptoService.findBySymbol(symbol);
        return ResponseEntity.ok(currency);
    }

//    @GetMapping(value = "/{username}/{symbol}")
//    public void notify(@PathVariable String username,
//                       @PathVariable String symbol) {
//        cryptoService.notify(username, symbol);
//    }
}

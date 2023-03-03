package com.example.repository;

import com.example.domain.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {

    public CryptoCurrency findBySymbol(String symbol);

}

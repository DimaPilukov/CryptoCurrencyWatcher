package com.example.repo;

import com.example.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    public Currency findBySymbol(String symbol);
}

package com.example.repository;

import com.example.domain.UserCryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCryptoCurrencyRepository extends JpaRepository<UserCryptoCurrency, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM user_crypto_currency WHERE user_id = ? AND crypto_currency_id = ?")
    UserCryptoCurrency existsByCurrency(Long userId, Long currencyId);

}
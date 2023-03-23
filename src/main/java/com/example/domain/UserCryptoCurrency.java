package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserCryptoCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "crypto_currency_id", referencedColumnName = "id")
    private CryptoCurrency cryptoCurrency;

    private double priceCoinLore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCryptoCurrency that = (UserCryptoCurrency) o;
        return Double.compare(that.priceCoinLore, priceCoinLore) == 0 &&
                Objects.equals(id, that.id) && Objects.equals(user, that.user) &&
                Objects.equals(cryptoCurrency, that.cryptoCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, cryptoCurrency, priceCoinLore);
    }
}
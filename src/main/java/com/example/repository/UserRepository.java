package com.example.repository;

import com.example.domain.CryptoCurrency;
import com.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    public User findByUsername(String username);
}

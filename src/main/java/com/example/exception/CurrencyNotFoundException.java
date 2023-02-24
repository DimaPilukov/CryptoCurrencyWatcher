package com.example.exception;

public class CurrencyNotFoundException extends Exception {

    public CurrencyNotFoundException() {
        super("Coin not found");
    }

}

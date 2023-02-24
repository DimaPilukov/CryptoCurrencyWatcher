package com.example.exception;

public class CurrenciesNotFoundException extends Exception {

    public CurrenciesNotFoundException() {
        super("Coins not found");
    }

}

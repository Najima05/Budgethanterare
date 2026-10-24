package com.example.java26;

// Kontrollerar om en transaktion är ogiltigt

public class InvalidTransactionException extends Exception {
    public InvalidTransactionException(String meddelande) {
        super(meddelande);
    }
}

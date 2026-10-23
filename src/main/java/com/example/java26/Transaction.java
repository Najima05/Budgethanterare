package com.example.java26;

import java.time.LocalDate;

public record Transaction(
        LocalDate datum,
        String kategori,
        double belopp,
        TransactionType type

) {
}

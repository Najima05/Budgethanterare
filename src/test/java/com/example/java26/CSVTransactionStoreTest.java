package com.example.java26;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CSVTransactionStoreTest {

    // Test 1

    @Test
    void toCsv() {
        Transaction transaction = new Transaction(LocalDate.of(2026,10,23),
                "Mat", 300.0, TransactionType.UTGIFT);

        // Kör metoden
        String result = CSVTransactionStore.toCsv(transaction);

        // Kontrollera resultatet
        assertEquals("2026-10-23,Mat,300.0,UTGIFT", result);
    }

    // Test 2

    @Test
    void fromCsv() throws FileFormatException {
        String csv = "2026-10-23, Mat, 300.0, UTGIFT";

        // Kör metoden
        Transaction result = CSVTransactionStore.fromCsv(csv);

        // Kontrollera resultatet
        assertEquals(LocalDate.of(2026, 10,23),
                result.datum());
        assertEquals("Mat", result.kategori());
        assertEquals(300.0, result.belopp(), 0.0001);
        assertEquals(TransactionType.UTGIFT, result.type());
    }

    // Test 3

    @Test
    void fromCsv_invalidData_throwException() {
        String csv = "felaktigt datum,Mat,300.0, UTGIFT";

        // Kontrollera att ett undantag kastas
        assertThrows(FileFormatException.class, () -> CSVTransactionStore.fromCsv(csv));
    }
}
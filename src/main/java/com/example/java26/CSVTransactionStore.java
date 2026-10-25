package com.example.java26;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class CSVTransactionStore {

    public static String toCsv(Transaction transaction) {
        return transaction.datum() + "," +
                transaction.kategori() + "," +
                transaction.belopp() + "," +
                transaction.type();
    }

    public static Transaction fromCsv(String line) throws FileFormatException {
        if (line == null || line.trim().isEmpty()) {
            throw new FileFormatException("Raden är tom.");
        }
        String[] parts = line.split(",",-1);
        if (parts.length != 4) {
            throw new FileFormatException(
                    "Ogiltigt antal fält i CSV-raden. Förväntade 4 fält, fick " + parts.length);
        }
        try {
            LocalDate datum = LocalDate.parse(parts[0].trim());
            String kategori = parts[1].trim();
            if (kategori.isEmpty()) {
                throw new FileFormatException(
                        "Kategori kan inte vara tom.");
            }

            double belopp = Double.parseDouble(parts[2].trim());
            if (!Double.isFinite(belopp) || belopp <=0) {
                throw new FileFormatException(
                        "Belopp måste vara ett positivt tal.");
            }
            TransactionType type;
            try {
                type = TransactionType.valueOf(parts[3].trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new FileFormatException(
                        "Ogiltig transaktionstyp: " + parts[3], e);
            }
            return new Transaction(datum, kategori, belopp, type);
        } catch (DateTimeParseException e) {
            throw new FileFormatException(
                    "Ogiltigt datumformat: " + parts[0], e);
        } catch (NumberFormatException e) {
            throw new FileFormatException(
                    "Ogiltigt belopp: " + parts[2], e);
        }
    }
}

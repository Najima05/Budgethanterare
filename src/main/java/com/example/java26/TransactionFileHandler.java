package com.example.java26;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionFileHandler {
    private static final Logger LOGGER = Logger.getLogger(TransactionFileHandler.class.getName());

    public List<Transaction> loadFromFile(Path filePath) throws FileFormatException, IOException {
        List<Transaction> transactions = new ArrayList<>();

        if (!Files.exists(filePath)) {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            LOGGER.info("Filen saknas, skapar ny fil: " + filePath.toAbsolutePath());
            Files.createFile(filePath);
            return transactions;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    transactions.add(CSVTransactionStore.fromCsv(line));
                } catch (FileFormatException e) {
                    LOGGER.log(Level.SEVERE,
                            "Fel vid parsning av rad " + lineNumber + " i filen " + filePath, e);
                    throw new FileFormatException("Fel på rad " + lineNumber + " : " + e.getMessage(), e);
                }
            }
        }
        LOGGER.info("Läste in " + transactions.size() + " transaktioner från " + filePath);
        return transactions;
    }

    public void saveToFile(Path filePath, List<Transaction> transactions) throws IOException {
        LOGGER.info("Sparar " + transactions.size() + " transaktioner till " + filePath.toAbsolutePath());
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            for (Transaction transaction : transactions) {
                writer.write(CSVTransactionStore.toCsv(transaction));
                writer.newLine();
            }
        }
    }
}

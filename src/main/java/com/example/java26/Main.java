package com.example.java26;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final Path FILE_PATH = Paths.get("transaction.csv");

    public static void main(String[] args) {
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        TransactionFileHandler fileHandler = new TransactionFileHandler();

        try {
            List<Transaction> savedTransactions = fileHandler.loadFromFile(FILE_PATH);

            for (Transaction transaction : savedTransactions) {
                budgetService.add(transaction);
            }
            IO.println("Transaktioner har lästs in");
        } catch (FileFormatException e) {
            LOGGER.log(Level.SEVERE, "Kunde inte läsa transaktionsfilens format.", e);
            IO.println("Fel i CSV_filen: " + e.getMessage());
            IO.println("Kontrollera filen innan du startar om programmet.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Kunde inte läsa transaktionsfilen: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            showMenu();
            String choice = IO.readln("Välj ett alternativ: ").trim();

            switch (choice.toLowerCase()) {
                case "1" -> addTransaction(budgetService);
                case "2" -> showAllTransactions(budgetService);
                case "3" -> showASummary(budgetService);
                case "4" -> filterTransactions(budgetService);
                case "5" -> saveTransactions(budgetService, fileHandler);
                case "e" -> {
                    saveTransactions(budgetService, fileHandler);
                    IO.println("Programmet avslutas.");
                    running = false;
                }
                default -> IO.println("Ogiltigt val. Välj mellan 1 och e.");
            }
        }
    }

    private static void showMenu() {
        IO.println("\n==========================");
        IO.println("    Budgethanterare     ");
        IO.println("1. Lägg till en transaktion");
        IO.println("2. Visa alla transaktioner");
        IO.println("3. Visa saldo och kategorisammanställning");
        IO.println("4. Filtrera transaktioner");
        IO.println("5. Spara till fil");
        IO.println("e. Avsluta");
    }

    private static void addTransaction(BudgetService budgetService) {
        try {
            String dateInput = IO.readln("Datum (YYYY-MM-DD): ").trim();
            LocalDate date = LocalDate.parse(dateInput);

            String category = IO.readln("Kategori: ").trim();
            if (category.isEmpty()) {
                throw new InvalidTransactionException("Kategori kan inte vara tom.");
            }

            String amountInput = IO.readln("Belopp: ").trim().replace(",", ".");
            double amount = Double.parseDouble(amountInput);
            if (!Double.isFinite(amount) || amount <= 0) {
                throw new InvalidTransactionException(
                        "Belopp måste vara ett positivt tal."
                );
            }
            String typeInput = IO.readln("Typ (INKOMST eller UTGIFT):  ").trim();
            TransactionType type;

            try {
                type = TransactionType.valueOf(typeInput.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new InvalidTransactionException(
                        "Typen måste vara antingen INKOMST eller UTGIFT.");
            }
            Transaction transaction = new Transaction(date, category, amount, type);

            budgetService.add(transaction);
            IO.println("Transaktionen är inlagts.");

        } catch (DateTimeParseException e) {
            IO.println("Ogiltigt datum. Använd formatet YYYY-MM-DD");
        } catch (NumberFormatException e) {
            IO.println("Ogiltigt belopp. Ange ett tal.");
        } catch (InvalidTransactionException e) {
            IO.println("Ogiltitgt transaktion: " + e.getMessage());
        }
    }

    private static void showAllTransactions(BudgetService budgetService) {
        List<Transaction> transactions = budgetService.sortByDate();

        if (transactions.isEmpty()){
            IO.println("Det finns inga transaktioner.");
            return;
        }
        IO.println("Alla transaktioner:");

        for (Transaction transaction : transactions) {
            printTransaction(transaction);
        }
    }

    private static void showASummary(BudgetService budgetService) {
        double total = budgetService.calculateTotal();

        IO.println("Sammafattning");
        IO.println("Totalt saldo: " + total + "kr");

        Map<String, Double> categories =
                budgetService.calculateByCategory();

        IO.println("\n Saldo per kategoru: ");

        if (categories.isEmpty()) {
            IO.println("Det finns inga kategorier att visa.");
            return;
        }
        for (Map.Entry<String, Double> entry : categories.entrySet()) {
            IO.println(entry.getKey() + ":" + entry.getValue() + "kr");
        }
    }

    private static void filterTransactions(BudgetService budgetService) {
        IO.println("Filtrera transaktioner:");
        IO.println("1. Filtrera efter datumintervall");
        IO.println("2. Fitlrera efter typ");

        String choice = IO.readln("Välj filter: ").trim();

        switch (choice) {
            case "1" -> filterByDate(budgetService);
            case "2" -> filterbyType(budgetService);
            default -> IO.println("Ogiltigt filterval");
        }
    }

    private static void filterByDate(BudgetService budgetService) {

        try {
            LocalDate start = LocalDate.parse(
                    IO.readln("Stardatum (YYYY-MM-DD): ").trim());

            LocalDate end = LocalDate.parse(
                    IO.readln("Slutdatum (YYYY-MM-DD): ").trim());

            List<Transaction> filtered = budgetService.filterByDateRange(start, end);

            printTransactions(filtered);
        } catch (DateTimeParseException e) {
            IO.println("Ogiltigt datum. Använd formatet YYYY-MM-DD");

        } catch (IllegalArgumentException e) {
            IO.println("Ogiltigt datumintervall: " + e.getMessage());
        }
    }

    private static void filterbyType(BudgetService budgetService) {
        String input = IO.readln("Typ (INKOMST eller UTGIFT): ").trim();
        try {
            TransactionType type = TransactionType.valueOf(
                    input.toUpperCase(Locale.ROOT));

            List<Transaction> filtered = budgetService.filterByType(type);

            printTransactions(filtered);
        } catch (IllegalArgumentException e) {
            IO.println("Ogigltigt typ. Ange antingen INKOMST eller UTGIFT. ");
        }
    }

    private static void printTransactions(List<Transaction> transactions) {

        if (transactions.isEmpty()) {
            IO.println("Inga transaktioner hittades.");
            return;
        }
        for (Transaction transaction : transactions) {
            printTransaction(transaction);
        }
    }

    private static void printTransaction(Transaction transaction) {
        IO.println(
                "Datum: " + transaction.datum()
                + " | Kategori: " + transaction.kategori()
                + " | Belopp: " + transaction.belopp() + "kr"
                + " | Typ: " + transaction.type()
        );

    }

    private static void saveTransactions(BudgetService budgetService, TransactionFileHandler fileHandler) {

        try {
            fileHandler.saveToFile(
                    FILE_PATH,
                    budgetService.findAll());
        IO.println("Sparats transaktioner ");

    } catch (IOException e) {
            LOGGER.log(Level.SEVERE,
                    "Kunde inte spara transaktioner.", e);
            IO.println("Kunde inte spara filen: " + e.getMessage());
        }
    }
}

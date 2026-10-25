package com.example.java26;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BudgetServiceTest {

    // Test 1

    @Test
    void calculateTotal() {
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(
                LocalDate.of(2026, 10,20), "Lön", 4000,
                TransactionType.INKOMST));

        repository.add(new Transaction(
                LocalDate.of(2026, 10, 20), "Mat", 300,
                TransactionType.UTGIFT));
        BudgetService service = new BudgetService(repository);

        // Kör metoden
        double result = service.calculateTotal();


        // Kontrollera resultatet
        assertEquals(3700.0, result, 0.0001);
    }

    // Test 2

    @Test
    void calculateByCategory() {
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(
                LocalDate.of(2026, 10, 20), "Mat", 300,
                TransactionType.UTGIFT));
        BudgetService service = new BudgetService(repository);

        // Kör metoden
        Map<String, Double> result = service.calculateByCategory();

        // Kontrollera resultatet
        assertEquals(-300.0, result.get("Mat"), 0.0001);
    }

    // Test 3

    @Test
    void filterByType() {
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(
                LocalDate.of(2026, 10, 20), "Mat", 300,
                TransactionType.UTGIFT));
        repository.add(new Transaction(
                LocalDate.of(2026, 10, 20), "Lön", 30000,
                TransactionType.INKOMST));
        BudgetService service = new BudgetService(repository);

        // Kör metoden
        List<Transaction> result =
                service.filterByType(TransactionType.UTGIFT);

        // Kontrollera resultatet
        assertEquals(1, result.size());
        assertEquals(TransactionType.UTGIFT, result.get(0).type());
    }

    // Test 4

    @Test
    void filterByDateRange() {
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(
                LocalDate.of(2026, 10, 20), "Mat", 300,
                TransactionType.UTGIFT));
        BudgetService service = new BudgetService(repository);

        // Kör metoden
        List<Transaction> result = service.filterByDateRange(
                LocalDate.of(2026, 10, 20),
                LocalDate.of(2026, 10, 20));

        // Kontrollera resultatet
        assertEquals(1, result.size());
    }

    // Test 5

    @Test
    void sortByDate() {
        Repository<Transaction> repository = new Repository<>();
        Transaction senare = new Transaction(
                LocalDate.of(2026, 10, 20), "Lön", 30000,
                TransactionType.INKOMST);
        Transaction tidigare = new Transaction(
                LocalDate.of(2026, 10, 15), "Mat", 300,
                TransactionType.UTGIFT);
        repository.add(senare);
        repository.add(tidigare);
        BudgetService service = new BudgetService(repository);

        // Kör metoden
        List<Transaction> result = service.sortByDate();

        // Kontrollera resultatet
        assertEquals(tidigare, result.get(0));
        assertEquals(senare, result.get(1));
    }

    // Test 6

    @Test
    void findAll() {
        Repository<Transaction> repository = new Repository<>();
        Transaction transaction = new Transaction(
                LocalDate.of(2026, 9, 1), "Mat", 300,
                TransactionType.UTGIFT);
        repository.add(transaction);
        BudgetService service = new BudgetService(repository);

        // Kör metoden
        List<Transaction> result = service.findAll();

        // Kontrollera resultatet
        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));
    }
}

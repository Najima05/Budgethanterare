package com.example.java26;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class BudgetService {
    private static final Logger LOGGER = Logger.getLogger(BudgetService.class.getName());
    private final Repository<Transaction> repository;

    public BudgetService(Repository<Transaction> repository) {
        this.repository = repository;
    }

    // Lägga till och hämta transaktioner
    public void add(Transaction transaction) {
        repository.add(transaction);
        LOGGER.info("Transaktion tillagd: " + transaction);
    }

    public List<Transaction> findAll() {
        LOGGER.fine("Hämtar alla transaktioner.");
        return repository.findAll();
    }

    // Beräkna den totala saldo

    public double calculateTotal() {
        LOGGER.fine("Beräknar det totala saldot.");
        double income = calculateTotalByType(TransactionType.INKOMST);
        double expense = calculateTotalByType(TransactionType.UTGIFT);
        return income - expense;
    }

    private double calculateTotalByType(TransactionType type) {
        return repository.findWhere(t -> t.type() == type)
                .stream()
                .mapToDouble(Transaction::belopp)
                .sum();
    }

    // Sammanställning per kategori
    public Map<String, Double> calculateByCategory() {
        LOGGER.fine("Beräknar summan per kategori.");

        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Transaction::kategori,
                        TreeMap::new,
                        Collectors.summingDouble(transaction ->
                                transaction.type() == TransactionType.INKOMST
                                ? transaction.belopp()
                                : -transaction.belopp())
                ));
    }
    public List<Transaction> filterByDateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("ogiltigt datumintervall.");
        }
        LOGGER.info("Filtrering på datum: " + start + " och " + end);
        return repository.findWhere(t ->
                (t.datum().isEqual(start) || t.datum().isAfter(start)) &&
                        (t.datum().isEqual(end) || t.datum().isBefore(end)));
    }

    public List<Transaction> filterByType(TransactionType type) {
        LOGGER.fine("Filtrering på typ:" + type);
        return repository.findWhere(t -> t.type() == type);

    }
    public List<Transaction> sortByDate() {
        LOGGER.fine("Sorterar transaktioner efter datum.");

        return repository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::datum))
                .toList();
    }
}

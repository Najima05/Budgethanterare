package com.example.java26;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    // Test 1

    @Test
    void add() {
        Repository<String> repository = new Repository<>();

        // Kör metoden
        repository.add("Mat");

        // Kontrollera resultatet
        assertEquals(1, repository.findAll().size());
    }

    // Test 2

    @Test
    void findAll() {
        Repository<String> repository = new Repository<>();
        repository.add("Mat");
        repository.add("Hyra");

        // Kör metoden
        List<String> result = repository.findAll();

        // Kontrollera resultatet
        assertEquals(2, result.size());
        assertTrue(result.contains("Mat"));
        assertTrue(result.contains("Hyra"));
    }

    // Test 3

    @Test
    void findWhere() {
        Repository<Integer> repository = new Repository<>();
        repository.add(10);
        repository.add(20);
        repository.add(30);

        // Kör metoden
        List<Integer> result = repository.findWhere(
                number -> number > 10);

        // Kontrollera resultatet
        assertEquals(2, result.size());
        assertTrue(result.contains(20));
        assertTrue(result.contains(30));
    }

    // Test 4

    @Test
    void addNull_throwException() {
        Repository<String> repository = new Repository<>();
        // Kontrollera att ett undantag kastas
        assertThrows(IllegalArgumentException.class, () -> repository.add(null));
    }
}

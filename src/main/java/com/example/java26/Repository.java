package com.example.java26;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;



public class Repository<T> {

    private final ArrayList<T> items = new ArrayList<>();

    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item  can not be null");
        }
        items.add(item);
    }

    public List<T> findAll() {
        return new ArrayList<> (items);
    }

    public List<T> findWhere(Predicate<T> condition) {
        return new ArrayList<>(items.stream().filter(condition).toList());
    }
}


//Gerics klass alltså , vi ska lagrara objekt av valfri T i en collection -- ArrayList<T>
    // och exponerar generiska metoder som add(T item , findAll() eller
    //  eller findWhere(Predicate<T>, vilkor för filtering med lamba




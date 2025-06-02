package com.reactive.learning.spring_webflux_consumer.pojo;

public class Book {
    private String id;
    private String name;

    public Book(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
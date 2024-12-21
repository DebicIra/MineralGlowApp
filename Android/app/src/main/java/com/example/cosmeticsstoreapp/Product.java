package com.example.cosmeticsstoreapp;

public class Product {
    private final String name;
    private final String price;
    private final String description;
    private final String composition;
    private final String imageUrl;
    private final int id;
    public Product(String name, String price, String description, String composition, String imageUrl, int id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.composition = composition;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDesc() {
        return description;
    }

    public String getComposition() {
        return composition;
    }
    public int getId() {
        return id;
    }
}

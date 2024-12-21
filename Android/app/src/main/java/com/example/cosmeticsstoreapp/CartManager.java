package com.example.cosmeticsstoreapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Product> cartProducts;

    private CartManager() {
        cartProducts = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProductToCart(Product product) {
        cartProducts.add(product);
    }

    public List<Product> getCartProducts() {
        return cartProducts;
    }

    public void clearCart() {
        cartProducts.clear();
    }

    public int getTotalPrice() {
        int total = 0;
        for (Product product : cartProducts) {
            total += Integer.parseInt(product.getPrice());
        }
        return total;
    }
}

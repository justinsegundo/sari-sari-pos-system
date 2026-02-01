package com.sarisari.pos.service;

import com.sarisari.pos.model.Product;

import java.util.*;

public class ProductService {

    private final Map<String, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getName(), product);
    }

    public Product getProduct(String name) {
        return products.get(name.toLowerCase());
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Product searchProduct(String keyword) {
        keyword = keyword.toLowerCase();
        for (Product p : products.values()) {
            if (p.getName().toLowerCase().contains(keyword)) {
                return p;
            }
        }
        return null;
    }

    public void updateProduct(String name, double price, int stock) {
        Product product = getProduct(name);
        if (product != null) {
            product.setPrice(price);
            product.setStock(stock);
        }
    }

    public void deleteProduct(String name) {
        products.remove(name.toLowerCase());
    }

}

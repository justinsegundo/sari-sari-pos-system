package com.sarisari.pos.model;

public class Product {

    private final String name;
    private double price;
    private int stock;

    public Product(String name, double price, int stock) {
        this.name = name.toLowerCase();
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return name + "," + price + "," + stock;
    }
}

package com.sarisari.pos.service;

import com.sarisari.pos.model.Product;

import java.io.*;
import java.util.List;

public class FileStorageService {

    private final String filename;

    public FileStorageService(String filename) {
        this.filename = filename;
    }

    public void save(List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                writer.println(p);
            }
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    public void load(ProductService productService) {
        File file = new File(filename);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                productService.addProduct(
                        new Product(
                                parts[0],
                                Double.parseDouble(parts[1]),
                                Integer.parseInt(parts[2])
                        )
                );
            }
        } catch (IOException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }
}

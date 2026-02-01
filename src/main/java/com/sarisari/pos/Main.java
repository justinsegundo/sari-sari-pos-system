package com.sarisari.pos;

import com.sarisari.pos.service.FileStorageService;
import com.sarisari.pos.service.ProductService;
import com.sarisari.pos.ui.MainFrame;

import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ProductService productService = new ProductService();
        FileStorageService storage = new FileStorageService("products.txt");

        storage.load(productService);

        if (productService.getAllProducts().isEmpty()) {
            System.out.println("No products yet. Please add products using the Manage Products tab.");
        }
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(productService);
            frame.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            storage.save(new ArrayList<>(productService.getAllProducts()));
        }));
    }
}

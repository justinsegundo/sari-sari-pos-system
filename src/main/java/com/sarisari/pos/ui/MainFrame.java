package com.sarisari.pos.ui;

import com.sarisari.pos.service.ProductService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    ImageIcon priceIcon = new ImageIcon(getClass().getResource("/icons/price.png"));
    ImageIcon productsIcon = new ImageIcon(getClass().getResource("/icons/products.png"));
    public MainFrame(ProductService productService) {

        setTitle("Sari-Sari POS System");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(new Color(245, 245, 245));
        tabs.setForeground(new Color(40, 40, 40));


        tabs.addTab("Price Checker", priceIcon, new PriceCheckerPanel(productService));
        tabs.addTab("Manage Products", productsIcon, new ProductManagerPanel(productService));


        tabs.setTabPlacement(JTabbedPane.TOP);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(tabs, BorderLayout.CENTER);

        add(mainPanel);
    }
}
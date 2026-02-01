package com.sarisari.pos.ui;

import com.sarisari.pos.model.Product;
import com.sarisari.pos.service.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PriceCheckerPanel extends JPanel {

    private final ProductService productService;
    private final JTextField searchField;
    private final JLabel resultLabel;
    private final JPanel resultPanel;
    private final ImageIcon successIconImg;
    private final ImageIcon errorIconImg;
    private final ImageIcon warningIconImg;
    private final ImageIcon searchIconImg;


    public PriceCheckerPanel(ProductService productService) {
        this.productService = productService;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Price Checker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));


        successIconImg = new ImageIcon(
                new ImageIcon(getClass().getResource("/icons/success.png"))
                        .getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)
        );
        errorIconImg = new ImageIcon(
                new ImageIcon(getClass().getResource("/icons/error.png"))
                        .getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)
        );
        warningIconImg = new ImageIcon(
                new ImageIcon(getClass().getResource("/icons/warning.png"))
                        .getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)
        );
        searchIconImg = new ImageIcon(
                new ImageIcon(getClass().getResource("/icons/search-icon.png"))
                        .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)
        );


        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel searchLabel = new JLabel("Enter Product Name:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(60, 60, 60));
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);


        searchField.addActionListener(e -> searchProduct());

        JButton searchButton = new JButton("Search Price", searchIconImg);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setOpaque(true);
        searchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        searchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> searchProduct());


        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(0, 105, 217));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(0, 123, 255));
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        searchPanel.add(searchField);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        searchPanel.add(searchButton);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        resultLabel = new JLabel("Enter a product name to check its price", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        resultLabel.setForeground(new Color(120, 120, 120));

        resultPanel.add(resultLabel, BorderLayout.CENTER);

        JPanel centerContainer = new JPanel(new GridLayout(2, 1, 15, 15));
        centerContainer.setBackground(new Color(245, 245, 245));
        centerContainer.add(searchPanel);
        centerContainer.add(resultPanel);

        add(title, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);
    }

    private void searchProduct() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            updateResultPanel("Please enter a product keyword", new Color(220, 53, 69), warningIconImg);
            return;
        }

        Product product = productService.searchProduct(keyword);

        if (product != null) {
            displayProductInfo(product);
        } else {
            updateResultPanel("No matching product found!", new Color(220, 53, 69), errorIconImg);
        }
    }

    private void displayProductInfo(Product product) {
        resultPanel.removeAll();
        resultPanel.setBackground(new Color(240, 252, 245));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 167, 69), 2, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 252, 245));

        JLabel successIconLabel = new JLabel(successIconImg, SwingConstants.CENTER);
        successIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel productName = new JLabel(product.getName(), SwingConstants.CENTER);
        productName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        productName.setForeground(new Color(40, 40, 40));
        productName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel detailsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        detailsPanel.setBackground(new Color(240, 252, 245));
        detailsPanel.setMaximumSize(new Dimension(500, 80));

        JPanel priceBox = createInfoBox("Price", "₱" + String.format("%.2f", product.getPrice()),
                new Color(0, 123, 255));

        String stockText = product.getStock() + " units";
        Color stockColor = product.getStock() > 10 ? new Color(40, 167, 69) : new Color(255, 193, 7);
        JPanel stockBox = createInfoBox("Stock", stockText, stockColor);

        detailsPanel.add(priceBox);
        detailsPanel.add(stockBox);

        infoPanel.add(successIconLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(productName);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(detailsPanel);

        resultPanel.add(infoPanel, BorderLayout.CENTER);
        resultPanel.revalidate();
        resultPanel.repaint();
    }


    private JPanel createInfoBox(String label, String value, Color accentColor) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel labelText = new JLabel(label, SwingConstants.CENTER);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelText.setForeground(new Color(100, 100, 100));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueText = new JLabel(value, SwingConstants.CENTER);
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueText.setForeground(accentColor);
        valueText.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(labelText);
        box.add(Box.createRigidArea(new Dimension(0, 5)));
        box.add(valueText);

        return box;
    }

    private void updateResultPanel(String message, Color color, ImageIcon icon) {
        resultPanel.removeAll();
        resultPanel.setBackground(new Color(255, 245, 245));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(new Color(255, 245, 245));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        messageLabel.setForeground(color);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messagePanel.add(iconLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        messagePanel.add(messageLabel);

        resultPanel.add(messagePanel, BorderLayout.CENTER);
        resultPanel.revalidate();
        resultPanel.repaint();
    }

}
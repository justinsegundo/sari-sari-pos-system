package com.sarisari.pos.ui;

import com.sarisari.pos.model.Product;
import com.sarisari.pos.service.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class ProductManagerPanel extends JPanel {

    private final ProductService productService;
    private final DefaultTableModel tableModel;
    private final JTable productTable;

    private final ImageIcon addIcon;
    private final ImageIcon editIcon;
    private final ImageIcon deleteIcon;

    public ProductManagerPanel(ProductService productService) {
        this.productService = productService;

        addIcon = loadIcon("/icons/plus.png");
        editIcon = loadIcon("/icons/edit.png");
        deleteIcon = loadIcon("/icons/delete.png");

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Product Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel subtitle = new JLabel("Manage your inventory", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(120, 120, 120));

        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Product Name", "Price (₱)", "Stock", "Edit", "Delete"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        productTable.setRowHeight(40);
        productTable.setShowVerticalLines(true);
        productTable.setGridColor(new Color(200, 200, 200));
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setSelectionBackground(new Color(220, 237, 255));
        productTable.setSelectionForeground(new Color(40, 40, 40));

        JTableHeader header = productTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );
                label.setFont(new Font("Segoe UI", Font.BOLD, 15));
                label.setBackground(new Color(0, 123, 255));
                label.setForeground(Color.WHITE);
                label.setOpaque(true);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        productTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 15, 0, 0));
        productTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);

        productTable.setDefaultRenderer(Object.class, new EnhancedStripeRenderer());

        productTable.getColumnModel().getColumn(3).setCellRenderer(new EditButtonRenderer());
        productTable.getColumnModel().getColumn(3).setCellEditor(new EditButtonEditor());

        productTable.getColumnModel().getColumn(4).setCellRenderer(new DeleteButtonRenderer());
        productTable.getColumnModel().getColumn(4).setCellEditor(new DeleteButtonEditor());

        productTable.getColumnModel().getColumn(0).setPreferredWidth(280);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        refreshTable();

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder()
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton addButton = new JButton("Add Product", addIcon);
        styleButton(addButton, new Color(40, 167, 69), new Color(33, 136, 56));
        addButton.addActionListener(e -> addProduct());
        buttonPanel.add(addButton);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) return null;
        return new ImageIcon(url);
    }

    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(170, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Product p : productService.getAllProducts()) {
            tableModel.addRow(new Object[]{
                    p.getName(),
                    String.format("%.2f", p.getPrice()),
                    p.getStock(),
                    "",
                    ""
            });
        }
        tableModel.fireTableDataChanged();
    }

    private void addProduct() {
        JPanel dialogPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        dialogPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        dialogPanel.add(new JLabel("Product Name:"));
        dialogPanel.add(nameField);
        dialogPanel.add(new JLabel("Price (₱):"));
        dialogPanel.add(priceField);
        dialogPanel.add(new JLabel("Stock:"));
        dialogPanel.add(stockField);

        int option = JOptionPane.showConfirmDialog(
                this,
                dialogPanel,
                "Add New Product",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                productService.addProduct(new Product(name, price, stock));
                refreshTable();
                showSuccessDialog("Product added successfully!");
            } catch (Exception ex) {
                showErrorDialog("Invalid input values.");
            }
        }
    }

    private void editProduct(String name) {
        Product product = productService.getProduct(name);
        if (product == null) return;

        JPanel dialogPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        dialogPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        JTextField stockField = new JTextField(String.valueOf(product.getStock()));

        dialogPanel.add(new JLabel("New Price (₱):"));
        dialogPanel.add(priceField);
        dialogPanel.add(new JLabel("New Stock:"));
        dialogPanel.add(stockField);

        int option = JOptionPane.showConfirmDialog(
                this,
                dialogPanel,
                "Edit Product - " + name,
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                double newPrice = Double.parseDouble(priceField.getText().trim());
                int newStock = Integer.parseInt(stockField.getText().trim());
                productService.updateProduct(name, newPrice, newStock);
                refreshTable();
                showSuccessDialog("Product updated successfully!");
            } catch (Exception ex) {
                showErrorDialog("Invalid values entered.");
            }
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    static class EnhancedStripeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column
        ) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
            }
            setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));
            setHorizontalAlignment(column == 0 ? LEFT : CENTER);
            return this;
        }
    }

    class EditButtonRenderer extends JButton implements TableCellRenderer {
        public EditButtonRenderer() {
            setIcon(editIcon);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setBackground(row % 2 == 0 ? new Color(255, 193, 7) : new Color(245, 180, 0));
            if (isSelected) setBackground(new Color(220, 237, 255));
            return this;
        }
    }

    class EditButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String productName;
        public EditButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setIcon(editIcon);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 13));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                editProduct(productName);
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            productName = table.getValueAt(row, 0).toString();
            button.setBackground(new Color(255, 193, 7));
            return button;
        }
        @Override
        public Object getCellEditorValue() { return null; }
    }

    class DeleteButtonRenderer extends JButton implements TableCellRenderer {
        public DeleteButtonRenderer() {
            setIcon(deleteIcon);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setBackground(row % 2 == 0 ? new Color(220, 53, 69) : new Color(200, 35, 50));
            if (isSelected) setBackground(new Color(255, 102, 102));
            return this;
        }
    }

    class DeleteButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String productName;
        public DeleteButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setIcon(deleteIcon);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 13));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(
                        ProductManagerPanel.this,
                        "Are you sure you want to delete " + productName + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    productService.deleteProduct(productName);
                    refreshTable();
                }
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            productName = table.getValueAt(row, 0).toString();
            button.setBackground(new Color(220, 53, 69));
            return button;
        }
        @Override
        public Object getCellEditorValue() { return null; }
    }

}

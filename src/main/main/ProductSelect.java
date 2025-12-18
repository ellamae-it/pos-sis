package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductSelect extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField quantityField;
    private CashierDashboard cashierFrame; // Reference to the main cashier window
    
    public DbConnect dbCon = new DbConnect();
    
    /**
     * Create the frame.
     */
    public ProductSelect(CashierDashboard cashier) {
        this.cashierFrame = cashier;
        
        setTitle("Select Product from Inventory");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 450);
        setLocationRelativeTo(cashier);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblTitle = new JLabel("Available Products");
        lblTitle.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
        lblTitle.setBounds(20, 10, 200, 25);
        contentPane.add(lblTitle);
        
        // Product list table
        JScrollPane productScrollPane = new JScrollPane();
        productScrollPane.setBounds(20, 40, 550, 250);
        contentPane.add(productScrollPane);
        
        String[] productColumns = {"Product ID", "Product Name", "Price (₱)", "Available Stock"};
        productTableModel = new DefaultTableModel(productColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(productTableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productScrollPane.setViewportView(productTable);
        
        // Load products
        loadProducts();
        
        // Quantity input section
        JPanel quantityPanel = new JPanel();
        quantityPanel.setBackground(new Color(230, 240, 250));
        quantityPanel.setBounds(20, 300, 550, 100);
        quantityPanel.setLayout(null);
        contentPane.add(quantityPanel);
        
        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
        lblQuantity.setBounds(20, 20, 80, 25);
        quantityPanel.add(lblQuantity);
        
        quantityField = new JTextField();
        quantityField.setText("1");
        quantityField.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
        quantityField.setBounds(100, 20, 100, 25);
        quantityPanel.add(quantityField);
        quantityField.setColumns(10);
        
        // Buttons
        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 12));
        btnAddToCart.setBounds(220, 20, 120, 25);
        btnAddToCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSelectedToCart();
            }
        });
        quantityPanel.add(btnAddToCart);
        
        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 12));
        btnClose.setBounds(350, 20, 100, 25);
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        quantityPanel.add(btnClose);
        setLocationRelativeTo(null);
    }
    
    private void loadProducts() {
        productTableModel.setRowCount(0);
        
        dbCon.connect();
        
        try {
            String query = "SELECT product_id, product_name, product_price, product_stocks " +
                          "FROM products " +
                          "WHERE product_stocks > 0 " +
                          "AND product_status = 'listed' "+
                          "ORDER BY product_name";
            
            PreparedStatement pstmt = dbCon.con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            int rowCount = 0;
            while (rs.next()) {
                Object[] rowData = {
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    String.format("₱%.2f", rs.getDouble("product_price")),
                    rs.getInt("product_stocks")
                };
                productTableModel.addRow(rowData);
                rowCount++;
            }
            
            setTitle("Select Product from Inventory - " + rowCount + " items available");
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading products: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void addSelectedToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a product!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Quantity must be greater than 0!",
                    "Invalid Quantity",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Get product details
            int productId = (int) productTableModel.getValueAt(selectedRow, 0);
            String productName = (String) productTableModel.getValueAt(selectedRow, 1);
            
            // Get price (remove ₱ symbol)
            String priceStr = (String) productTableModel.getValueAt(selectedRow, 2);
            priceStr = priceStr.replace("₱", "");
            double price = Double.parseDouble(priceStr);
            
            int availableStock = (int) productTableModel.getValueAt(selectedRow, 3);
            
            // Check stock
            if (quantity > availableStock) {
                JOptionPane.showMessageDialog(this,
                    "Insufficient stock! Only " + availableStock + " available.",
                    "Stock Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Add to cart in the main Cashier window
            if (cashierFrame != null) {
                cashierFrame.addToCart(productId, productName, price, quantity);
            }
            
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid quantity!",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
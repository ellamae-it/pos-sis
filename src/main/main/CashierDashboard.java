package main;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;

public class CashierDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField cashTextField;
    private JLabel cashierNameLabel;
    private JLabel totalAmountLabel;
    private JLabel changeLabel;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    public DbConnect dbCon = new DbConnect();
    private double cartTotal = 0.0;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CashierDashboard frame = new CashierDashboard(new UserAccount());
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CashierDashboard(UserAccount user) {
        int cashierUserId = user.getUserId();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 550);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(253, 245, 230));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        //left
        JPanel panel = new JPanel();
        panel.setBackground(new Color(238, 232, 170));
        panel.setBounds(31, 49, 171, 477);
        panel.setPreferredSize(new Dimension(180, 0));
        contentPane.add(panel, BorderLayout.WEST);
        
        //center
        JPanel centerPanel = new JPanel(new MigLayout("fill, insets 10", "[grow]", "[][][grow][]"));
        contentPane.add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new MigLayout("", "[][][]", "[]"));
        centerPanel.add(buttonsPanel, "cell 0 1");
        
                
                JButton btnSelectItem = new JButton("Select Item");
                btnSelectItem.setForeground(Color.WHITE);
                btnSelectItem.setBackground(new Color(100, 149, 237));
                btnSelectItem.addActionListener(e -> openProductSelection());
                btnSelectItem.setBounds(220, 58, 120, 23);
                
                        JButton btnRemoveItem = new JButton("Remove Item");
                        btnRemoveItem.setForeground(new Color(119, 136, 153));
                        btnRemoveItem.setBackground(new Color(175, 238, 238));
                        btnRemoveItem.addActionListener(e -> removeSelectedItem());
                        btnRemoveItem.setBounds(350, 58, 120, 23);
                        
                                JButton btnClearCart = new JButton("Clear Cart");
                                btnClearCart.setForeground(new Color(253, 245, 230));
                                btnClearCart.setBackground(new Color(250, 128, 114));
                                btnClearCart.addActionListener(e -> clearCart());
                                btnClearCart.setBounds(480, 58, 120, 23);
                                
                                
                                buttonsPanel.add(btnSelectItem);
                                buttonsPanel.add(btnRemoveItem);
                                buttonsPanel.add(btnClearCart);
        
        //bottom
        JPanel paymentPanel = new JPanel(new MigLayout(
        	    "insets 0",
        	    "[right][grow]",
        	    "[][][][]"
        	));
        centerPanel.add(paymentPanel, "dock south");
        panel.setLayout(new MigLayout("", "[178.00px]", "[20px][27px][27px][][][][][][][][][][][][][][]"));
        
        

        JButton btnLogout = new JButton("Logout");
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(178, 34, 34));
        btnLogout.addActionListener(e -> logout());
        
        JButton btnChangePassword = new JButton("Change Password");
        btnChangePassword.setForeground(new Color(253, 245, 230));
        btnChangePassword.setBackground(new Color(46, 139, 87));
        btnChangePassword.setFocusable(false);
        btnChangePassword.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		ChangePassword changepass = new ChangePassword(user.getUserId());
				changepass.setVisible(true);
        	}
        });
        
        
                JLabel lblName = new JLabel("Cashier:");
                lblName.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
                panel.add(lblName, "cell 0 1,alignx left,aligny top");
        
        cashierNameLabel = new JLabel(user.getName());
        cashierNameLabel.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
        panel.add(cashierNameLabel, "cell 0 2,alignx left,aligny top");
        panel.add(btnChangePassword, "cell 0 15,growx,aligny top");
        panel.add(btnLogout, "cell 0 16,growx,aligny top");

        String[] columnNames = {"Product ID", "Product Name", "Price (₱)", "Quantity", "Subtotal"};
        cartTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(cartTableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(cartTable);


        centerPanel.add(scrollPane, "cell 0 0,pushy,grow");



        // Payment 
        JLabel lblTotal = new JLabel("Total amount:");
        lblTotal.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
        totalAmountLabel = new JLabel("₱0.00");
        totalAmountLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        totalAmountLabel.setFont(new Font("Microsoft PhascrollPane.setBounds(214, 93, 550, 220);\n"
        		+ "contentPane.add(scrollPane);\n"
        		+ "gsPa", Font.BOLD, 14));
        totalAmountLabel.setBounds(662, 325, 100, 25);
        
        paymentPanel.add(lblTotal);
        paymentPanel.add(totalAmountLabel, "wrap");

        JLabel lblCash = new JLabel("Cash:");
        lblCash.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));

        cashTextField = new JTextField();
        cashTextField.setBounds(612, 376, 152, 25);
        cashTextField.setColumns(10);
        
        cashTextField.addKeyListener(new KeyAdapter() {
			int dot = 0;
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				for(int i = 0; i < cashTextField.getText().length(); i++) {
					if(cashTextField.getText().charAt(i) == '.') {
						dot = dot + 1;
					}
				}
				if(!(c >= '0' && c <= '9')) {
					if(!(c == '.' && dot == 0)) {
						e.consume();
					}
				}
				if(cashTextField.getText().length()+1 >= 11) {
					e.consume();
				}
				dot = 0;
			}
		});
        
        paymentPanel.add(lblCash);
        paymentPanel.add(cashTextField, "growx, wrap");

        JLabel lblChange = new JLabel("Change:");
        lblChange.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));

        changeLabel = new JLabel("₱0.00");
        changeLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        changeLabel.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 14));
        changeLabel.setBounds(662, 464, 100, 25);
        
        paymentPanel.add(lblChange);
        paymentPanel.add(changeLabel, "wrap");
        


        JButton btnCalculateChange = new JButton("Calculate Change");
        btnCalculateChange.setForeground(new Color(253, 245, 230));
        btnCalculateChange.setBackground(new Color(46, 139, 87));
        btnCalculateChange.addActionListener(e -> calculateChange());
        btnCalculateChange.setBounds(612, 424, 150, 25);
        btnCalculateChange.setFocusable(false);

        JButton btnProcessPayment = new JButton("Process Payment");
        btnProcessPayment.setForeground(new Color(253, 245, 230));
        btnProcessPayment.setBackground(new Color(60, 179, 113));
        btnProcessPayment.addActionListener(e -> processPayment(cashierUserId));
        btnProcessPayment.setBounds(614, 501, 150, 25);

		paymentPanel.add(btnCalculateChange, "span, growx, wrap");
		paymentPanel.add(btnProcessPayment, "span, growx");

        setLocationRelativeTo(null);
    }

    public void addToCart(int productId, String productName, double price, int quantity) {
        try {
            dbCon.connect();
            String sql = "SELECT product_stocks FROM products WHERE product_id = ?";
            try (PreparedStatement stmt = dbCon.con.prepareStatement(sql)) {
                stmt.setInt(1, productId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int currentStock = rs.getInt("product_stocks");
                        
                        int totalInCart = quantity;
                        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                            int existingId = (int) cartTableModel.getValueAt(i, 0);
                            if (existingId == productId) {
                                totalInCart += (int) cartTableModel.getValueAt(i, 3);
                            }
                        }
                        
                        if (totalInCart > currentStock) {
                            JOptionPane.showMessageDialog(this, 
                                "Cannot add " + quantity + " items of " + productName + 
                                "\nAvailable stock: " + currentStock + 
                                "\nTotal in cart after addition: " + totalInCart, 
                                "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error checking stock: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //stock check passed, add to cart
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            int existingId = (int) cartTableModel.getValueAt(i, 0);
            if (existingId == productId) {
                int newQty = (int) cartTableModel.getValueAt(i, 3) + quantity;
                cartTableModel.setValueAt(newQty, i, 3);
                cartTableModel.setValueAt(String.format("₱%.2f", newQty * price), i, 4);
                updateTotalAmount();
                return;
            }
        }
        cartTableModel.addRow(new Object[]{productId, productName, String.format("₱%.2f", price), quantity, String.format("₱%.2f", price * quantity)});
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        cartTotal = 0.0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String subtotal = ((String) cartTableModel.getValueAt(i, 4)).replace("₱", "").replace(",", "");
            cartTotal += Double.parseDouble(subtotal);
        }
        totalAmountLabel.setText(String.format("₱%.2f", cartTotal));
    }

    private void removeSelectedItem() {
        int row = cartTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cartTableModel.removeRow(cartTable.convertRowIndexToModel(row));
        updateTotalAmount();
    }

    private void clearCart() {
        if (cartTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart is already empty!", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Clear all items from cart?", "Confirm Clear", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            cartTableModel.setRowCount(0);
            cartTotal = 0.0;
            totalAmountLabel.setText("₱0.00");
            changeLabel.setText("₱0.00");
            cashTextField.setText("");
        }
    }

    private void calculateChange() {
        if (cartTotal == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double cash = Double.parseDouble(cashTextField.getText());
            if (cash < cartTotal) {
                JOptionPane.showMessageDialog(this, "Insufficient cash! Need at least ₱" + String.format("%.2f", cartTotal), "Insufficient Cash", JOptionPane.WARNING_MESSAGE);
                changeLabel.setText("₱0.00");
                return;
            }
            changeLabel.setText(String.format("₱%.2f", cash - cartTotal));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid cash amount!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processPayment(int cashierUserId) {
        if (cartTableModel.getRowCount() == 0) { 
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Empty Cart", JOptionPane.WARNING_MESSAGE); 
            return; 
        }
        
        if (cashTextField.getText().trim().isEmpty()) { 
            JOptionPane.showMessageDialog(this, "Enter cash amount!", "No Cash Amount", JOptionPane.WARNING_MESSAGE); 
            return; 
        }

        try {
            double cash = Double.parseDouble(cashTextField.getText());
            double change = cash - cartTotal;
            
            if (cash < cartTotal) { 
                JOptionPane.showMessageDialog(this, "Insufficient cash!", "Insufficient Cash", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            
            //Check product if a have stock
            if (!validateStockBeforePayment()) {
                return; // Validation failed, show error message
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Process sale for ₱" + String.format("%.2f", cartTotal) + 
                "?\nCash: ₱" + String.format("%.2f", cash) + 
                "\nChange: ₱" + String.format("%.2f", change), 
                "Confirm Sale", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION && recordSale(cash, change, cashierUserId)) {               
                printReceipt();
                clearCart();
            }
        } catch (NumberFormatException e) { 
            JOptionPane.showMessageDialog(this, "Enter valid cash amount!", "Invalid Input", JOptionPane.ERROR_MESSAGE); 
        }
    }

    private boolean validateStockBeforePayment() {
        try {
            dbCon.connect();
            
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                int productId = (int) cartTableModel.getValueAt(i, 0);
                int quantityToSell = (int) cartTableModel.getValueAt(i, 3);
                
                String sql = "SELECT product_stocks, product_name FROM products WHERE product_id = ?";
                try (PreparedStatement stmt = dbCon.con.prepareStatement(sql)) {
                    stmt.setInt(1, productId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int currentStock = rs.getInt("product_stocks");
                            String productName = rs.getString("product_name");
                            
                            if (currentStock < quantityToSell) {
                                JOptionPane.showMessageDialog(this, 
                                    "Insufficient stock for product: " + productName + 
                                    "\nAvailable: " + currentStock + 
                                    "\nRequested: " + quantityToSell, 
                                    "Insufficient Stock", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Product not found in database!", 
                                "Product Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                }
            }
            return true; // All products have sufficient stock
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error checking stock: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


    private int createSaleRecord() throws SQLException {
        String sql = "INSERT INTO sales_records (total_amount, date) VALUES (?, NOW())";
        try (PreparedStatement stmt = dbCon.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, cartTotal);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    private boolean recordSale(double cashReceived, double changeAmount, int cashierUserId) {
        try {
            dbCon.connect();
            dbCon.con.setAutoCommit(false);

            int salesId = createSaleRecord();
            if (salesId <= 0) throw new SQLException("Failed to create sale record.");

            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                int productId = (int) cartTableModel.getValueAt(i, 0);
                int quantity = (int) cartTableModel.getValueAt(i, 3);
                double price = getProductPrice(productId);
                double total = price * quantity;

                String sql = "INSERT INTO transactions (fk_transactions_users_id, fk_transactions_sales_id, fk_transactions_products_id, price_at_sale, quantity, date, total_amount) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
                try (PreparedStatement stmt = dbCon.con.prepareStatement(sql)) {
                    stmt.setInt(1, cashierUserId);
                    stmt.setInt(2, salesId);
                    stmt.setInt(3, productId);
                    stmt.setDouble(4, price);
                    stmt.setInt(5, quantity);
                    stmt.setDouble(6, total);
                    if (stmt.executeUpdate() <= 0) throw new SQLException("Failed transaction for product " + productId);
                }

                if (!updateProductStock(productId, quantity)) throw new SQLException("Failed stock update for product " + productId);
            }

            dbCon.con.commit();
            dbCon.con.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            try { if (dbCon.con != null) dbCon.con.rollback(); dbCon.con.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
            JOptionPane.showMessageDialog(this, "Error recording sale: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private double getProductPrice(int productId) throws SQLException {
        String sql = "SELECT product_price FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = dbCon.con.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("product_price");
            }
        }
        return 0.0;
    }

    private boolean updateProductStock(int productId, int quantitySold) throws SQLException {
        String sql = "UPDATE products SET product_stocks = product_stocks - ? WHERE product_id = ?";
        try (PreparedStatement stmt = dbCon.con.prepareStatement(sql)) {
            stmt.setInt(1, quantitySold);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    private void openProductSelection() { new ProductSelect(this).setVisible(true); }

    private void printReceipt() {
        if (cartTableModel.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Cart is empty!", "Empty Cart", JOptionPane.WARNING_MESSAGE); return; }

        StringBuilder receipt = new StringBuilder("=== RECEIPT ===\nCashier: ").append(cashierNameLabel.getText())
                .append("\nDate: ").append(new java.util.Date()).append("\n----------------------------\n");

        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            receipt.append(String.format("%-20s x%d\n  %s each = %s\n",
                    cartTableModel.getValueAt(i, 1), cartTableModel.getValueAt(i, 3),
                    cartTableModel.getValueAt(i, 2), cartTableModel.getValueAt(i, 4)));
        }
        receipt.append("----------------------------\nTOTAL: ").append(totalAmountLabel.getText());

        if (!cashTextField.getText().isEmpty()) {
            try { double cash = Double.parseDouble(cashTextField.getText());
                receipt.append("\nCASH: ₱").append(String.format("%.2f", cash)).append("\nCHANGE: ").append(changeLabel.getText());
            } catch (NumberFormatException ignored) {}
        }
        receipt.append("\n============================\nThank you for your purchase!\n");
        JOptionPane.showMessageDialog(this, receipt.toString(), "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }
}

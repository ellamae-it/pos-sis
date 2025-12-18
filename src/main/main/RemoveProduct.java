package main;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RemoveProduct extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private String selectedProductID = null;
	private JPanel contentPane;
	private JTable deleteTable;
	DbConnect dbCon = new DbConnect();
	private JTextField textField;
	private JTextField searchField;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					RemoveProduct frame = new RemoveProduct(new UserAccount());
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public RemoveProduct(UserAccount users, AdminDashboard dashboard) {

//		setTitle("Product Deletion");
//		setAlwaysOnTop(true);
//		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(400, 150, 750, 550);

	    contentPane = new JPanel(new BorderLayout(10, 10));
	    contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
	    contentPane.setBackground(new Color(253, 245, 230));
	    setContentPane(contentPane);

	    //top 
	    JPanel topPanel = new JPanel(new BorderLayout());
	    topPanel.setBackground(new Color(253, 245, 230));
	    contentPane.add(topPanel, BorderLayout.NORTH);

	    // top left
	    JPanel topLeft = new JPanel(new MigLayout("insets 0", "[]10[]", "[]"));
	    topLeft.setBackground(new Color(253, 245, 230));

	    ImageIcon backImg = new ImageIcon("src/resources/images/back.png");
	    Image image = backImg.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
	    JButton btnBack = new JButton(new ImageIcon(image));
	    btnBack.setBackground(new Color(253, 245, 230));
	    btnBack.setFocusPainted(false);
	    btnBack.setBorder(null);
	    btnBack.setPreferredSize(new Dimension(40, 30));
	    btnBack.addActionListener(e -> {
	        setVisible(false);
	        dashboard.setVisible(true);
	    });

	    JLabel lbldelete = new JLabel("PRODUCT DELETION");
	    lbldelete.setFont(new Font("Dialog", Font.BOLD, 30));
	    lbldelete.setForeground(new Color(210, 180, 140));

	    topLeft.add(btnBack);
	    topLeft.add(lbldelete);

	    //top right
	    JPanel topRight = new JPanel(new MigLayout("insets 0", "[]10[]", "[]"));
	    topRight.setBackground(new Color(253, 245, 230));

	    searchField = new JTextField(15);
	    searchField.setFont(new Font("Tahoma", Font.PLAIN, 16));
	    searchField.setText("Search product...");
	    searchField.setForeground(Color.GRAY);

	    searchField.addFocusListener(new FocusAdapter() {
	        @Override
	        public void focusGained(FocusEvent e) {
	            if (searchField.getText().equals("Search product...")) {
	                searchField.setText("");
	                searchField.setForeground(Color.BLACK);
	            }
	        }

	        @Override
	        public void focusLost(FocusEvent e) {
	            if (searchField.getText().isEmpty()) {
	                searchField.setText("Search product...");
	                searchField.setForeground(Color.GRAY);
	            }
	        }
	    });
	    

	    JButton searchBtn = new JButton("Search");
	    searchBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
	    searchBtn.setBackground(new Color(144, 238, 144));
	    searchBtn.addActionListener(e -> searchTable(users));

	    topRight.add(searchField);
	    topRight.add(searchBtn);

	    topPanel.add(topLeft, BorderLayout.WEST);
	    topPanel.add(topRight, BorderLayout.EAST);

	    //center table
	    JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
	    centerPanel.setBackground(new Color(253, 245, 230));
	    
	    JLabel lblInstruction = new JLabel("Click the product you want to delete");
	    lblInstruction.setFont(new Font("Dialog", Font.BOLD, 12));
	    lblInstruction.setForeground(new Color(210, 180, 140));
	    lblInstruction.setBorder(new EmptyBorder(0, 5, 5, 0));
	    
	    deleteTable = new JTable();
	    deleteTable.setBackground(new Color(253, 245, 230));
	    deleteTable.getTableHeader().setBackground(new Color(245, 222, 179));
	    deleteTable.getTableHeader().setForeground(Color.BLACK);

	    deleteTable.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            int row = deleteTable.getSelectedRow();
	            DefaultTableModel model = (DefaultTableModel) deleteTable.getModel();
	            selectedProductID = model.getValueAt(row, 1).toString();
	            textField.setText(selectedProductID);
	        }
	    });

	    JScrollPane scrollPane = new JScrollPane(deleteTable);

	    centerPanel.add(lblInstruction, BorderLayout.NORTH);
	    centerPanel.add(scrollPane, BorderLayout.CENTER);
	    
	    contentPane.add(centerPanel, BorderLayout.CENTER);

	    loadTable(users);

	    //bottom right
	    JPanel bottomPanel = new JPanel(new MigLayout("insets 0, align right", "[]10[]10[]", "[]"));
	    bottomPanel.setBackground(new Color(253, 245, 230));
	    contentPane.add(bottomPanel, BorderLayout.SOUTH);

	    JLabel lblid = new JLabel("SELECTED ID:");
	    lblid.setFont(new Font("Tahoma", Font.PLAIN, 16));

	    textField = new JTextField(10);
	    textField.setEditable(false);
	    textField.setFocusable(false);

	    JButton deleteButton = new JButton("DELETE");
	    deleteButton.setFont(new Font("Tahoma", Font.BOLD, 16));
	    deleteButton.setBackground(new Color(240, 128, 128));
	    deleteButton.addActionListener(e -> {
	        int choice = JOptionPane.showConfirmDialog(
	                RemoveProduct.this,
	                "Do you want to delete product?",
	                "Product Deletion Confirmation",
	                JOptionPane.YES_NO_CANCEL_OPTION);

	        if (choice == JOptionPane.YES_NO_OPTION) {
	            deletion(users, dashboard);
	            loadTable(users);
	            textField.setText("");
	        }
	    });

	    bottomPanel.add(lblid);
	    bottomPanel.add(textField);
	    bottomPanel.add(deleteButton);
	
	}
	
	
	
	
	
	
	public void deletion(UserAccount users, AdminDashboard dashboard) {
		
		String productID = textField.getText();
		
		try {
			dbCon.connect();
			dbCon.con.setAutoCommit(false);
			
			if (productID.isEmpty()) {
			    
			    JOptionPane.showMessageDialog(
			    		RemoveProduct.this,
			        "Product ID field cannot be empty. Please enter an ID.",
			        "Input Required",
			        JOptionPane.WARNING_MESSAGE
			    );

			    textField.requestFocusInWindow();

			    return;
			}
			
			String delete = "UPDATE products SET product_status = 'discontinued' WHERE product_id = ?";
			PreparedStatement prep = dbCon.con.prepareStatement(delete);
			
			prep.setString(1, productID);
		
			if (prep.executeUpdate() <= 0)
	            throw new SQLException("Product not updated");

	        if (!inventoryChanges(users))
	            throw new SQLException("Inventory change failed");

	        dbCon.con.commit();
	        
	        JOptionPane.showMessageDialog(
	                RemoveProduct.this,
	                "Product status successfully updated.",
	                "Success",
	                JOptionPane.INFORMATION_MESSAGE
	            );
	        
	     
	        setVisible(false);
	        dispose(); 

	        //back to dash
	        AdminDashboard newDashboard = new AdminDashboard(users); 
	        newDashboard.setVisible(true);
			
		} catch (Exception e) {
	        try { dbCon.con.rollback(); } catch (Exception ex) {}
	        e.printStackTrace();
	    } finally {
	        try { dbCon.con.setAutoCommit(true); } catch (Exception e) {}
	    }
	}
	
	
	
	
	
	public boolean inventoryChanges(UserAccount users) {
		try {
			int userIDChange = users.getUserId();
			
			String productIDString = textField.getText();
			int productIDChange = Integer.parseInt(productIDString);
			
			String typeOfChange = "remove";
			
			String message = JOptionPane.showInputDialog(
					RemoveProduct.this,
				    "Please enter the reason for discontinuing this product:",
				    "Reason Required",
				    JOptionPane.QUESTION_MESSAGE
				);
			LocalDateTime dateTime = LocalDateTime.now();
			String date_time = String.valueOf(dateTime);
			
			
			String insert = "INSERT INTO inventory_changes ("
		               + "fk_inventory_changes_user_id, "
		               + "fk_inventory_changes_product_id, "
		               + "type_of_change, "
		               + "message, "
		               + "date) "
		               + "VALUES (?, ?, ?, ?, ?)";
			
			PreparedStatement prep = dbCon.con.prepareStatement(insert);
			
			prep.setInt(1, userIDChange); 
	        prep.setInt(2, productIDChange);     
	        prep.setString(3, typeOfChange); 
	        prep.setString(4, message.trim());

	        prep.setString(5, date_time);
			
	        return prep.executeUpdate() > 0;
	        
	        
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	
	public void searchTable(UserAccount users) {
		
		String search = searchField.getText();
		
		try {
			dbCon.connect();
			
			String select = "SELECT * FROM products WHERE product_status = 'listed' AND product_name LIKE ?";
			PreparedStatement prep = dbCon.con.prepareStatement(select);
			prep.setString(1, "%" + search + "%");
			
				DefaultTableModel model = new DefaultTableModel(){
			        private static final long serialVersionUID = 1L;

			        @Override
			        public boolean isCellEditable(int row, int column) {
			           return false; 
			        }
			    };
				
			    model.addColumn("Item No.");
				model.addColumn("Product ID");
				model.addColumn("Product");
				model.addColumn("Price");
				model.addColumn("Stock");
				
				deleteTable.setModel(model);
			
			ResultSet result = prep.executeQuery();
			
			int count = 1;

				while (result.next()) {
				    
				    int productId = result.getInt("product_id");
				    String productName = result.getString("product_name");
				    double price = result.getDouble("product_price");
				    int stock = result.getInt("product_stocks");
				    
				    DecimalFormat decimalFormat = new DecimalFormat("#, ##0.00");
				    String formattedPrice = String.valueOf(decimalFormat.format(price));

				    model.addRow(new Object[] {count++, productId, productName, formattedPrice, stock});
				}
				
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void loadTable(UserAccount users) {
		
		try {
			dbCon.connect();
			
			String select = "SELECT * from products where product_status = 'listed'";
			PreparedStatement prep = dbCon.con.prepareStatement(select);
			
				DefaultTableModel model = new DefaultTableModel(){
			        private static final long serialVersionUID = 1L;

			        @Override
			        public boolean isCellEditable(int row, int column) {
			           return false; 
			        }
			    };
				
			    model.addColumn("Item No.");
				model.addColumn("Product ID");
				model.addColumn("Product");
				model.addColumn("Price");
				model.addColumn("Stock");
				
				deleteTable.setModel(model);
			
			ResultSet result = prep.executeQuery();
			
			int count = 1;
			
			while(result.next()) {
				    
				    int productId = result.getInt("product_id");
				    String productName = result.getString("product_name");
				    double price = result.getDouble("product_price");
				    int stock = result.getInt("product_stocks");
				    
				    DecimalFormat decimalFormat = new DecimalFormat("#, ##0.00");
				    String formattedPrice = String.valueOf(decimalFormat.format(price));

				    model.addRow(new Object[] {count++, productId, productName, formattedPrice, stock});
			}
				
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

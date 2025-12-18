package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

//import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

public class EditProduct extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int productId;
	DbConnect dbCon = new DbConnect();
	private JTextField tf_prod_name;
	private JTextField tf_prod_price;
	private JTextField tf_prod_stocks;
	private String old_name;
	private double old_price;
	private int old_stock;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					EditProduct frame = new EditProduct(product_id);
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
	public EditProduct(String productId, UserAccount user, AdminDashboard dashboard) {
		this.productId = Integer.valueOf(productId);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 150, 750, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(253, 245, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[110.00][112.00][182.00,grow][103.00][44.00,grow][95.00][114.00]", "[27.00][][][][][][][][][][][][]"));
		
		
		JLabel label = new JLabel(" ");
		contentPane.add(label, "cell 3 0");
		
		JLabel lblEditProduct = new JLabel("Edit Product");
		lblEditProduct.setFont(new Font("Dialog", Font.BOLD, 25));
		contentPane.add(lblEditProduct, "cell 3 2,alignx center");
		
		JButton btnBack = new JButton("Back");
		btnBack.setBackground(new Color(238, 232, 170));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);       
	             dashboard.setVisible(true); 
			}
		});
		contentPane.add(btnBack, "cell 1 12,growx");
		
		
		JLabel lblProductName = new JLabel("Product Name:");
		contentPane.add(lblProductName, "cell 1 5");
		
		tf_prod_name = new JTextField();
		contentPane.add(tf_prod_name, "cell 2 5 4 1,growx");
		tf_prod_name.setColumns(10);
		
		tf_prod_name.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(tf_prod_name.getText().length()+1 >= 25) {
					e.consume();
				}
			}
		});	
		
		JLabel lblProductPrice = new JLabel("Product Price:");
		contentPane.add(lblProductPrice, "cell 1 7");
		
		tf_prod_price = new JTextField();
		tf_prod_price.setColumns(10);
		contentPane.add(tf_prod_price, "cell 2 7 4 1,growx");
		
		tf_prod_price.addKeyListener(new KeyAdapter() {
			int dot = 0;
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				for(int i = 0; i < tf_prod_price.getText().length(); i++) {
					if(tf_prod_price.getText().charAt(i) == '.') {
						dot = dot + 1;
					}
				}
				if(!(c >= '0' && c <= '9')) {
					if(!(c == '.' && dot == 0)) {
						e.consume();
					}
				}
				if(tf_prod_price.getText().length()+1 >= 11) {
					e.consume();
				}
				dot = 0;
			}
		});
		
		JLabel lblProductStocks = new JLabel("Product Stocks:");
		contentPane.add(lblProductStocks, "cell 1 9");
		
		tf_prod_stocks = new JTextField();
		contentPane.add(tf_prod_stocks, "cell 2 9 4 1,growx");
		tf_prod_stocks.setColumns(10);
		
		tf_prod_stocks.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!(c >= '0' && c <= '9')) {
					e.consume();
				}
				if(tf_prod_stocks.getText().length()+1 >= 11) {
					e.consume();
				}
			}
		});	
		
		JButton btnEditProduct = new JButton("Edit Product");
		btnEditProduct.setBackground(new Color(210, 180, 140));
		btnEditProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				edit(user);
			}
		});
		contentPane.add(btnEditProduct, "cell 5 12,growx");
		
		product(this.productId);
		

	}
	
	public void product(int product_id) {
		try {
			dbCon.connect();
			dbCon.con.setAutoCommit(false);
			
			String select = "Select * from products where product_id = " + product_id;
			PreparedStatement prep = dbCon.con.prepareStatement(select);
			ResultSet result = prep.executeQuery();
			
			if(result.next()) {
				tf_prod_name.setText(result.getString("product_name"));
				tf_prod_price.setText(result.getString("product_price"));
				tf_prod_stocks.setText(result.getString("product_stocks"));
				this.old_name =result.getString("product_name");
				this.old_price = Double.valueOf(result.getString("product_price"));
				this.old_stock = Integer.valueOf(result.getString("product_stocks"));
			}else {
				System.out.println("Error fetching the product");
			}
			
			dbCon.con.commit();
            dbCon.con.setAutoCommit(true);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void edit(UserAccount user) {
		try {
			dbCon.connect();
			String new_name = tf_prod_name.getText();
			double new_price = Double.valueOf(tf_prod_price.getText());
			int new_stocks = Integer.valueOf(tf_prod_stocks.getText());
			
			if(new_name.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a name!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String update = "UPDATE products set product_name = ?, product_price =?, product_stocks=?, fk_products_user_id = ? where product_id = ?";
			PreparedStatement prep = dbCon.con.prepareStatement(update);
			prep.setString(1,new_name);
			prep.setDouble(2,new_price);
			prep.setInt(3,new_stocks);
			prep.setInt(4,user.getUserId());
			prep.setInt(5,productId);
			
			int rows = prep.executeUpdate();
					
						
			if (rows > 0) {
		        String message = "";
		
		        if (!new_name.equals(old_name)) message += "Name Change: " + old_name + " -> " + new_name + "\n";
		        if (new_price != old_price) message += "Price Change: " + old_price + " -> " + new_price + "\n";
		        if (new_stocks != old_stock) message += "Stocks Updated: " + old_stock + " -> " + new_stocks + "\n";
		
		        // INSERT into inventory_changes
		        String insert = "INSERT INTO inventory_changes(fk_inventory_changes_user_id, fk_inventory_changes_product_id, type_of_change, message, date) "
		                      + "VALUES (?, ?, ?, ?, NOW())";
		        PreparedStatement prep1 = dbCon.con.prepareStatement(insert);
		        prep1.setInt(1, user.getUserId());
		        prep1.setInt(2, productId);
		        prep1.setString(3, "update");
		        prep1.setString(4, message);
		
		        int rows1 = prep1.executeUpdate();
		
		        if (rows1 > 0) {
		        	JOptionPane.showMessageDialog(this, "Update Succesful", "Update", JOptionPane.INFORMATION_MESSAGE);
		            AdminDashboard admin_dash = new AdminDashboard(user);
		            setVisible(false);
		            admin_dash.setVisible(true);
		        }
		
		    } else {
		        System.out.println("Error: No rows updated in products table.");
		    }
			
		}catch(Exception e) {
			JOptionPane.showMessageDialog(this,
	                "Please enter a value!",
	                "Invalid Input",
	                JOptionPane.ERROR_MESSAGE);
		}
	}
	
}

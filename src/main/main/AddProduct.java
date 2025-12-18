package main;


import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.sql.Statement;


import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class AddProduct extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_prod_name;
	private JTextField tf_prod_price;
	DbConnect dbCon = new DbConnect();
	private JTextField tf_prod_stock;

	

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					AddProduct frame = new AddProduct(new UserAccount());
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
	public AddProduct(UserAccount user, AdminDashboard dashboard) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 150, 750, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(253, 245, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[110.00][112.00][182.00,grow][103.00][44.00,grow][95.00][114.00]", "[27.00][][][][][][][][][][][][]"));
		
		JLabel label = new JLabel(" ");
		contentPane.add(label, "cell 3 0");
		
		JLabel lblAddProduct = new JLabel("Add Product");
		lblAddProduct.setFont(new Font("Dialog", Font.BOLD, 25));
		contentPane.add(lblAddProduct, "cell 3 2,alignx center");
		
		JButton btnBack = new JButton("Back");
		btnBack.setBackground(new Color(238, 232, 170));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);       
	             dashboard.setVisible(true); 
			}
		});
		
		JLabel lblProductNAme = new JLabel("Product Name:");
		contentPane.add(lblProductNAme, "cell 1 5");
		
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
		
		JLabel lbl_prod_stock = new JLabel("Product Stock:");
		contentPane.add(lbl_prod_stock, "cell 1 9");
		
		tf_prod_stock = new JTextField();
		contentPane.add(tf_prod_stock, "cell 2 9 4 1,growx");
		tf_prod_stock.setColumns(10);
		
		tf_prod_stock.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!(c >= '0' && c <= '9')) {
					e.consume();
				}
				if(tf_prod_stock.getText().length()+1 >= 11) {
					e.consume();
				}
			}
		});	
		contentPane.add(btnBack, "cell 1 12,growx");
		
		JButton btnAddProduct = new JButton("Add Product");
		btnAddProduct.setBackground(new Color(210, 180, 140));
		btnAddProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				add_product(user);
			}
		});
		contentPane.add(btnAddProduct, "cell 5 12,growx");

	}
	
	public void add_product(UserAccount user) {
	
		int user_id = user.getUserId();
		String prod_name = tf_prod_name.getText();
		double prod_price = Double.valueOf(tf_prod_price.getText());
		int prod_stock = Integer.valueOf(tf_prod_stock.getText());
		
		try {
			dbCon.connect();
			dbCon.con.setAutoCommit(false);
			
			if (prod_name.isEmpty() || prod_price == 0 ) {
			    JOptionPane.showMessageDialog(null, "Product Name and Product Price are required! Please fill in the missing information.");
			    return; 
			}
			
			String query = "INSERT INTO products(product_name, product_price, product_stocks, fk_products_user_id)"
							+"VALUES(?,?,?,?)";
			PreparedStatement prep = dbCon.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, prod_name);
			prep.setDouble(2, prod_price);
			prep.setInt(3, prod_stock);
			prep.setInt(4, user_id);
			
			int rows = prep.executeUpdate();
;			
			
			
			if(rows > 0) {
				ResultSet rs = prep.getGeneratedKeys();
				int productId = 0;
				if (rs.next()) {                    
			        productId = rs.getInt(1);  
			    }
				String message= prod_name +" product added";
				
				 String insert = "INSERT INTO inventory_changes(fk_inventory_changes_user_id, fk_inventory_changes_product_id, type_of_change, message, date) "
	                      + "VALUES (?, ?, ?, ?, NOW())";
				 
				 PreparedStatement prep1 = dbCon.con.prepareStatement(insert);
			        prep1.setInt(1, user.getUserId());
			        prep1.setInt(2, productId);
			        prep1.setString(3, "add");
			        prep1.setString(4, message);
			
			        int rows1 = prep1.executeUpdate();
			
			        if (rows1 > 0) {
			        	JOptionPane.showMessageDialog(this, "Product Added: "+ prod_name +"!");
			        }
			        dbCon.con.commit();
		            dbCon.con.setAutoCommit(true);
		            
		            this.dispose();
					AdminDashboard admin_dash = new AdminDashboard(user);
					admin_dash.setVisible(true);
				
			}else {
				System.out.println("Unexpected Error Occured");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("error: " + user_id);
		}
		
		
	}

}

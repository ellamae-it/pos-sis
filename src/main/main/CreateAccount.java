package main;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import java.awt.Color;

public class CreateAccount extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_email;
	private JTextField tf_fname;
	private JTextField tf_mname;
	private JTextField tf_lname;
	private JTextField tf_cnum;
	ButtonGroup accountType = new ButtonGroup();
	private JRadioButton rdbtnAdmin;
	private JRadioButton rdbtnCashier;
	DbConnect dbCon = new DbConnect();
	private JButton btnCreateAccount;
	private JButton btnBack;
	private JTextField passwordField;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					CreateAccount frame = new CreateAccount(new UserAccount());
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
	public CreateAccount(UserAccount user, AdminDashboard dashboard) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 150, 750, 550);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(253, 245, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[133px][290.00,grow][right]", "[17px][][][17px][][17px][][17px][17px][][][][17px][17px][][]"));
		
		JLabel lblCreateAccount = new JLabel("Create Account");
		lblCreateAccount.setFont(new Font("Dialog", Font.BOLD, 25));
		contentPane.add(lblCreateAccount, "cell 1 4,alignx center,aligny top");
		
		JLabel lblFirstName = new JLabel("First Name:");
		contentPane.add(lblFirstName, "cell 0 6,alignx trailing,aligny top");
		
		tf_fname = new JTextField();
		contentPane.add(tf_fname, "cell 1 6,growx");
		tf_fname.setColumns(10);
		
		JLabel lblMiddleName = new JLabel("Middle Name:");
		contentPane.add(lblMiddleName, "cell 0 7,alignx trailing,aligny top");
		
		tf_mname = new JTextField();
		tf_mname.setColumns(10);
		contentPane.add(tf_mname, "cell 1 7,growx");
		
		JLabel lblLastName = new JLabel("Last Name:");
		contentPane.add(lblLastName, "cell 0 8,alignx trailing,aligny top");
		
		tf_lname = new JTextField();
		tf_lname.setColumns(10);
		contentPane.add(tf_lname, "cell 1 8,growx");
		
		JLabel lblContactNumber = new JLabel("Contact Number:");
		contentPane.add(lblContactNumber, "cell 0 9,alignx trailing,aligny top");
		
		tf_cnum = new JTextField();
		tf_cnum.setColumns(10);
		contentPane.add(tf_cnum, "cell 1 9,growx");
		
		tf_cnum.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(!(c >= '0' && c <= '9')) {
					e.consume();
				}
				if(tf_cnum.getText().length() >= 11) {
					e.consume();
				}
			}
		});	
		
		JLabel lblEmail = new JLabel("Email:");
		contentPane.add(lblEmail, "cell 0 10,alignx right,aligny center");
		
		tf_email = new JTextField();
		contentPane.add(tf_email, "cell 1 10,growx,aligny center");
		tf_email.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		contentPane.add(lblPassword, "cell 0 11,alignx trailing,aligny top");
		
		JLabel lbl_usertype = new JLabel("Account Type");
		contentPane.add(lbl_usertype, "cell 0 12,alignx trailing,aligny top");
		
		
		rdbtnAdmin = new JRadioButton("admin");
		contentPane.add(rdbtnAdmin, "flowx,cell 1 12,alignx center,aligny center");
		
		
		rdbtnCashier = new JRadioButton("cashier");
		contentPane.add(rdbtnCashier, "cell 1 12,alignx center");
		
		rdbtnCashier.setSelected(true); // cashier selected by default
		
		accountType.add(rdbtnAdmin);
		accountType.add(rdbtnCashier);
		
		btnBack = new JButton("Back");
		btnBack.setBackground(new Color(238, 232, 170));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);       
	            dashboard.setVisible(true); 
			}
		});
		contentPane.add(btnBack, "cell 0 15,alignx center,aligny center");
		
		btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.setBackground(new Color(210, 180, 140));
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				create_account(user);
			}
		});
		contentPane.add(btnCreateAccount, "cell 1 15,alignx right");
		
		passwordField = new JTextField();
		contentPane.add(passwordField, "cell 1 11,growx");
		passwordField.setColumns(10);
		
		
	
	
	}
	
	public void create_account(UserAccount user) {
		String email = tf_email.getText().trim();
		String password = String.valueOf(passwordField.getText());
		String fname = tf_fname.getText().trim();
		String mname = (tf_mname.getText()!= null) ? tf_mname.getText().trim() : "";
		String lname = tf_lname.getText().trim();
		String contact_num = tf_cnum.getText().trim();
		String usertype = "";
		
		if (rdbtnAdmin.isSelected()) {
			usertype = "admin";
		} else if (rdbtnCashier.isSelected()) {
			usertype = "cashier";
		}
		
		
		if (email.isEmpty() || password.isEmpty() || fname.isEmpty() || lname.isEmpty()) {
		    JOptionPane.showMessageDialog(null, "Email, Password, First and Last Name, and Account Type are required! Please fill in the missing information.");
		    return; 
		}
		
		if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password should be at least 6 characters long!", 
                "Weak Password", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
		
		if(!email.endsWith("@gmail.com")) {
			JOptionPane.showMessageDialog(this, 
	                "Google account should be used!", 
	                "Invalid Email", 
	                JOptionPane.WARNING_MESSAGE);
	            return;
		}

		
		try {
			
			dbCon.connect();
			
			String insert = "INSERT INTO users (email, password, usertype, first_name, middle_name, last_name, contact_number, status)"
					+"VALUES(?,?,?,?,?,?,?,'active')";
			PreparedStatement prep = dbCon.con.prepareStatement(insert);
			prep.setString(1, email);
			prep.setString(2, password);
			prep.setString(3, usertype);
			prep.setString(4, fname);
			prep.setString(5, mname);
			prep.setString(6, lname);
			prep.setString(7, contact_num);
					
			int rowsInserted = prep.executeUpdate();
			
			if(rowsInserted > 0) {
				JOptionPane.showMessageDialog(this, "Account Created: "+ fname + " " + lname +"!");
				setVisible(false);
				AdminDashboard admin_dash = new AdminDashboard(user);
				admin_dash.setVisible(true);
				
			}else {
				System.out.println("error");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}

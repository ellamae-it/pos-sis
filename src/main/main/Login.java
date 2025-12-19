package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;



public class Login extends JFrame {
			

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_email;
	private JPasswordField passwordField;

	public DbConnect dbCon = new DbConnect();	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setBackground(new Color(253, 245, 230));
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(750, 550);
	    setLocationRelativeTo(null); 

	    
	    contentPane = new JPanel(new BorderLayout());
	    contentPane.setBackground(new Color(253, 245, 230));
	    contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
	    setContentPane(contentPane);

	    //left
	    JPanel panelLeft = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[73.00][211.00][111.00]"));
	    panelLeft.setBackground(new Color(238, 232, 170));

	    contentPane.add(panelLeft, BorderLayout.WEST);
	    
	    	    JLabel lblPOS = new JLabel("P O I N T  O F  S A L E");
	    	    lblPOS.setForeground(new Color(210, 180, 140));
	    	    lblPOS.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
	    	    lblPOS.setHorizontalAlignment(SwingConstants.CENTER);
	    	    panelLeft.add(lblPOS, "flowy,cell 0 1,alignx center");
	    	    
	    	    	    JLabel lblSIS = new JLabel("S A L E S  I N V E N T O R Y");
	    	    	    lblSIS.setHorizontalAlignment(SwingConstants.CENTER);
	    	    	    lblSIS.setFont(new Font("Dialog", Font.BOLD, 18));
	    	    	    panelLeft.add(lblSIS, "cell 0 1,alignx center,wrap");

	    //right
	    JPanel panelRight = new JPanel(new MigLayout("fill, wrap 1", "[106.00][305.00][91.00]", "[][]15[]15[]5[]15[]15[]15[]15[][]"));
	    panelRight.setBackground(new Color(253, 245, 230));
	    
	    JLabel lblLOG = new JLabel("WELCOME BACK !");
	    lblLOG.setForeground(new Color(210, 180, 140));
	    lblLOG.setFont(new Font("Dialog", Font.BOLD, 25));
	    lblLOG.setHorizontalAlignment(SwingConstants.CENTER);
	    panelRight.add(lblLOG, "cell 1 2,growx,alignx center");
	    	    	    	    	    	    
	    JLabel lblEmail = new JLabel("Email");
	    lblEmail.setFont(new Font("Dialog", Font.PLAIN, 15));
	    panelRight.add(lblEmail, "cell 1 3,growx");
	    	    	    	    	    	    
	    tf_email = new JTextField();
	    tf_email.setBackground(new Color(238, 232, 170));
	    tf_email.setBorder(null);
	    panelRight.add(tf_email, "cell 1 4,growx,height 30");	    	    	    
	    	    	    	    	    
	    JLabel lblPassword = new JLabel("Password");
	    lblPassword.setFont(new Font("Dialog", Font.PLAIN, 15));
	    panelRight.add(lblPassword, "cell 1 5,growx");
	    	    	    	    
	    passwordField = new JPasswordField();
	    passwordField.setBackground(new Color(238, 232, 170));
	    passwordField.setBorder(null);
	    panelRight.add(passwordField, "cell 1 6,growx,height 30");
	    
	    JButton btnLogIn = new JButton("L O G I N");
	    btnLogIn.setFont(new Font("Dialog", Font.BOLD, 14));
	    btnLogIn.setForeground(new Color(253, 245, 230));
	    btnLogIn.setBackground(new Color(210, 180, 140));
	    btnLogIn.setBorder(null);
	    btnLogIn.setPreferredSize(new Dimension(120, 35));
	    panelRight.add(btnLogIn, "cell 1 7,alignx center,aligny top,gapy 20");
	    btnLogIn.addActionListener(e -> login(new UserAccount()));
	    	    

	    contentPane.add(panelRight, BorderLayout.CENTER);
	}
	
	//login method
	public void login(UserAccount user) {
		String email = tf_email.getText().trim();
		String password = String.valueOf(passwordField.getPassword());
		user.setEmail(email);
		
		if(email.isEmpty()&& password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields are required", "Login error", JOptionPane.ERROR_MESSAGE);
            return;
		}
		
		try {
			dbCon.connect();
			String select = "SELECT * FROM users WHERE email = ?";
			PreparedStatement prep = dbCon.con.prepareStatement(select);
			prep.setString(1, email);
			
			ResultSet result = prep.executeQuery();
			
			
			if(result.next()) {
				user.setEmail(result.getString("email"));
				user.setPassword(result.getString("password"));
				user.setName(result.getString("first_name"),result.getString("last_name"));
				user.setUsertype(result.getString("usertype"));
				user.setUserId(result.getInt("user_id"));
				user.setStatus(result.getString("status"));
				
				
				
				if(email.equals(user.getEmail())&&(password.equals(user.getPassword()))) {
					if((user.getStatus().equals("active"))) {
						if(user.getUsertype().equals("admin")) {
							AdminDashboard admin_dashboard = new AdminDashboard(user);
							setVisible(false);
							admin_dashboard.setVisible(true);
						}else {					
						CashierDashboard cashier_dashboard = new CashierDashboard(user);
						setVisible(false);
						cashier_dashboard.setVisible(true);
						}
					}else {

						JOptionPane.showMessageDialog(this, "Your account is deactivated. You can no longer enter the system", "Account Deactivated", JOptionPane.ERROR_MESSAGE);
					}
					
				}else {
					
					JOptionPane.showMessageDialog(this, "Incorrect email or password", "Login error", JOptionPane.ERROR_MESSAGE);
				}
			}else {
		            JOptionPane.showMessageDialog(this, "Incorrect email or password", "Login error", JOptionPane.ERROR_MESSAGE);
		            return;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
 
	}
}

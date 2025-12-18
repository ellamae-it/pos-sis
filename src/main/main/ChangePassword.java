package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;

public class ChangePassword extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JPasswordField passwordField_2;
    private int current_userId;
    DbConnect dbCon = new DbConnect();


    /**
     * Launch the application for testing.
     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    // For testing, you need to pass a user ID
//                	AdminChangePassword frame = new AdminChangePassword(1); // Test with user ID 1
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

 

    /**
     * Create the frame with user ID.
     */
    public ChangePassword(int  currentUserId) {
        this. current_userId =  currentUserId;
        
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 472, 306);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(253, 245, 230));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        setTitle("Change Password");
        contentPane.setLayout(new MigLayout("", "[140px][10px][160px][][70.00]", "[34px][25px][25px][25px][30px][30px][][]"));
        
        JLabel lblTitle = new JLabel("Change Password");
        lblTitle.setBackground(new Color(210, 180, 140));
        lblTitle.setFont(new Font("Microsoft New Tai Lue", Font.BOLD, 20));
        contentPane.add(lblTitle, "cell 2 0,alignx center,growy");
        
        JLabel lblCurrentPassword = new JLabel("Current Password:");
        lblCurrentPassword.setFont(new Font("Microsoft New Tai Lue", Font.PLAIN, 14));
        contentPane.add(lblCurrentPassword, "cell 0 2,grow");
        
        passwordField = new JPasswordField();
        contentPane.add(passwordField, "cell 2 2 3 1,grow");
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(238, 232, 170));
        btnCancel.setFont(new Font("Microsoft New Tai Lue", Font.PLAIN, 12));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        JButton btnChange = new JButton("Change Password");
        btnChange.setBackground(new Color(210, 180, 140));
        btnChange.setFont(new Font("Dialog", Font.BOLD, 12));
        btnChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
        
        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setFont(new Font("Microsoft New Tai Lue", Font.PLAIN, 14));
        contentPane.add(lblNewPassword, "cell 0 3,grow");
        
        passwordField_1 = new JPasswordField();
        contentPane.add(passwordField_1, "cell 2 3 3 1,grow");
        
        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setFont(new Font("Microsoft New Tai Lue", Font.PLAIN, 14));
        contentPane.add(lblConfirmPassword, "cell 0 4,grow");
        
        passwordField_2 = new JPasswordField();
        contentPane.add(passwordField_2, "cell 2 4 3 1,grow");
        contentPane.add(btnChange, "cell 2 6,alignx center,growy");
        contentPane.add(btnCancel, "cell 2 7,alignx center,growy");
        
        
        setLocationRelativeTo(null);
    }


    public void  current_userId(int currentUserId) {
        this. current_userId = currentUserId;
    }

     

    private void changePassword() {
    	dbCon.connect();
        
        String currentPassword = new String(passwordField.getPassword()).trim();
        String newPassword = new String(passwordField_1.getPassword()).trim();
        String confirmPassword = new String(passwordField_2.getPassword()).trim();
        
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all password fields!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "New password and confirm password do not match!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            passwordField_1.setText("");
            passwordField_2.setText("");
            passwordField_1.requestFocus();
            return;
        }
        
        if (newPassword.equals(currentPassword)) {
            JOptionPane.showMessageDialog(this, 
                "New password cannot be the same as current password!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            passwordField_1.setText("");
            passwordField_2.setText("");
            passwordField_1.requestFocus();
            return;
        }
        
        
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password should be at least 6 characters long!", 
                "Weak Password", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
       
        if (!verifyCurrentPassword(currentPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Current password is incorrect!", 
                "Authentication Error", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
            return;
        }
        
        
        if (updatePassword(newPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Password changed successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to change password!", 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    

    

    private boolean verifyCurrentPassword(String currentPassword) {
        dbCon.connect();
        
        String query = "SELECT password FROM users WHERE user_id = ? AND password = ?";
        
        try (PreparedStatement pstmt = dbCon.con.prepareStatement(query)) { 
            pstmt.setInt(1,  current_userId);
            pstmt.setString(2, currentPassword);
            
            ResultSet rs = pstmt.executeQuery();
            boolean isValid = rs.next();
            rs.close();
            return isValid;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    private boolean updatePassword(String newPassword) {
    	 dbCon.connect();
        
        String query = "UPDATE users SET password = ? WHERE user_id = ?";
        
        try (PreparedStatement pstmt = dbCon.con.prepareStatement(query)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2,  current_userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    private void clearFields() {
        passwordField.setText("");
        passwordField_1.setText("");
        passwordField_2.setText("");
    }
    
    
}
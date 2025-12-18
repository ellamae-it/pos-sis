package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class Accounts extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tableAccounts;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbAccounts;
    private JComboBox<String> cbStatus;
    DbConnect dbCon = new DbConnect();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Accounts frame = new Accounts(new UserAccount());
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Accounts(UserAccount user) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 150, 900, 600);

        JPanel contentPane = new JPanel(new MigLayout("", "[57.00][grow][64.00]", "[][][][grow]"));
        contentPane.setBackground(new Color(253, 245, 230));
        setContentPane(contentPane);

        
        JPanel topPanel = new JPanel(new MigLayout("", "[][46.00][216.00][292.00,grow][][122.00px][117.00][64.00]", "[][][][]"));
        topPanel.setBackground(new Color(253, 245, 230));
        contentPane.add(topPanel, "dock north");
        
                JButton btnBack = new JButton("←");
                btnBack.setBackground(new Color(253, 245, 230));
                btnBack.setFont(new Font("Arial Black", Font.BOLD, 30));
                btnBack.setFocusPainted(false);
                btnBack.setBorder(null);
                btnBack.addActionListener((ActionEvent e) -> {
                    AdminDashboard admin = new AdminDashboard(user);
                    setVisible(false);
                    admin.setVisible(true);
                });
                topPanel.add(btnBack, "cell 1 2,aligny center");
                
                        JLabel lblTitle = new JLabel("A C C O U N T S");
                        lblTitle.setForeground(new Color(210, 180, 140));
                        lblTitle.setFont(new Font("Cooper Black", Font.BOLD, 30));
                        topPanel.add(lblTitle, "cell 2 2 5 1,alignx center");
        
                JLabel lblChoose = new JLabel("Choose Account to update:");
                lblChoose.setFont(new Font("Dialog", Font.BOLD, 14));
                topPanel.add(lblChoose, "cell 2 3,alignx left");
        
                cbAccounts = new JComboBox<>();
                cbAccounts.setBackground(new Color(253, 245, 230));
                cbAccounts.setFont(new Font("Dialog", Font.PLAIN, 14));
                topPanel.add(cbAccounts, "cell 3 3,growx");

        cbStatus = new JComboBox<>(new String[]{"active", "inactive"});
        cbStatus.setBackground(new Color(253, 245, 230));
        cbStatus.setFont(new Font("Dialog", Font.PLAIN, 14));
        topPanel.add(cbStatus, "cell 5 3,growx");

        JButton btnUpdate = new JButton("Update Status");
        btnUpdate.setForeground(new Color(253, 245, 230));
        btnUpdate.setBackground(new Color(210, 180, 140));
        btnUpdate.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnUpdate.setFocusPainted(false);
        btnUpdate.setBorder(null);
        btnUpdate.addActionListener((ActionEvent e) -> updateAccountStatus());
        topPanel.add(btnUpdate, "cell 6 3,grow");

        
        tableModel = new DefaultTableModel(new Object[]{
                "User ID", "First Name", "Middle Name", "Last Name",
                "Email", "User Type", "Contact Number", "Status"
        }, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableAccounts = new JTable(tableModel);
        tableAccounts.setBackground(new Color(253, 245, 230));
        tableAccounts.getTableHeader().setBackground(new Color(245, 222, 179));
        tableAccounts.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tableAccounts);
        contentPane.add(scrollPane, "cell 1 2,growx,aligny baseline");

        loadAccounts();
    }

    private void loadAccounts() {
        try {
            dbCon.connect();
            String select = "SELECT user_id, first_name, middle_name, last_name, email, " +
                    "usertype, contact_number, status FROM users";
            PreparedStatement prep = dbCon.con.prepareStatement(select);
            ResultSet result = prep.executeQuery();

            DefaultComboBoxModel<String> comboAccounts = new DefaultComboBoxModel<>();

            while (result.next()) {
                int userID = result.getInt("user_id");
                String fName = result.getString("first_name");
                String midName = result.getString("middle_name");
                String lName = result.getString("last_name");
                String email = result.getString("email");
                String userType = result.getString("usertype");
                String contactNum = result.getString("contact_number");
                String stat = result.getString("status");

                tableModel.addRow(new Object[]{
                        userID, fName, midName, lName, email, userType, contactNum, stat
                });

                comboAccounts.addElement(userID + " - " + fName + " " + lName);
            }
            cbAccounts.setModel(comboAccounts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAccountStatus() {
        try {
            String selectedAcc = (String) cbAccounts.getSelectedItem();
            if (selectedAcc == null) return;
            String userID = selectedAcc.split(" - ")[0].trim();
            String newStatus = (String) cbStatus.getSelectedItem();
            if (newStatus == null) return;

            dbCon.connect();
            String update = "UPDATE users SET status = ? WHERE user_id = ?";
            PreparedStatement prep = dbCon.con.prepareStatement(update);
            prep.setString(1, newStatus);
            prep.setInt(2, Integer.parseInt(userID));
            int result = prep.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Status updated successfully!");
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int idInTable = (int) tableModel.getValueAt(i, 0);
                    if (idInTable == Integer.parseInt(userID)) {
                        tableModel.setValueAt(newStatus, i, 7);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No rows updated. Check user ID.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating status: " + ex.getMessage());
        }
    }
}

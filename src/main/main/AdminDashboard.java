package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
//import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;

public class AdminDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    DbConnect dbCon = new DbConnect();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AdminDashboard frame = new AdminDashboard(new UserAccount());
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AdminDashboard(UserAccount user) {
    	//logo
//    	ImageIcon framelogo = new ImageIcon("src/resources/images/logo.png");
//		Image logoImage = framelogo.getImage();
//		setIconImage(logoImage);
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 150, 900, 600);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBackground(new Color(253, 245, 230));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(238, 232, 170));
        sidePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
        sidePanel.setPreferredSize(new Dimension(200, 0));
        contentPane.add(sidePanel, BorderLayout.WEST);

        JLabel lblDashboard = new JLabel("A D M I N");
        lblDashboard.setFont(new Font("Dialog", Font.BOLD, 16));
        lblDashboard.setHorizontalAlignment(SwingConstants.CENTER);
        lblDashboard.setForeground(Color.BLACK);
        lblDashboard.setPreferredSize(new Dimension(180, 30));
        sidePanel.add(lblDashboard);

        JButton btnInventoryChanges = createMenuButton("Inventory Changes");
        btnInventoryChanges.addActionListener(e -> {
            InventoryChanges iv = new InventoryChanges(user, AdminDashboard.this);
            setVisible(false);
            iv.setVisible(true);
        });
        sidePanel.add(btnInventoryChanges);

        JButton btnAccounts = createMenuButton("Accounts");
        btnAccounts.addActionListener(e -> {
            Accounts accounts = new Accounts(user);
            setVisible(false);
            accounts.setVisible(true);
        });
        sidePanel.add(btnAccounts);

        JButton btnSalesRecords = createMenuButton("Sales Records");
        btnSalesRecords.addActionListener(e -> {
            SalesRecords records = new SalesRecords(user, AdminDashboard.this);
            setVisible(false);
            records.setVisible(true);
        });
        sidePanel.add(btnSalesRecords);

        JButton btnOrders = createMenuButton("Orders");
        btnOrders.addActionListener(e -> {
            Orders orders = new Orders(user, AdminDashboard.this);
            setVisible(false);
            orders.setVisible(true);
        });
        sidePanel.add(btnOrders);

        JButton btnCreateAccount = createMenuButton("Create Account");
        btnCreateAccount.addActionListener(e -> {
            CreateAccount create = new CreateAccount(user, AdminDashboard.this);
            setVisible(false);
            create.setVisible(true);
        });
        sidePanel.add(btnCreateAccount);

        JButton btnChangePassword = new JButton("Change Password");
        btnChangePassword.setFont(new Font("Dialog", Font.BOLD, 12));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setBackground(new Color(46, 139, 87));
        btnChangePassword.setPreferredSize(new Dimension(180, 30));
        btnChangePassword.setFocusPainted(false);
        btnChangePassword.setBorder(null);
        btnChangePassword.addActionListener(e -> {
            ChangePassword changepass = new ChangePassword(user.getUserId());
            changepass.setVisible(true);
        });
        sidePanel.add(btnChangePassword);

        JButton btnLogout = new JButton("Log Out");
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(178, 34, 34));
        btnLogout.setPreferredSize(new Dimension(180, 30));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(null);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(AdminDashboard.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        });
        sidePanel.add(btnLogout);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(253, 245, 230));
        contentPane.add(mainPanel, BorderLayout.CENTER);

        JPanel topButtonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        topButtonsPanel.setBackground(new Color(253, 245, 230));
        mainPanel.add(topButtonsPanel, BorderLayout.NORTH);

        JButton btnAdd = new JButton("Add Product");
        btnAdd.setForeground(new Color(253, 245, 230));
        btnAdd.setBackground(new Color(46, 139, 87));
        btnAdd.setPreferredSize(new Dimension(130, 25));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(null);
        btnAdd.addActionListener(e -> {
            AddProduct add = new AddProduct(user, AdminDashboard.this);
            setVisible(false);
            add.setVisible(true);
        });
        topButtonsPanel.add(btnAdd);

        JButton btnRemove = new JButton("Remove Product");
        btnRemove.setForeground(new Color(253, 245, 230));
        btnRemove.setPreferredSize(new Dimension(130, 25));
        btnRemove.setBackground(new Color(178, 34, 34));
        btnRemove.setFocusPainted(false);
        btnRemove.setBorder(null);
        btnRemove.addActionListener(e -> {
            RemoveProduct delete = new RemoveProduct(user, AdminDashboard.this);
            setVisible(false);
            delete.setVisible(true);
        });
        topButtonsPanel.add(btnRemove);

        String[] columnNames = { "Product ID", "Product Name", "Price", "No. of Stock", "Action" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable product_table = new JTable(model);
        product_table.setBackground(new Color(253, 245, 230));
        product_table.getTableHeader().setBackground(new Color(245, 222, 179));
        product_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(product_table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        fetch_products(model, product_table, user);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Dialog", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEADING);
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(238, 232, 170));
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setPreferredSize(new Dimension(180, 30));
        return btn;
    }

    public void fetch_products(DefaultTableModel model, JTable product_table, UserAccount user) {
        try {
            dbCon.connect();
            String select = "SELECT * FROM products where product_status = 'listed'";
            PreparedStatement prep = dbCon.con.prepareStatement(select);
            ResultSet result = prep.executeQuery();

            while (result.next()) {
                model.addRow(new Object[] {
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getDouble("product_price"),
                        result.getInt("product_stocks"),
                        "Edit"
                });

                product_table.getColumn("Action").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                    JButton btn = new JButton(value.toString());
                    btn.setBackground(new Color(253, 245, 230));
                    btn.setBorder(null);
                    return btn;
                });
            }

            product_table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int col = product_table.columnAtPoint(evt.getPoint());
                    int row = product_table.rowAtPoint(evt.getPoint());
                    if (col == 4) {
                        String productId = model.getValueAt(row, 0).toString();
                        EditProduct edit = new EditProduct(productId, user, AdminDashboard.this);
                        setVisible(false);
                        edit.setVisible(true);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

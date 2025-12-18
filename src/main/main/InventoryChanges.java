package main;

import java.awt.Color;
import java.awt.Image;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

public class InventoryChanges extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    DbConnect dbCon = new DbConnect();

    public InventoryChanges(UserAccount user, AdminDashboard dashboard) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 150, 750, 550);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(253, 245, 230));
        setContentPane(contentPane);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(253, 245, 230));
        contentPane.add(topPanel, BorderLayout.NORTH);

        ImageIcon backImg = new ImageIcon("src/resources/images/back.png");
        Image image = backImg.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JButton btnBack = new JButton(new ImageIcon(image));
        btnBack.setBackground(new Color(253, 245, 230));
        btnBack.setFocusPainted(false);
        btnBack.setBorder(null);
        btnBack.setPreferredSize(new Dimension(40, 30));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
                dashboard.setVisible(true);
            }
        });
        topPanel.setLayout(new MigLayout("", "[40px][183.00px][][468.00]", "[42px]"));
        topPanel.add(btnBack, "cell 0 0,alignx left,aligny center");
                                                        
            JLabel lblInventoryChanges = new JLabel("Inventory Changes");
            lblInventoryChanges.setHorizontalAlignment(SwingConstants.CENTER);
            lblInventoryChanges.setFont(new Font("Dialog", Font.BOLD, 30));
            lblInventoryChanges.setForeground(new Color(210, 180, 140));                                                    
            topPanel.add(lblInventoryChanges, "cell 3 0,alignx trailing,aligny top, pushx");

        String[] columnNames = { "Product Name", "Type of Change", "Message", "Date", "Updated by" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable trans_table = new JTable(model);
        trans_table.setBackground(new Color(253, 245, 230));
        trans_table.getTableHeader().setBackground(new Color(245, 222, 179));
        trans_table.getTableHeader().setForeground(Color.BLACK);
        trans_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(trans_table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        fetch_transactions(model, trans_table, user);
    }

    public void fetch_transactions(DefaultTableModel model, JTable trans_table, UserAccount user) {
        try {
            dbCon.connect();
            String select = "SELECT u.first_name, u.last_name, p.product_name, i.type_of_change, i.message, i.date "
                    + "FROM inventory_changes i "
                    + "INNER JOIN users u ON i.fk_inventory_changes_user_id = u.user_id "
                    + "INNER JOIN products p ON i.fk_inventory_changes_product_id = p.product_id";

            PreparedStatement prep = dbCon.con.prepareStatement(select);
            ResultSet result = prep.executeQuery();

            while (result.next()) {
                String name = result.getString("first_name") + " " + result.getString("last_name");
                Date sqlDate = result.getDate("date");
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); 
				String formattedDate = sdf.format(sqlDate);
                model.addRow(new Object[] {
                        result.getString("product_name"),
                        result.getString("type_of_change"),
                        result.getString("message"),
                        formattedDate,
                        name
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

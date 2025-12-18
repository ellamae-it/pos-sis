package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;

public class SalesRecords extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	DbConnect dbCon = new DbConnect();
	

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SalesRecords frame = new SalesRecords(new UserAccount());
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
	public SalesRecords(UserAccount user, AdminDashboard dashboard) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 150, 750, 550);
		
		contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(253, 245, 230));
        setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(253, 245, 230));
        contentPane.add(topPanel, BorderLayout.NORTH);
        

		String[] columnNames = {"Transaction ID", "Product Name", "Price At Sale", "Quantity", "Total Amount", "Date"};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }; 
				
				
				
						JTable records_table = new JTable(model);
						records_table.setBackground(new Color(253, 245, 230));
						records_table.getTableHeader().setBackground(new Color(245, 222, 179));
						
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
						
						JLabel lblSalesRecords = new JLabel("Sales Records");
						lblSalesRecords.setHorizontalAlignment(SwingConstants.CENTER);
						lblSalesRecords.setFont(new Font("Dialog", Font.BOLD, 30));
						lblSalesRecords.setForeground(new Color(210, 180, 140));
						topPanel.add(lblSalesRecords, "cell 3 0, alignx right, aligny top, pushx");

						
						
						records_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
						JScrollPane scrollPane = new JScrollPane(records_table);
						scrollPane.setBackground(new Color(253, 245, 230));
						contentPane.add(scrollPane, BorderLayout.CENTER);
						
						fetch_transactions(model, records_table, user);
				

		
	}
	
	public void fetch_transactions(DefaultTableModel model, JTable records_table, UserAccount user) {
		try {
			dbCon.connect();
			String select = "SELECT t.transaction_id, p.product_name, t.price_at_sale, t.quantity, t.date, t.total_amount "
					+"from transactions t inner join products p on t.fk_transactions_products_id = p.product_id "
					+"inner join users u on u.user_id = p.fk_products_user_id";		
			PreparedStatement prep = dbCon.con.prepareStatement(select);
			ResultSet result = prep.executeQuery();
			
			
			
				while (result.next()) {
					Date sqlDate = result.getDate("date");
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); 
					String formattedDate = sdf.format(sqlDate);
				    model.addRow(new Object[] {
				        result.getString("transaction_id"),
				        result.getString("product_name"),
				        result.getDouble("price_at_sale"),
				        result.getInt("quantity"),
				        result.getDouble("total_amount"),
				        formattedDate
				    });
				 

			         
				}
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

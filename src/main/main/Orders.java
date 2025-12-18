package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class Orders extends JFrame {

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
//					Orders frame = new Orders();
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
	public Orders(UserAccount user, AdminDashboard dashboard) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 150, 750, 550);
		
		contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(253, 245, 230));
        setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(253, 245, 230));
        contentPane.add(topPanel, BorderLayout.NORTH);
		
		String[] columnNames = { "Order ID", "Date", "Items", "Total Amount"};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
			JTable	trans_table = new JTable(model);
			trans_table.setBackground(new Color(253, 245, 230));
			trans_table.getTableHeader().setBackground(new Color(245, 222, 179));
			JScrollPane scrollPane = new JScrollPane(trans_table);
			scrollPane.setBounds(40, 74, 683, 442);
			scrollPane.setBackground(new Color(253, 245, 230));
			contentPane.add(scrollPane);
			fetch_orders(model, trans_table, user);
			
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
			
			
			topPanel.add(btnBack, "cell 1 0");
			topPanel.setLayout(new MigLayout("", "[40px][183.00px][][468.00]", "[42px]"));
			
			JLabel lblOrders = new JLabel("Orders");
			lblOrders.setHorizontalAlignment(SwingConstants.CENTER);
			lblOrders.setFont(new Font("Dialog", Font.BOLD, 30));
			lblOrders.setForeground(new Color(210, 180, 140));
			topPanel.add(lblOrders, "cell 3 0,, alignx right, aligny top, pushx");
		    
			
			
	}
	
	public void fetch_orders(DefaultTableModel model, JTable trans_table, UserAccount user) {
		try {
			dbCon.connect();
			String select = "Select  s.sales_id, s.total_amount, s.date, count(fk_transactions_sales_id) as items \n"
					+ "from sales_records s \n"
					+"inner join transactions t on s.sales_id = t.fk_transactions_sales_id\n"
				    +"inner join  products p "
				    +"on  t.fk_transactions_products_id = p.product_id\n"
				    +"group by s.sales_id, s.total_amount, s.date";
							
			PreparedStatement prep = dbCon.con.prepareStatement(select);
			ResultSet result = prep.executeQuery();
			
				while (result.next()) {
					Date sqlDate = result.getDate("date");
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); 
					String formattedDate = sdf.format(sqlDate);
				    model.addRow(new Object[] {
				        
				        result.getInt("sales_id"),
				        formattedDate,	
				        result.getString("items"),			        
				        result.getDouble("total_amount")
				    });
				 

			         
				}
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}

}

}

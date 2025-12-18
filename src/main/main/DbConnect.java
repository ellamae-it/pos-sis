package main;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnect {
	
	private String url = "jdbc:mysql://127.0.0.2:3307/pos-sis";
	private String username = "root";
	private String password = "";
	
	public Connection con;
	
	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,username,password);
			//System.out.println("Database Connected");
		}catch(Exception e) {
			System.err.print("Failed to connect!");
			e.printStackTrace();
		}
	}

}

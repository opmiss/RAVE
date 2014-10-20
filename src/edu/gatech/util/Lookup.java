package edu.gatech.util;

import java.sql.*;

public class Lookup {
	private static final Lookup INSTANCE = new Lookup();
	private static final String DBpath = "../data/Aan.db";
	private static Connection conn;
	private static PreparedStatement ps;
	private static Object result; 
 
	public static Lookup getInstance() {
		return INSTANCE;
	}
	
	public static void print(){
		System.out.println(result.toString()); 
	}
	
	Lookup() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + DBpath);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void getStrength() {
		try {
			ps = conn.prepareStatement("select * from edges where target_id=1000 and type=0");
			ResultSet rs = ps.executeQuery();
			result = rs.getArray("weight").getArray(); 
			rs.close(); 
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Lookup.getInstance().getStrength(); 
		Lookup.print(); 
	}
}

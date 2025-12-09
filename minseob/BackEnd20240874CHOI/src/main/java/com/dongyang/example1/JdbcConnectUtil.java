package com.dongyang.example1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JdbcConnectUtil {
	public static Connection getConnection() { // 항시 사용하는 내용일 경우 static을 통해 객체 생성 필요없이 클래스.메서드로 사용 
		//객체 바깥에서 데이터를 저장해둘 용도로도 static을 사용한다.
		Connection conn = null;
		ResultSet rs = null;
		try {
			String url = "jdbc:mysql://localhost:3306/servletdb";
			String user = "root";
			String password = "password";
			
		//1. 커넥터 로딩
		Class.forName("com.mysql.cj.jdbc.Driver");   // 객체가 아니고 클래스라고  메모리에 커넥터를 올려주는 코드
		conn= DriverManager.getConnection(url, user, password); //java.sql 클래스에 있는 Connection
		
		
		
	}catch(ClassNotFoundException e) {
		System.out.println("Loading Error!!");
		e.printStackTrace();
	} catch (SQLException e) {
		System.out.println("Connection Error!!");
		e.printStackTrace();
	}
		
	return conn;
	}
	public static void close(Connection conn, PreparedStatement pstmt) {
		try{
			conn.close();
			pstmt.close();
		}catch(SQLException e) {
			System.out.println("close error");
			e.printStackTrace();
		}
	}
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) { //select 용
		
		try{
			conn.close();
			pstmt.close();
			rs.close();
		}catch(SQLException e) {
			System.out.println("close error");
			e.printStackTrace();
		}
		
	}
	
	

}

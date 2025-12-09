package com.dongyang.example1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class MemberDAO {
	
	Connection conn = null; //로컬변수라 초기화 해야한다.
	PreparedStatement pstmt = null;
	ResultSet rs= null;
	final String SQL_ALL = "select * from membertbl;";
	final String SQL_LOGIN = "select * from membertbl where memberid= ? and password= ? ;";
	
	public ArrayList<MemberDTO> selectmemberList(){
		
		
		ArrayList <MemberDTO> aList = new ArrayList<MemberDTO>();
		try {
			conn = JdbcConnectUtil.getConnection();
			pstmt = conn.prepareStatement(SQL_ALL);
			rs= pstmt.executeQuery();
				
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				
				dto.setMemberid(rs.getString("name"));
				dto.setPassword(rs.getString("password"));
				dto.setName(rs.getString("name"));
				dto.setEmail(rs.getString("email"));
				aList.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("sql 오류");
			e.printStackTrace();
		}
		finally {
			JdbcConnectUtil.close(conn, pstmt, rs);
		}
		return aList;
		
	}
	public boolean loginCheck(MemberDTO mdto) {
		
			
			boolean loginCheck =false;
			
			try {
				conn = JdbcConnectUtil.getConnection();
				
				//3. SQL로 데이터 조작
				
				pstmt = conn.prepareStatement(SQL_LOGIN);
				
				pstmt.setString(1, mdto.getMemberid());
				pstmt.setString(2, mdto.getPassword());
				//1. pstmt.executeQuery();  //resultset 형태로 객체를 반환 select문 쿼리 시 사용
				//2. pstmt.executeUpdate(); 
				
				rs =pstmt.executeQuery(); 
				
				loginCheck =rs.next(); // 다음 값이 존재하는 경우 true 아닌 경우 false 조건을 실행하고 다음 값으로 이동
					
				
				
			}catch(SQLException e) {
				System.out.println("query error");
				e.printStackTrace();
			}
			finally {
				JdbcConnectUtil.close(conn, pstmt, rs);
			}
			return loginCheck;
				
	}
}

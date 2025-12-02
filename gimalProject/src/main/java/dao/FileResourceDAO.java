package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.FileResourceDTO;
import util.JDBCUtil;

public class FileResourceDAO {
	
	public String getFileUrl(long autoId, String usedType) {
		System.out.println("work DBquery: getFileUrl");
		String sql = "select file_url from file_resource where used_id = ? and used_type = ?;";
		String oldFileName = "";
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
		
        	pstmt.setLong(1, autoId);
        	pstmt.setString(2, usedType);
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	if(rs.next()) {
        		oldFileName = rs.getString("file_url");
        	}
        	return oldFileName;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return oldFileName;
       }
	} 
	
	public List<String> getFileUrls(long autoId, String usedType) {
		System.out.println("work DBquery: getFileUrl");
		List<String> urls = new ArrayList<>();
		String sql = "select file_url from file_resource where used_id = ? and used_type = ?;";
		String oldFileName = "";
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
		
        	pstmt.setLong(1, autoId);
        	pstmt.setString(2, usedType);
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	while(rs.next()) {
        		urls.add(rs.getString("file_url"));
        	}
        	return urls;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return urls;
       }
	}
	
	
	public boolean insertFile(FileResourceDTO dto) {
		System.out.println("work DBquery: insertFile");
		String sql = "Insert into file_resource(file_url, file_name, original_name,"
				+ " file_type, size, used_type, used_id) values(?, ?, ?, ?, ?, ?, ?);";
		
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
           
            pstmt.setString(1, dto.getFileUrl());
            pstmt.setString(2, dto.getFileName());
            pstmt.setString(3, dto.getOriginalName());
   			pstmt.setString(4, dto.getFileType()); 
   			pstmt.setLong(5, dto.getSize());
   			pstmt.setString(6, dto.getUsedType());
   			pstmt.setLong(7, dto.getUsedId());

   			int rs = pstmt.executeUpdate();
   			if(rs>0) {
   				System.out.println("업데이트 성공");
   				return true;
   			}
   			System.out.println("업데이트 실패");
   			return false;
           } catch (SQLException e) {
               e.printStackTrace();
               System.out.println("DB 연결 또는 쿼리 오류");
               return false;
          }
      }	
	public boolean deleteFile(String usedType, long usedId) {
		System.out.println("work DBquery: deleteFile");
		String sql = "delete from file_resource where used_type = ? and used_id = ?";
		
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
        	
        	pstmt.setString(1, usedType);
            pstmt.setLong(2, usedId);

   			int rs = pstmt.executeUpdate();
   			if(rs>0) {
   				System.out.println("삭제 성공");
   				return true;
   			}
   			System.out.println("삭제 실패");
   			return false;
           } catch (SQLException e) {
               e.printStackTrace();
               System.out.println("DB 연결 또는 쿼리 오류");
               return false;
          }
	}
	public boolean isExist(long autoId, String usedType) {
		System.out.println("work DBquery: isExist");
		String sql = "select * from file_resource where used_type = ? and used_id = ?;";
		
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
		
        	pstmt.setString(1, usedType);
        	pstmt.setLong(2, autoId);
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	if(rs.next()) {
        		return true;
        	}
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return false;
       }
		
		return false;
	}
}


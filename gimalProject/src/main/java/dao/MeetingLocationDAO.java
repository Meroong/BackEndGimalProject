package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dto.MeetingLocationDTO;
import dto.UserAddressDTO;
import util.JDBCUtil;

public class MeetingLocationDAO {
	
    // 주소 삽입
    public boolean addLocation(MeetingLocationDTO dto) {
        String sql = "INSERT INTO meeting_location (road_address, jibun_address, addr_detail, latitude, longitude) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            
            pstmt.setString(1, dto.getRoadAddress());
            pstmt.setString(2, dto.getJibunAddress());
			pstmt.setString(3, dto.getAddrDetail());
			pstmt.setDouble(4, dto.getLatitude()); 
			pstmt.setDouble(5, dto.getLongitude());
													 

            if(pstmt.executeUpdate() > 0) {
            	return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return false;
        }
        return false;
    }
    //주소 수정
    public boolean updateLocation(MeetingLocationDTO dto) {
    	String sql = "UPDATE meeting_location SET road_address = ?, jibun_address = ?, addr_detail = ?, latitude = ?, longitude = ?"
    			+ "Where id = ? ;";
    	
    	try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

               
            pstmt.setString(1, dto.getRoadAddress());
            pstmt.setString(2, dto.getJibunAddress());
   			pstmt.setString(3, dto.getAddrDetail());
   			pstmt.setDouble(4, dto.getLatitude()); 
   			pstmt.setDouble(5, dto.getLongitude());
   			pstmt.setDouble(6, dto.getId());
   													 

               if(pstmt.executeUpdate() > 0) {
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

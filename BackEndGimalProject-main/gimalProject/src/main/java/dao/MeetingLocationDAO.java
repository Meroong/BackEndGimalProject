package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dto.MeetingLocationDTO;
import dto.UserAddressDTO;
import util.JDBCUtil;

public class MeetingLocationDAO {
	
	//주소 가져오기 널 반환 가능성 익셉션 처리 잘
	public MeetingLocationDTO getLocation(long locationId) {
		String sql = "select * from meeting_location where id = ?;";
		
		try (Connection con = JDBCUtil.jdbcCon();
        		PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, locationId);
			
			try(ResultSet rs = pstmt.executeQuery();){
				if(rs.next()) {
					MeetingLocationDTO dto = new MeetingLocationDTO();
					dto.setId(locationId);
					dto.setRoadAddress(rs.getString("road_address"));
					dto.setJibunAddress(rs.getString("jibun_address"));
					dto.setAddrDetail(rs.getString("addr_detail"));
					dto.setLatitude(rs.getDouble("latitude"));
					dto.setLongitude(rs.getDouble("longitude"));
					
					return dto;
				}
			}
		}catch(SQLException e) {
			System.out.println("주소 가져오기 에러");
			e.printStackTrace();
		}
		return null;

	}

    // 주소 삽입
    public Long insertLocation(MeetingLocationDTO dto) {
        String sql = "INSERT INTO meeting_location (road_address, jibun_address, addr_detail, latitude, longitude) "
                   + "VALUES (?, ?, ?, ?, ?);";
        try (Connection con = JDBCUtil.jdbcCon();
        		PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            
            pstmt.setString(1, dto.getRoadAddress());
            pstmt.setString(2, dto.getJibunAddress());
			pstmt.setString(3, dto.getAddrDetail());
			pstmt.setDouble(4, dto.getLatitude()); 
			pstmt.setDouble(5, dto.getLongitude());
													 

	        int affectedRows = pstmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Insertion failed, no rows affected.");
	        }
	        //인서트 결과로 생성된 키를 반환
	        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                return generatedKeys.getLong(1); // 생성된 PK 반환
	            } else {
	                throw new SQLException("삽입 실패, 아이디 포함안됨.");
	            }
	        }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
            return null;
        }
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

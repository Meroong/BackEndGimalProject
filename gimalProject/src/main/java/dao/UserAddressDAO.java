package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import dto.UserAddressDTO;
import util.JDBCUtil;

public class UserAddressDAO {

    // 유저 주소 조회
    public UserAddressDTO getAddressByUserId(long userId) {
        String sql = "SELECT * FROM user_address WHERE user_id = ?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserAddressDTO dto = new UserAddressDTO();
                    dto.setUserId(userId);
                    dto.setRoadAddress(rs.getString("road_address"));
                    dto.setJibunAddress(rs.getString("jibun_address"));
                    dto.setAddrDetail(rs.getString("addr_detail"));
                    dto.setLatitude(rs.getDouble("latitude"));
					dto.setLongitude(rs.getDouble("longitude"));
					dto.setDongName(rs.getString("dong_name"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    dto.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return dto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
        }
        return null;
    }

    // 유저 주소 저장 (없으면 insert, 있으면 update)
    public int saveOrUpdate(UserAddressDTO dto) {
        if (getAddressByUserId(dto.getUserId()) != null) {
            return updateAddress(dto);
        } else {
            return insertAddress(dto);
        }
    }

    // 주소 삽입
    private int insertAddress(UserAddressDTO dto) {
        String sql = "INSERT INTO user_address (user_id, road_address, jibun_address, addr_detail) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, dto.getUserId());
            pstmt.setString(2, dto.getRoadAddress());
            pstmt.setString(3, dto.getJibunAddress());
			pstmt.setString(4, dto.getAddrDetail());/*
													 * pstmt.setDouble(5, dto.getLatitude()); pstmt.setDouble(6,
													 * dto.getLongitude());
													 */

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
        }
        return 0;
    }

    // 주소 수정
    private int updateAddress(UserAddressDTO dto) {
    	System.out.println("work DBquery: updateAddress");
        String sql = "UPDATE user_address SET road_address = ?, jibun_address = ?, addr_detail = ?, "
                   + "latitude = ?, longitude = ?, updated_at= CURRENT_TIMESTAMP WHERE user_id = ?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
        	System.out.println(dto.getLatitude());
            pstmt.setString(1, dto.getRoadAddress());
            pstmt.setString(2, dto.getJibunAddress());
            pstmt.setString(3, dto.getAddrDetail());
			pstmt.setDouble(4, dto.getLatitude()); 
			pstmt.setDouble(5, dto.getLongitude());
			pstmt.setLong(6, dto.getUserId());
												 
            
			System.out.println("주소 수정 디비쿼리 성공");
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
        }
        return 0;
    }

    // 주소 삭제
    public int deleteAddress(long userId) {
        String sql = "DELETE FROM user_address WHERE user_id = ?";
        System.out.println("deleteAddress 시작, userId=" + userId);
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {


            pstmt.setLong(1, userId);
            return pstmt.executeUpdate();
           
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 또는 쿼리 오류");
        }
        return 0;
    }
}
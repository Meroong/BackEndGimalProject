package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.ItemDTO;
import util.JDBCUtil;

public class DreamDAO {

	// 1. 아이템 등록
    public boolean insert(ItemDTO dto) {
        String sql = "INSERT INTO item (seller_id, category_id, title, content, price, trade_type, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'AVAILABLE')";
        
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, dto.getSeller_id());
            pstmt.setInt(2, dto.getCategory_id());
            pstmt.setString(3, dto.getTitle());
            pstmt.setString(4, dto.getContent());
            pstmt.setInt(5, dto.getPrice());
            pstmt.setString(6, dto.getTrade_type());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ItemDAO insert 오류: " + e.getMessage());
        }
        return false;
    }
    
    // 1.1. 드림 등록 (가격 0원, 타입 DREAM 고정)
    public boolean insertDream(ItemDTO dto) {
        String sql = "INSERT INTO item (seller_id, category_id, title, content, price, trade_type, status) " +
                     "VALUES (?, ?, ?, ?, 0, 'DREAM', 'AVAILABLE')";
        
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, dto.getSeller_id());
            pstmt.setInt(2, dto.getCategory_id());
            pstmt.setString(3, dto.getTitle());
            pstmt.setString(4, dto.getContent());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ItemDAO insertDream 오류: " + e.getMessage());
        }
        return false;
    }

    // 2. 타입별 목록 조회
    public List<ItemDTO> selectListByType(String tradeType) {
        List<ItemDTO> list = new ArrayList<>();
        
        // item + user + user_address 테이블 조인
        String sql = "SELECT i.*, u.nickname, ua.road_address " +
                     "FROM item i " +
                     "JOIN user u ON i.seller_id = u.auto_id " +
                     "LEFT JOIN user_address ua ON u.auto_id = ua.user_id " +
                     "WHERE i.trade_type = ? " +
                     "ORDER BY i.created_at DESC";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, tradeType);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ItemDTO dto = new ItemDTO();
                    dto.setItem_id(rs.getInt("item_id"));
                    dto.setSeller_id(rs.getInt("seller_id"));
                    dto.setCategory_id(rs.getInt("category_id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setPrice(rs.getInt("price"));
                    dto.setTrade_type(rs.getString("trade_type"));
                    dto.setStatus(rs.getString("status"));
                    dto.setCreated_at(rs.getTimestamp("created_at"));
                    
                    // 조인된 데이터
                    dto.setSeller_nickname(rs.getString("nickname"));
//                    dto.setDong_name(rs.getString("dong_name"));
                    
                    // 주소 파싱
                    String fullAddress = rs.getString("road_address");
                    if (fullAddress != null && fullAddress.contains(" ")) {
                        String[] parts = fullAddress.split(" ");
                        // "서울시 마포구 망원동" -> "마포구" 또는 "망원동" 설정
                        // (보통 2번째나 3번째 어절이 동네 이름)
                        if(parts.length > 1) dto.setDong_name(parts[1]);
                        else dto.setDong_name(parts[0]);
                    } else {
                        dto.setDong_name("동네미정");
                    }
                    
                    dto.setThumbnail(null); 
                    
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ItemDAO selectListByType 오류: " + e.getMessage());
        }
        return list;
    }
}
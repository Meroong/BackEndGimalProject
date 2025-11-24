package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.AdminNoticeDTO;
import util.JDBCUtil;

public class AdminNoticeDAO {

    // 공지 전체 목록 조회
    public List<AdminNoticeDTO> findAll() {
    	
        List<AdminNoticeDTO> list = new ArrayList<>();

        String sql = "SELECT id, title, content, writer, created_at, hit " +
                     "FROM notice " +
                     "ORDER BY id DESC";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                AdminNoticeDTO dto = new AdminNoticeDTO();
                dto.setId(rs.getLong("id"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setWriter(rs.getString("writer"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setHit(rs.getInt("hit"));
                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
 // 공지 등록
    public int insert(AdminNoticeDTO dto) {
        String sql = "INSERT INTO notice (title, content, writer, created_at, hit) " +
                     "VALUES (?, ?, ?, NOW(), 0)";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setString(3, dto.getWriter());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
 // 공지 수정
    public int update(AdminNoticeDTO dto) {
        String sql = "UPDATE notice SET title = ?, content = ? WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setLong(3, dto.getId());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
 // 공지 삭제
    public int delete(long id) {
        String sql = "DELETE FROM notice WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}

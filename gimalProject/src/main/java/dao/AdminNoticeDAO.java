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

        // DB 테이블에 맞게 writer, hit 제거
        String sql = "SELECT id, title, content, created_at " +
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
                dto.setCreatedAt(rs.getTimestamp("created_at"));

                // DB에는 없지만 DTO에는 있는 필드 → 기본값으로 두기
                dto.setWriter(null);
                dto.setHit(0);

                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 공지 등록
    public int insert(AdminNoticeDTO dto) {

        // DB 테이블에 맞게 writer, hit 제거
        String sql = "INSERT INTO notice (title, content) " +
                     "VALUES (?, ?)";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());

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

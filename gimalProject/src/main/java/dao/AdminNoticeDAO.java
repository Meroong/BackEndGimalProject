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
}

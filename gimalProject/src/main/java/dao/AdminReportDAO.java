package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.ReportDTO;
import util.JDBCUtil;

public class AdminReportDAO {

    // 신고 전체 조회 (상태 필터 없이 전부)
    public List<ReportDTO> findAll() {
        List<ReportDTO> list = new ArrayList<>();

        String sql = "SELECT id, reporter_id, target_user_id, target_type, "
                   + "reason, status, created_at "
                   + "FROM report "
                   + "ORDER BY id DESC";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ReportDTO dto = new ReportDTO();
                dto.setId(rs.getLong("id"));
                dto.setReporterId(rs.getLong("reporter_id"));
                dto.setTargetUserId(rs.getLong("target_user_id"));
                dto.setTargetType(rs.getString("target_type"));
                dto.setReason(rs.getString("reason"));
                dto.setStatus(rs.getString("status"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 신고 단건 조회
    public ReportDTO findById(long id) {
        String sql = "SELECT id, reporter_id, target_user_id, target_type, "
                   + "reason, status, created_at "
                   + "FROM report "
                   + "WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ReportDTO dto = new ReportDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setReporterId(rs.getLong("reporter_id"));
                    dto.setTargetUserId(rs.getLong("target_user_id"));
                    dto.setTargetType(rs.getString("target_type"));
                    dto.setReason(rs.getString("reason"));
                    dto.setStatus(rs.getString("status"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    return dto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 신고 상태 변경 (예: PENDING -> RESOLVED)
    public int updateStatus(long id, String status) {
        String sql = "UPDATE report SET status = ? WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setLong(2, id);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

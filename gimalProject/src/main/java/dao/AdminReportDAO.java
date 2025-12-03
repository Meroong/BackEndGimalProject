package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.ReportDTO;
import util.JDBCUtil;

/**
 * 관리자 신고(Report) DAO
 * - AdminNoticeDAO 처럼, 메서드 안에서 JDBCUtil로 커넥션을 열고 닫는 구조
 * - 기본 생성자만 사용 (new AdminReportDAO())
 */
public class AdminReportDAO {

    // 신고 전체 목록 조회
    public List<ReportDTO> findAll() {
        List<ReportDTO> list = new ArrayList<>();

        String sql =
                "SELECT id, reporter_id, target_user_id, target_type, status, reason, created_at " +
                "FROM report " +
                "ORDER BY created_at DESC";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[AdminReportDAO] findAll() 오류");
        }

        return list;
    }

    // 신고 단건 조회
    public ReportDTO findById(long id) {

        String sql =
                "SELECT id, reporter_id, target_user_id, target_type, status, reason, created_at " +
                "FROM report " +
                "WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[AdminReportDAO] findById() 오류");
        }

        return null;
    }

    // 신고 상태 변경 (예: PENDING → RESOLVED)
    public int updateStatus(long id, String status) {

        String sql = "UPDATE report SET status = ? WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setLong(2, id);

            int rows = pstmt.executeUpdate();
            System.out.println("[AdminReportDAO] updateStatus rows = " + rows);
            return rows;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[AdminReportDAO] updateStatus() 오류");
        }

        return 0;
    }

    // ResultSet → ReportDTO 매핑
    private ReportDTO mapRow(ResultSet rs) throws SQLException {
        ReportDTO dto = new ReportDTO();
        dto.setId(rs.getLong("id"));
        dto.setReporterId(rs.getLong("reporter_id"));
        dto.setTargetUserId(rs.getLong("target_user_id"));
        dto.setTargetType(rs.getString("target_type"));
        dto.setStatus(rs.getString("status"));
        dto.setReason(rs.getString("reason"));
        dto.setCreatedAt(rs.getTimestamp("created_at"));
        return dto;
    }
}
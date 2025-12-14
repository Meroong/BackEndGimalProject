package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.ReportDTO;
import util.JDBCUtil;

public class ReportDAO {

    // ★ 신고 상태 변경 (PENDING / RESOLVED 공통)
    public int updateReportStatus(long id, String status) throws SQLException {
    	System.out.println("ReportDAO: updateReportStatus");
        String sql = "UPDATE report SET status = ? WHERE id = ?";
        try (Connection con = JDBCUtil.jdbcCon();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, status);   // "PENDING" 또는 "RESOLVED"
            pstmt.setLong(2, id);
            return pstmt.executeUpdate();
        }
    }
    public boolean insertReport(ReportDTO dto) {
    	System.out.println("ReportDAO: insertReport");
        String sql = """
                INSERT INTO report
                (reporter_id, target_user_id, target_type, reason)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, dto.getReporterId());
            pstmt.setLong(2, dto.getTargetUserId());
            pstmt.setString(3, dto.getTargetType());
            pstmt.setString(4, dto.getReason());

            int result = pstmt.executeUpdate();

            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean hasReported(long reporterId, long targetUserId, String targetType) {
    	System.out.println("ReportDAO: hasReported");
        String sql = """
                SELECT COUNT(*) 
                FROM report
                WHERE reporter_id = ? AND target_user_id = ? AND target_type = ?
                """;

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, reporterId);
            pstmt.setLong(2, targetUserId);
            pstmt.setString(3, targetType);

            try(ResultSet rs = pstmt.executeQuery()){
            	if (rs.next()) {
                    return rs.getInt(1) > 0;  // 이미 신고함
                }
            }

        } catch (Exception e) {
        	System.out.println("DB에러");
            e.printStackTrace();
        }

        return false;
    }
    public boolean existsReport(long reporterId, long targetUserId, String targetType) {
        String sql = """
            SELECT 1
            FROM report
            WHERE reporter_id = ?
              AND target_user_id = ?
              AND target_type = ?
            LIMIT 1
        """;

        try (Connection conn = JDBCUtil.jdbcCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, reporterId);
            ps.setLong(2, targetUserId);
            ps.setString(3, targetType);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // 있으면 true
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

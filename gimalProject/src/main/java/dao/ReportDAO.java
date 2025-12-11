package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReportDAO {

    private Connection conn;

    public ReportDAO(Connection conn) {
        this.conn = conn;
    }

    // ★ 신고 상태 변경 (PENDING / RESOLVED 공통)
    public int updateReportStatus(long id, String status) throws SQLException {
        String sql = "UPDATE report SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);   // "PENDING" 또는 "RESOLVED"
            pstmt.setLong(2, id);
            return pstmt.executeUpdate();
        }
    }

    // ... (기존 신고 목록, 상세 조회 메소드들은 그대로 두면 됩니다.)
}

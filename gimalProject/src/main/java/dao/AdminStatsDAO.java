package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.AdminStatsDTO;
import util.JDBCUtil;

public class AdminStatsDAO {

    // 통계 한 번에 조회
    public AdminStatsDTO getStats() {
        AdminStatsDTO dto = new AdminStatsDTO();

        // 1) 전체 회원 수
        String userSql = "SELECT COUNT(*) AS cnt FROM user";

        // 2) 전체 신고 수
        String reportSql = "SELECT COUNT(*) AS cnt FROM report";

        try (Connection con = JDBCUtil.jdbcCon()) {

            // 전체 회원 수
            try (PreparedStatement pstmt = con.prepareStatement(userSql);
                 ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    dto.setTotalUsers(rs.getInt("cnt"));
                }
            }

            // 전체 신고 수
            try (PreparedStatement pstmt = con.prepareStatement(reportSql);
                 ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    dto.setTotalReports(rs.getInt("cnt"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dto;
    }
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.AdminStatsDTO;
import dto.DailySignupDTO;
import dto.UserTrustRankDTO;
import dto.ReportStatusCountDTO;
import util.JDBCUtil;

public class AdminStatsDAO {

    // ===========================
    // 기본 통계 (기존 코드 유지)
    // ===========================
    public AdminStatsDTO getStats() {
        AdminStatsDTO dto = new AdminStatsDTO();

        // 1) 전체 회원 수
        String userSql   = "SELECT COUNT(*) AS cnt FROM user";

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


    // ===========================
    // ① 상단 요약 카드용 통계
    // ===========================

    // 전체 회원 수
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM user";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 오늘 새로 가입한 회원 수
    public int getTodayNewUsers() {
        String sql = "SELECT COUNT(*) FROM user WHERE DATE(created_at) = CURDATE()";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 전체 드림 게시글 수
    public int getTotalItems() {
        String sql = "SELECT COUNT(*) FROM dream_post";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 전체 거래 수
    public int getTotalTransactions() {
        String sql = "SELECT COUNT(*) FROM transaction";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 미처리 신고 수 (PENDING)
    public int getPendingReports() {
        String sql = "SELECT COUNT(*) FROM report WHERE status = 'PENDING'";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }


    // ===========================
    // ② 최근 N일 회원가입 통계
    // ===========================
    public List<DailySignupDTO> getDailySignupStats(int days) {

        String sql =
                "SELECT DATE(created_at) AS dt, COUNT(*) AS cnt " +
                "FROM user " +
                "WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                "GROUP BY DATE(created_at) " +
                "ORDER BY dt";

        List<DailySignupDTO> list = new ArrayList<>();

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, days);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DailySignupDTO dto =
                            new DailySignupDTO(rs.getDate("dt"), rs.getInt("cnt"));
                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // ===========================
    // ③ 신뢰도 높은 유저 TOP N
    // ===========================
    public List<UserTrustRankDTO> getTopUsersByTrustScore(int limit) {

        String sql =
                "SELECT user_id, nickname, trust_score, created_at " +
                "FROM user " +
                "ORDER BY trust_score DESC, created_at ASC " + 
                "LIMIT ?";

        List<UserTrustRankDTO> list = new ArrayList<>();

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UserTrustRankDTO dto = new UserTrustRankDTO();
                    dto.setUserId(rs.getString("user_id"));
                    dto.setNickname(rs.getString("nickname"));
                    dto.setTrustScore(rs.getInt("trust_score"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // ===========================
    // ④ 신고 상태별 개수 (PENDING / RESOLVED)
    // ===========================
    public List<ReportStatusCountDTO> getReportStatusCounts() {

        String sql =
                "SELECT status, COUNT(*) AS cnt " +
                "FROM report " +
                "GROUP BY status";

        List<ReportStatusCountDTO> list = new ArrayList<>();

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ReportStatusCountDTO dto =
                        new ReportStatusCountDTO(rs.getString("status"),
                                                 rs.getInt("cnt"));
                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}

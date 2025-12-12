package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.AdminMeetingDTO;
import util.JDBCUtil;

/**
 * 관리자용 모임 DAO
 * - meeting 테이블 기준
 * - 컬럼: id, title, content, date, max_members, current_members, cost, tag, created_at, status, creator_id
 *
 * ✅ 추가된 기능(상세 경고용)
 * 1) hasReportedUserInMeeting(meetingId)  : 신고 이력(미처리 PENDING)의 유저가 모임에 포함되어 있는지
 * 2) hasMeetingReport(meetingId)          : 해당 모임 자체에 신고(PENDING)가 있는지 (report.target_id 필요)
 *
 * (옵션) 카운트 메서드도 포함
 */
public class AdminMeetingDAO {

    // 공통으로 한 행을 DTO로 매핑
    private AdminMeetingDTO mapRow(ResultSet rs) throws SQLException {
        AdminMeetingDTO dto = new AdminMeetingDTO();

        dto.setId(rs.getLong("id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setDate(rs.getTimestamp("date"));
        dto.setMaxMembers(rs.getInt("max_members"));
        dto.setCurrentMembers(rs.getInt("current_members"));
        dto.setCost(rs.getInt("cost"));
        dto.setTag(rs.getString("tag"));
        dto.setCreatedAt(rs.getTimestamp("created_at"));
        dto.setStatus(rs.getString("status"));   // ★ 상태는 항상 여기서 세팅

        return dto;
    }

    // ① 전체 목록 (검색 + 상태 필터 + 신고필터(신고유저 포함))
    public List<AdminMeetingDTO> search(String keyword, String statusFilter, String reportFilter) {

        List<AdminMeetingDTO> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id, title, content, date, max_members, current_members, ");
        sb.append("       cost, tag, created_at, status ");
        sb.append("FROM meeting ");
        sb.append("WHERE 1=1 ");

        // 검색어가 있으면 제목 LIKE 검색
        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND title LIKE ? ");
        }

        // 상태 필터
        if (statusFilter != null && !statusFilter.equalsIgnoreCase("ALL")
                && !statusFilter.isEmpty()) {
            sb.append("AND status = ? ");
        }

        // ✅ 신고 필터: "신고된 유저가 포함된 모임"
        if (reportFilter != null && !reportFilter.isBlank()
                && "REPORTED_USER_IN_MEETING".equalsIgnoreCase(reportFilter)) {

            sb.append("AND EXISTS ( ");
            sb.append("   SELECT 1 ");
            sb.append("   FROM report r ");
            sb.append("   WHERE r.target_type = 'USER' ");
            sb.append("     AND r.target_user_id IS NOT NULL ");
            // 미처리 신고만 보려면 아래 주석 해제
            sb.append("     AND r.status = 'PENDING' ");
            sb.append("     AND ( ");
            sb.append("          r.target_user_id = meeting.creator_id ");
            sb.append("          OR EXISTS ( ");
            sb.append("              SELECT 1 FROM meeting_participant mp ");
            sb.append("              WHERE mp.meeting_id = meeting.id ");
            sb.append("                AND mp.user_id = r.target_user_id ");
            sb.append("          ) ");
            sb.append("     ) ");
            sb.append(") ");
        }

        sb.append("ORDER BY id DESC");

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sb.toString())) {

            int idx = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                pstmt.setString(idx++, "%" + keyword.trim() + "%");
            }

            if (statusFilter != null && !statusFilter.equalsIgnoreCase("ALL")
                    && !statusFilter.isEmpty()) {
                pstmt.setString(idx++, statusFilter);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ② 전체 목록 (필터 없이)
    public List<AdminMeetingDTO> findAll() {
        List<AdminMeetingDTO> list = new ArrayList<>();

        String sql =
                "SELECT id, title, content, date, max_members, current_members, " +
                "       cost, tag, created_at, status " +
                "FROM meeting ORDER BY id DESC";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ③ 단건 조회
    public AdminMeetingDTO findById(long id) {
        String sql =
                "SELECT id, title, content, date, max_members, current_members, " +
                "       cost, tag, created_at, status " +
                "FROM meeting WHERE id = ?";

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
        }

        return null;
    }

    // ④ 상태 변경 (OPEN / CLOSED / COMPLETED)
    public int updateStatus(long id, String status) {
        String sql = "UPDATE meeting SET status = ? WHERE id = ?";

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

    // ⑤ 모임 삭제
    public int delete(long id) {
        String sql = "DELETE FROM meeting WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ============================================================
    // ✅ (추가) 상세 페이지 경고용 메서드들
    // ============================================================

    /**
     * ✅ 신고 이력(미처리 PENDING)이 있는 유저가 모임(모임장/참가자)에 포함되어 있는지
     * - DB 수정 없이 바로 동작
     */
    public boolean hasReportedUserInMeeting(long meetingId) {

        String sql =
            "SELECT 1 " +
            "FROM meeting m " +
            "LEFT JOIN meeting_participant mp ON mp.meeting_id = m.id " +
            "WHERE m.id = ? " +
            "AND EXISTS ( " +
            "   SELECT 1 FROM report r " +
            "   WHERE r.target_type = 'USER' " +
            "     AND r.status = 'PENDING' " +
            "     AND r.target_user_id IN (m.creator_id, mp.user_id) " +
            ") " +
            "LIMIT 1";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, meetingId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * ✅ 해당 모임 자체에 신고(PENDING)가 있는지
     * - report 테이블에 target_id 컬럼이 있어야 동작합니다.
     *   (target_type='MEETING', target_id=meeting.id)
     */
    public boolean hasMeetingReport(long meetingId) {

        String sql =
            "SELECT 1 FROM report " +
            "WHERE target_type = 'MEETING' " +
            "  AND target_id = ? " +
            "  AND status = 'PENDING' " +
            "LIMIT 1";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, meetingId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * (옵션) 모임 신고(PENDING) 건수
     * - report.target_id 필요
     */
    public int countPendingMeetingReports(long meetingId) {

        String sql =
            "SELECT COUNT(*) " +
            "FROM report " +
            "WHERE target_type = 'MEETING' " +
            "  AND target_id = ? " +
            "  AND status = 'PENDING'";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, meetingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * (옵션) 모임에 포함된 신고 유저(모임장/참가자) PENDING 건수
     * - “유저가 몇 번 신고됐는지” 총합 카운트입니다.
     */
    public int countPendingReportedUsersInMeeting(long meetingId) {

        String sql =
            "SELECT COUNT(*) " +
            "FROM report r " +
            "WHERE r.target_type = 'USER' " +
            "  AND r.status = 'PENDING' " +
            "  AND r.target_user_id IS NOT NULL " +
            "  AND r.target_user_id IN ( " +
            "      SELECT m.creator_id FROM meeting m WHERE m.id = ? " +
            "      UNION " +
            "      SELECT mp.user_id FROM meeting_participant mp WHERE mp.meeting_id = ? " +
            "  )";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, meetingId);
            ps.setLong(2, meetingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

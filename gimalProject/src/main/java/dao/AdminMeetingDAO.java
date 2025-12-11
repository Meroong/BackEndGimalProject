package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.AdminMeetingDTO;
import util.JDBCUtil;

/**
 * 관리자용 모임 DAO
 * - meeting 테이블 기준
 * - 컬럼 이름은 다음과 같이 가정
 *   id, title, content, date, max_members, current_members, cost, tag, created_at, status
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

    // ① 전체 목록 (검색 + 상태 필터)
    public List<AdminMeetingDTO> search(String keyword, String statusFilter) {

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

        // 상태 필터 (ALL이면 필터링 안 함)
        if (statusFilter != null && !statusFilter.equalsIgnoreCase("ALL")
                && !statusFilter.isEmpty()) {
            sb.append("AND status = ? ");
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
}
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.AdminMeetingDTO;
import util.JDBCUtil;

public class AdminMeetingDAO {

    // 모임 전체 목록
    public List<AdminMeetingDTO> findAll() {
        List<AdminMeetingDTO> list = new ArrayList<>();

        String sql = "SELECT id, title, content, date, location, "
                   + "max_members, current_members, cost, tag, status, created_at "
                   + "FROM meeting "
                   + "ORDER BY id DESC";

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

    // 🔍 검색 + 상태 필터
    // keyword: 제목/내용/태그 검색, status: OPEN/CLOSED/COMPLETED/ALL
    public List<AdminMeetingDTO> search(String keyword, String status) {
        List<AdminMeetingDTO> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id, title, content, date, location, ")
          .append("max_members, current_members, cost, tag, status, created_at ")
          .append("FROM meeting WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND (title LIKE ? OR content LIKE ? OR tag LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sb.append("AND status = ? ");
            params.add(status);
        }

        sb.append("ORDER BY id DESC");

        String sql = sb.toString();

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            // 파라미터 바인딩
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
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

    // 모임 한 건 조회
    public AdminMeetingDTO findById(long id) {
        String sql = "SELECT id, title, content, date, location, "
                   + "max_members, current_members, cost, tag, status, created_at "
                   + "FROM meeting "
                   + "WHERE id = ?";

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

    // 상태 변경 (OPEN / CLOSED / COMPLETED)
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


 // 모임 삭제 (관련 채팅방까지 같이 정리)
    public int delete(long id) {

        String sqlDeleteChatRoom = "DELETE FROM chat_room WHERE meeting_id = ?";
        String sqlDeleteMeeting  = "DELETE FROM meeting WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon()) {

            // 1) 이 모임을 쓰는 채팅방 먼저 삭제
            try (PreparedStatement pstmt = con.prepareStatement(sqlDeleteChatRoom)) {
                pstmt.setLong(1, id);
                int deletedChat = pstmt.executeUpdate();
                System.out.println("[AdminMeetingDAO] delete chat_room by meeting_id=" + id
                        + ", deleted=" + deletedChat);
            }

            // 2) 이제 모임 삭제 (meeting_participant는 ON DELETE CASCADE라 같이 지워짐)
            try (PreparedStatement pstmt = con.prepareStatement(sqlDeleteMeeting)) {
                pstmt.setLong(1, id);
                int deletedMeeting = pstmt.executeUpdate();
                System.out.println("[AdminMeetingDAO] delete meeting id=" + id
                        + ", deleted=" + deletedMeeting);
                return deletedMeeting;  // 1이면 정상 삭제, 0이면 해당 id 없음
            }

        } catch (SQLException e) {
            System.out.println("[AdminMeetingDAO] delete error for id=" + id);
            e.printStackTrace();
            return 0;
        }
    }



    // 공통 매핑 로직
    private AdminMeetingDTO mapRow(ResultSet rs) throws SQLException {
        AdminMeetingDTO dto = new AdminMeetingDTO();
        dto.setId(rs.getLong("id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setDate(rs.getTimestamp("date"));
        dto.setLocation(rs.getString("location"));
        dto.setMaxMembers(rs.getInt("max_members"));
        dto.setCurrentMembers(rs.getInt("current_members"));
        dto.setCost(rs.getInt("cost"));
        dto.setTag(rs.getString("tag"));
        dto.setStatus(rs.getString("status"));
        dto.setCreatedAt(rs.getTimestamp("created_at"));
        return dto;
    }
}

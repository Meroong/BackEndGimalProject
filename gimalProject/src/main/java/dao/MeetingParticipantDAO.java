package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.MeetingParticipantDTO;
import util.JDBCUtil;

public class MeetingParticipantDAO {

    // 특정 모임의 모든 참가자 목록 조회
    public ArrayList<MeetingParticipantDTO> getParticipantsByMeetId(long meetingId) {
        ArrayList<MeetingParticipantDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM meeting_participant WHERE meeting_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MeetingParticipantDTO dto = new MeetingParticipantDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setMeetingId(rs.getLong("meeting_id"));
                    dto.setUserId(rs.getLong("user_id"));
                    dto.setPaid(rs.getBoolean("paid"));
                    dto.setJoinedAt(rs.getTimestamp("joined_at"));
                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("참여자 목록 조회 실패");
        }

        return list;
    }
    
    public boolean markAsPaid(long meetingId, long userId) {
        String sql = "UPDATE meeting_participant SET paid = TRUE WHERE meeting_id = ? AND user_id = ?";
        
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);
            pstmt.setLong(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 유저가 모임에 있는지 조회
    public boolean isParticipant(long meetingId, long userId) {
        MeetingParticipantDTO dto = null;
        String sql = "SELECT * FROM meeting_participant WHERE meeting_id = ? AND user_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);
            pstmt.setLong(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                	return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("참여 여부 조회 실패");
        }

        return false;
    }

    // 참여자 추가 (참가 신청)
    public boolean insertParticipant(MeetingParticipantDTO dto) {
        String sql = "INSERT INTO meeting_participant (meeting_id, user_id) VALUES (?, ?)";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, dto.getMeetingId());
            pstmt.setLong(2, dto.getUserId());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("참여자 추가 실패");
        }

        return false;
    }

    //  참여자 삭제 (모임 탈퇴)
    public boolean deleteParticipant(MeetingParticipantDTO dto) {
        String sql = "DELETE FROM meeting_participant WHERE meeting_id = ? AND user_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, dto.getMeetingId());
            pstmt.setLong(2, dto.getUserId());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("참여자 삭제 실패");
        }

        return false;
    }

    // 참여자 정보 업데이트 (예: 참가비 지불 상태 변경)
    public boolean updateParticipant(MeetingParticipantDTO dto) {
        String sql = "UPDATE meeting_participant SET paid = ? WHERE meeting_id = ? AND user_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setBoolean(1, dto.isPaid());
            pstmt.setLong(2, dto.getMeetingId());
            pstmt.setLong(3, dto.getUserId());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("참여자 업데이트 실패");
        }

        return false;
    }
    //유저 회비 지불 여부 체크 
    public boolean hasUserPaid(long meetingId, long userId) {

        String sql = "SELECT paid FROM meeting_participant WHERE meeting_id = ? AND user_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);
            pstmt.setLong(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("paid"); // true면 결제 완료
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // 기본값: 안 냄
    }

}

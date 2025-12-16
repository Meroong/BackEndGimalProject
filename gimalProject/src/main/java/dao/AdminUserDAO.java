package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.JDBCUtil;

public class AdminUserDAO {

    /**
     * 회원 권한/상태 변경 (정지 / 해제 등)
     */
    public int updateRole(long autoId, String newRole) {

        // TODO: 실제 테이블/컬럼명에 맞게 수정 필요
        String sql = "UPDATE user SET role = ? WHERE auto_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, newRole);
            pstmt.setLong(2, autoId);

            int rows = pstmt.executeUpdate();
            System.out.println("[AdminUserDAO] updateRole rows = " + rows);
            return rows;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[AdminUserDAO] 권한 변경 중 오류");
        }

        return 0;
    }

    /**
     * 회원 탈퇴(삭제)
     *
     * 1) 이 유저와 관련된 report / review / transaction / chat_room / item 삭제
     * 2) 마지막에 user 삭제
     */
    public int deleteUser(long autoId) {

        // 1. 신고 삭제 (reporter, target 둘 다)
        //    실제 테이블/컬럼명에 맞게 수정해서 사용하세요.
        String deleteReportsSql =
                "DELETE FROM report WHERE reporter_id = ? OR target_user_id = ?";


        // 2. 거래 기록 삭제
        String deleteMeetingSql =
                "DELETE FROM meeting WHERE  creator_id = ? ";

        // 3. 채팅방 삭제
        String deleteChatRoomsSql =
                "DELETE FROM chat_room WHERE host_id = ?";

        // 5. 이 유저가 올린 나눔게시글 삭제
        String deleteDreamSql =
                "DELETE FROM dream_post WHERE writer_id = ?";

        // 6. 마지막으로 회원 삭제
        String deleteUserSql =
                "DELETE FROM user WHERE auto_id = ?";

        try (Connection con = JDBCUtil.jdbcCon()) {

            con.setAutoCommit(false);   // 트랜잭션 시작

            try (
                PreparedStatement pstmtReport = con.prepareStatement(deleteReportsSql);
                PreparedStatement pstmtMeeting  = con.prepareStatement(deleteMeetingSql);
                PreparedStatement pstmtChat   = con.prepareStatement(deleteChatRoomsSql);
                PreparedStatement pstmtItem   = con.prepareStatement(deleteDreamSql);
                PreparedStatement pstmtUser   = con.prepareStatement(deleteUserSql);
            ) {
                // 1) 신고 삭제
                pstmtReport.setLong(1, autoId);
                pstmtReport.setLong(2, autoId);
                pstmtReport.executeUpdate();



                // 3) 모임 기록 삭제
                pstmtMeeting.setLong(1, autoId);
                pstmtMeeting.executeUpdate();
                
                // 4) 채팅방 삭제
                pstmtChat.setLong(1, autoId);
                pstmtChat.executeUpdate();

                // 5) 상품 삭제
                pstmtItem.setLong(1, autoId);
                pstmtItem.executeUpdate();

                // 6) 회원 삭제
                pstmtUser.setLong(1, autoId);
                int rows = pstmtUser.executeUpdate();

                con.commit();
                System.out.println("[AdminUserDAO] deleteUser 성공, 삭제된 user row 수 = " + rows);
                return rows;

            } catch (SQLException e) {
                con.rollback();     // 하나라도 실패하면 전체 롤백
                e.printStackTrace();
                System.out.println("[AdminUserDAO] 회원 삭제 중 오류 → 롤백");
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[AdminUserDAO] 커넥션/트랜잭션 설정 오류");
        }

        return 0;
    }

}
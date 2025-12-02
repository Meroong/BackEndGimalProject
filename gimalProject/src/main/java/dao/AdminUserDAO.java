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
        String deleteReportsSql =
                "DELETE FROM report WHERE reporter_id = ? OR target_user_id = ?";

        // 2. 리뷰 삭제 (작성자/대상 둘 다)
        //   ※ 실제 테이블 컬럼명이 다르면 여기만 맞게 바꿔 주세요.
        String deleteReviewsSql =
                "DELETE FROM review WHERE reviewer_id = ? OR reviewee_id = ?";

        // 3. 거래 기록 삭제
        String deleteTransactionsSql =
                "DELETE FROM transaction WHERE buyer_id = ? OR seller_id = ?";

        // 4. 채팅방 삭제
        String deleteChatRoomsSql =
                "DELETE FROM chat_room WHERE buyer_id = ? OR seller_id = ?";

        // 5. 이 유저가 올린 상품 삭제
        String deleteItemsSql =
                "DELETE FROM item WHERE seller_id = ?";

        // 6. 마지막으로 회원 삭제
        String deleteUserSql =
                "DELETE FROM user WHERE auto_id = ?";

        try (Connection con = JDBCUtil.jdbcCon()) {

            con.setAutoCommit(false);   // 트랜잭션 시작

            try (
                PreparedStatement pstmtReport = con.prepareStatement(deleteReportsSql);
                PreparedStatement pstmtReview = con.prepareStatement(deleteReviewsSql);
                PreparedStatement pstmtTrans  = con.prepareStatement(deleteTransactionsSql);
                PreparedStatement pstmtChat   = con.prepareStatement(deleteChatRoomsSql);
                PreparedStatement pstmtItem   = con.prepareStatement(deleteItemsSql);
                PreparedStatement pstmtUser   = con.prepareStatement(deleteUserSql);
            ) {
                // 1) 신고 삭제
                pstmtReport.setLong(1, autoId);
                pstmtReport.setLong(2, autoId);
                pstmtReport.executeUpdate();

                // 2) 리뷰 삭제
                pstmtReview.setLong(1, autoId);
                pstmtReview.setLong(2, autoId);
                pstmtReview.executeUpdate();

                // 3) 거래 기록 삭제
                pstmtTrans.setLong(1, autoId);
                pstmtTrans.setLong(2, autoId);
                pstmtTrans.executeUpdate();

                // 4) 채팅방 삭제
                pstmtChat.setLong(1, autoId);
                pstmtChat.setLong(2, autoId);
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
 // 회원 삭제(탈퇴) - PK(autoId) 기준
    public int deleteUser(long autoId) {
        // ★ 여기 테이블명 / 컬럼명은 실제 DB에 맞게 바꿔야 합니다.
        //    예시로 users / autoId 를 사용했습니다.
        String sql = "DELETE FROM users WHERE autoId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, autoId);
            int result = pstmt.executeUpdate();
            System.out.println("[AdminUserDAO] deleteUser autoId=" + autoId + ", result=" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}

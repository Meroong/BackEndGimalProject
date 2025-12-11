package dao;

import java.sql.*;
import dto.UserWalletDTO;
import util.JDBCUtil;

public class WalletDAO {

    public UserWalletDTO findByUserId(long userId) {
        String sql = "SELECT user_id, balance FROM user_wallet WHERE user_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserWalletDTO dto = new UserWalletDTO();
                    dto.setUserId(rs.getLong("user_id"));
                    dto.setBalance(rs.getInt("balance"));
                    return dto;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("지갑 조회 실패");
        }

        return null;
    }

    public void insertNew(long userId) {
        String sql = "INSERT INTO user_wallet (user_id, balance) VALUES (?, 0)";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("지갑 생성 실패");
        }
    }

    public void updateBalance(long userId, int balance) {
        String sql = "UPDATE user_wallet SET balance = ? WHERE user_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, balance);
            pstmt.setLong(2, userId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("잔액 업데이트 실패");
        }
    }

    public void insertHistory(long userId, String type, int amount, String desc) {
        String sql = "INSERT INTO wallet_history (user_id, type, amount, description) VALUES (?, ?, ?, ?)";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setString(2, type);
            pstmt.setInt(3, amount);
            pstmt.setString(4, desc);
            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("포인트 내역 저장 실패");
        }
    }
}

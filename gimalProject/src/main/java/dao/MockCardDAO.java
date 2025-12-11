package dao;

import java.sql.*;
import util.JDBCUtil;

public class MockCardDAO {

    public boolean checkAndUseCard(
            String cardNumber,
            String cvc,
            String cardPw,
            int amount
    ) {
    	
        String selectSql = "SELECT id, balance FROM mock_card WHERE card_number = ? AND cvc = ? AND password = ?";
        String updateSql = "UPDATE mock_card SET balance = balance - ? WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(selectSql)) {

            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cvc);
            pstmt.setString(3, cardPw);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) return false;

                int balance = rs.getInt("balance");
                long cardId = rs.getLong("id");

                if (balance < amount) return false;

                try (PreparedStatement up = con.prepareStatement(updateSql)) {
                    up.setInt(1, amount);
                    up.setLong(2, cardId);
                    up.executeUpdate();
                }

                return true;
            }

        } catch (Exception e) {
            throw new RuntimeException("카드 승인 처리 중 오류");
        }
    }
}

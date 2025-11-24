package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.JDBCUtil;

public class AdminUserDAO {

    // 회원 role 변경 (정지 / 해제에 사용)
    public int updateRole(long autoId, String newRole) {

        String sql = "UPDATE user SET role = ? WHERE auto_id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, newRole);
            pstmt.setLong(2, autoId);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("관리자 - 회원 role 변경 중 오류");
        }

        return 0;
    }
}

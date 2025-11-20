package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import dto.UserDTO;
import util.JDBCUtil;

public class UserDAO {

    // 회원 정보 업데이트
    public void updateUser(UserDTO dto) {
        StringBuilder sql = new StringBuilder("UPDATE user SET ");
        int fieldCount = 0;

        try (Connection con = JDBCUtil.jdbcCon()) {
            if (con == null) throw new RuntimeException("DB 연결 실패");

            if (dto.getUserId() != null) { sql.append("user_id = ?, "); fieldCount++; }
            if (dto.getUserPassword() != null) { sql.append("user_password = ?, "); fieldCount++; }
            if (dto.getNickname() != null) { sql.append("nickname = ?, "); fieldCount++; }
            if (dto.getTrustScore() != 0) { sql.append("trust_score = ?, "); fieldCount++; }
            if (dto.getAddressId() != 0) { sql.append("address_id = ?, "); fieldCount++; }
            if (dto.getAddressDetail() != null) { sql.append("address_detail = ?, "); fieldCount++; }

            if (fieldCount == 0) return; // 업데이트할 필드가 없으면 종료

            // 마지막 콤마 제거
            sql.setLength(sql.length() - 2);
            sql.append(" WHERE auto_id = ?");

            try (PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
                int index = 1;
                if (dto.getUserId() != null) pstmt.setString(index++, dto.getUserId());
                if (dto.getUserPassword() != null) pstmt.setString(index++, dto.getUserPassword());
                if (dto.getNickname() != null) pstmt.setString(index++, dto.getNickname());
                if (dto.getTrustScore() != 0) pstmt.setInt(index++, dto.getTrustScore());
                if (dto.getAddressId() != 0) pstmt.setInt(index++, dto.getAddressId());
                if (dto.getAddressDetail() != null) pstmt.setString(index++, dto.getAddressDetail());
                pstmt.setInt(index, dto.getAutoId());

                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
    }

    // 회원가입
    public boolean insert(UserDTO dto) {
        String sql = "INSERT INTO user (user_id, user_password, user_name, nickname, role, address_id, address_detail) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, dto.getUserId());
            pstmt.setString(2, dto.getUserPassword());
            pstmt.setString(3, dto.getUserName());
            pstmt.setString(4, dto.getNickname());
            pstmt.setString(5, dto.getRole());
            pstmt.setInt(6, dto.getAddressId());
            pstmt.setString(7, dto.getAddressDetail());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
        return false;
    }

    // 로그인용 검색
    public UserDTO searchForLogin(String userId, String userPassword) {
        String sql = "SELECT * FROM user WHERE user_id = ? AND user_password = ?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, userPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserDTO dto = new UserDTO();
                    dto.setAutoId(rs.getInt("auto_id"));
                    dto.setUserId(rs.getString("user_id"));
                    dto.setRole(rs.getString("role"));
                    return dto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
        return null;
    }

    // auto_id로 회원 검색
    public UserDTO searchByAutoId(int autoId) {
        String sql = "SELECT * FROM user WHERE auto_id = ?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {


            pstmt.setInt(1, autoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserDTO dto = new UserDTO();
                    dto.setAutoId(autoId);
                    dto.setUserId(rs.getString("user_id"));
                    dto.setUserPassword(rs.getString("user_password"));
                    dto.setUserName(rs.getString("user_name"));
                    dto.setNickname(rs.getString("nickname"));
                    dto.setTrustScore(rs.getInt("trust_score"));
                    dto.setRole(rs.getString("role"));
                    dto.setAddressId(rs.getInt("address_id"));
                    dto.setAddressDetail(rs.getString("address_detail"));
                    dto.setUpdatedAt(rs.getTimestamp("updated_at"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    return dto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
        return null;
    }

    // 회원 삭제
    public void delete(int autoId) {
        String sql = "DELETE FROM user WHERE auto_id = ?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {


            pstmt.setInt(1, autoId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
    }

    // ID 중복 체크
    public boolean isUserIdDuplicate(String userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE user_id = ?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
        return false;
    }

    // 닉네임 중복 체크 디버깅용
    public boolean isNicknameDuplicate(String nickname) {
        String sql = "SELECT COUNT(*) FROM user WHERE nickname = ?";
        System.out.println("isNicknameDuplicate 호출: nickname=" + nickname);

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            System.out.println("DB 연결 성공!");

            pstmt.setString(1, nickname);
            System.out.println("SQL 준비 완료: " + sql);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("중복 갯수: " + count);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
        return false;
    }

}

package dao;

import dto.WeatherDTO;
import util.JDBCUtil;

import java.sql.*;

public class WeatherDAO {

    public int insertWeather(WeatherDTO dto) {
        String sql = "INSERT INTO weather_data (temp, weather, pm10, created_at) VALUES (?, ?, ?, ?)";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setDouble(1, dto.getTemperature());
            pstmt.setString(2, dto.getWeather());
            pstmt.setInt(3, dto.getPm10());
            pstmt.setTimestamp(4, dto.getCreatedAt());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public WeatherDTO getLatestWeather() {
        String sql = "SELECT temp, weather, pm10, created_at FROM weather_data ORDER BY created_at DESC LIMIT 1";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return new WeatherDTO(
                    rs.getDouble("temp"),
                    rs.getString("weather"),
                    rs.getInt("pm10"),
                    rs.getTimestamp("created_at")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

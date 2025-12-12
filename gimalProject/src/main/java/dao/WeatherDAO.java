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

    public WeatherDTO findRecentByLocation(double lat, double lon, int minutes) {
        String sql = """
            SELECT temp, weather, pm10, latitude, longitude, created_at
            FROM weather_data
            WHERE latitude = ? AND longitude = ?
              AND created_at >= NOW() - INTERVAL ? MINUTE
            ORDER BY created_at DESC
            LIMIT 1
        """;

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, lat);
            ps.setDouble(2, lon);
            ps.setInt(3, minutes);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    WeatherDTO dto =new WeatherDTO();
                        dto.setTemperature(rs.getDouble("temp"));
                        dto.setWeather(rs.getString("weather"));
                        dto.setPm10(rs.getInt("pm10"));
                        dto.setCreatedAt(rs.getTimestamp("created_at"));
                        dto.setLatitude(rs.getDouble("latitude"));
                        dto.setLongitude(rs.getDouble("longitude"));
                        return dto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

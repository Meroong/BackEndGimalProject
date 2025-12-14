package service;

import dto.WeatherDTO;
import dao.WeatherDAO;
import com.google.gson.*;

import java.sql.Timestamp;
import java.io.*;
import java.net.*;

public class WeatherService {

    private WeatherDAO weatherDAO = new WeatherDAO();

    /**
     * 🌤 외부에서 호출하는 유일한 메서드
     * - 위도/경도 기준
     * - 30분 캐시
     */
    public WeatherDTO getWeather(double lat, double lon) {

        // DB 캐시 조회
        WeatherDTO cached =
            weatherDAO.findRecentByLocation(lat, lon, 30);

        if (cached != null) {
            System.out.println("🌤 DB 캐시 사용");
            return cached;
        }

        // API 호출
        System.out.println("🌤 API 호출");
        String jsonStr = callAnAPI(lat, lon);
        if (jsonStr == null) return null;

        // JSON → DTO
        WeatherDTO dto = parseWeather(jsonStr, lat, lon);

        //DB 저장
        weatherDAO.insertWeather(dto);

        return dto;
    }

    /**
     * 🌐 OpenWeather API 호출
     */
    private String callAnAPI(double lat, double lon) {

        String key = "fcdf715f2e4faa898d33ff124104cafe";
        String apiUrl =
            "https://api.openweathermap.org/data/3.0/onecall"
            + "?lat=" + lat
            + "&lon=" + lon
            + "&appid=" + key
            + "&units=metric"
            + "&lang=kr";

        try {
            HttpURLConnection conn =
                (HttpURLConnection) new URL(apiUrl).openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader br =
                new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);

            br.close();
            conn.disconnect();

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 📦 JSON → WeatherDTO 변환
     */
    private WeatherDTO parseWeather(String jsonStr, double lat, double lon) {

        JsonObject json =
            JsonParser.parseString(jsonStr).getAsJsonObject();

        double temp =
            json.getAsJsonObject("current")
                .get("temp").getAsDouble();

        String main =
            json.getAsJsonArray("daily")
                .get(0).getAsJsonObject()
                .getAsJsonArray("weather")
                .get(0).getAsJsonObject()
                .get("main").getAsString();

        String weather;
        switch (main) {
            case "Clear": weather = "맑음"; break;
            case "Clouds": weather = "흐림"; break;
            case "Rain": weather = "비"; break;
            case "Drizzle": weather = "이슬비"; break;
            case "Thunderstorm": weather = "천둥번개"; break;
            case "Snow": weather = "눈"; break;
            default: weather = "기타";
        }

        WeatherDTO dto = new WeatherDTO();
        dto.setTemperature(temp);
        dto.setWeather(weather);
        dto.setPm10(50); // 임시값
        dto.setLatitude(lat);
        dto.setLongitude(lon);
        dto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return dto;
    }
}

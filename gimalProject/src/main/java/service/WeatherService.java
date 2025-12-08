package service;

import dto.WeatherDTO;
import dao.WeatherDAO;
import com.google.gson.*;

import java.sql.Timestamp;
import java.io.*;
import java.net.*;

public class WeatherService {

    private WeatherDAO weatherDAO = new WeatherDAO();

    // API 호출 (JSON 반환)
    public String callAnAPI(double lat, double lon) { String key = "fcdf715f2e4faa898d33ff124104cafe";
    	String apiUrl = "https://api.openweathermap.org/data/3.0/onecall?lat=" + lat + "&lon=" + lon + "&appid=" + key + "&units=metric&lang=kr"; 
    	try { 
    		URL url = new URL(apiUrl); 
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
    		conn.setRequestMethod("GET"); 
    		conn.setRequestProperty("Content-Type", "application/json"); 
    		int status = conn.getResponseCode();
    		if (status != 200) 
    			throw new RuntimeException("API 요청 실패: " + status);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	    	StringBuilder sb = new StringBuilder(); 
	    	String line; while ((line = br.readLine()) != null) sb.append(line);
	    	br.close(); 
	    	conn.disconnect(); 
	    	return sb.toString(); 
	    } catch (Exception e) { e.printStackTrace(); return null; } }
    
    // JSON -> WeatherDTO 변환
    public WeatherDTO parseWeather(String jsonStr) {
        JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
        double temp = json.getAsJsonObject("current").get("temp").getAsDouble();
        JsonObject daily0 = json.getAsJsonArray("daily").get(0).getAsJsonObject();
        JsonArray weatherArr = daily0.getAsJsonArray("weather");
        String weather = weatherArr.get(0).getAsJsonObject().get("main").getAsString();

        switch (weather) {
            case "Clear": weather = "맑음"; break;
            case "Clouds": weather = "흐림"; break;
            case "Rain": weather = "비"; break;
            case "Drizzle": weather = "이슬비"; break;
            case "Thunderstorm": weather = "천둥번개"; break;
            case "Snow": weather = "눈"; break;
            default: weather = "기타";
        }

        int pm10 = 50; // 실제 PM10 API 호출 또는 DB 연동 필요

        return new WeatherDTO(temp, weather, pm10, new Timestamp(System.currentTimeMillis()));
    }

    // DB 저장
    public void saveWeather(double lat, double lon) {
        String jsonStr = callAnAPI(lat, lon);
        if(jsonStr == null) return;
        WeatherDTO dto = parseWeather(jsonStr);
        weatherDAO.insertWeather(dto);
        System.out.println("DB 저장 완료: " + dto.getWeather());
    }

    // DB 조회
    public WeatherDTO getLatestWeather() {
        return weatherDAO.getLatestWeather();
    }
}

package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.MeetingDAO;
import dao.MeetingLocationDAO;
import dto.MeetingDTO;
import dto.MeetingLocationDTO;
import dto.ResponseDTO;

public class MeetingService {
	
	
	public String callAnAPI(double lat, double lon) {
		String key = "fcdf715f2e4faa898d33ff124104cafe";
		JsonObject respJson = null;
		BufferedReader br= null;

		String apiUrl = "https://api.openweathermap.org/data/3.0/onecall?lat="+lat+"&lon="+lon+"&appid="+key+"&units=metric&lang=kr";

		HttpURLConnection conn = null;
		//오픈웨더맵API 호출
		try {
			URL url = new URL(apiUrl);
			conn = (HttpURLConnection) url.openConnection();
			
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            
            int status = conn.getResponseCode();
            if(status !=200) {
            	System.out.println("요청실패: "+status);
            	throw new RuntimeException("API 요청 실패: " + status);
            }
          //데이터 불러오기 
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        
			StringBuilder sb = new StringBuilder();
	        String line;

	        while((line = br.readLine()) != null) {
	            sb.append(line);
	        }
	        System.out.println(sb);
	        return sb.toString();

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;

	    } finally {
	        if(br != null) try { br.close(); } catch(Exception ignored){}
	        if(conn != null) conn.disconnect();
	    }
	}

	
	  public String extractWeather(String jsonStr, int dayIndex) {
		  //객체화 
		  JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject(); 
		  
		  //특정 데이터 daily배열 추출
		  JsonArray dailyArr = json.getAsJsonArray("daily");
		  
		  if (dailyArr == null || dailyArr.size() <= dayIndex) {
			  System.out.println("배열 데이터 오류 or 날짜 인덱스 인자 오류");
			  return null;
		  }
		  //배열 중 해당하는 날에 데이터만 가져오기
		  JsonObject dayObj = dailyArr.get(dayIndex).getAsJsonObject();
		  
		  //그 중에 weather 배열만 추출
		  //데이터 예시) 
			/* "weather": [{ "main": "Rain", "description": "light rain" }]*/
		  
		  JsonArray weatherArr = dayObj.getAsJsonArray("weather");
		  if (weatherArr == null || weatherArr.size() == 0) {
			    System.out.println("weather 데이터 없음");
			    return null;
			}
		  
		  JsonObject weatherObj = weatherArr.get(0).getAsJsonObject();
		  
		  //데이터 중 날씨 상태정보만 필요해서 메인을 추출
		  String main = weatherObj.get("main").getAsString();
		  
		    switch (main) {
		        case "Clear": return "맑음";
		        case "Clouds": return "흐림";
		        case "Rain": return "비";
		        case "Drizzle": return "이슬비";
		        case "Thunderstorm": return "천둥번개";
		        case "Snow": return "눈";
	
		        case "Mist": return "안개";
		        case "Smoke": return "연기";
		        case "Haze": return "실안개";
		        case "Dust": return "먼지";
		        case "Fog": return "안개";
		        case "Sand": return "모래바람";
		        case "Ash": return "화산재";
		        case "Squall": return "돌풍";
		        case "Tornado": return "토네이도";
	
		        case "Hot": return "폭염";
		        case "Cold": return "한파";
		        case "Extreme": return "악천후";
	
		        default: 
		            return "기타"; // 혹시라도 새로운 값이 나오면 안전하게 표시
	    }
	  }
	 
	public boolean updateLocation(MeetingLocationDTO dto) {
		System.out.println("Service: updateLocation");
	    if (dto == null) {
	        throw new IllegalArgumentException("DTO가 존재하지 않습니다.");
	    }

	    // locationId 체크
	    if (dto.getId() == null) {
	        throw new IllegalArgumentException("locationId 값이 없습니다.");
	    }

	    // DAO 호출 (위치 정보 업데이트)
	    return new MeetingLocationDAO().updateLocation(dto);
	}
	
	public boolean saveLocation(String roadAddress, String jibunAddress, String addrDetail, String latitudeStr, String longitudeStr) {
		System.out.println("Service: saveLocation");
		MeetingLocationDTO locationDto = new MeetingLocationDTO();
		//디폴트값
		double lat = 37.1;
		double lon = 107.2;
		locationDto.setRoadAddress(roadAddress);
		locationDto.setJibunAddress(jibunAddress);
		locationDto.setAddrDetail(addrDetail);
		
		  if (latitudeStr != null && !latitudeStr.isEmpty()) {
			  lat = Double.parseDouble(latitudeStr);
			  locationDto.setLatitude(lat);
		  }
		  if (longitudeStr != null && !longitudeStr.isEmpty()) {
			  lon = Double.parseDouble(longitudeStr);
			  locationDto.setLongitude(lon); 
		  }
		//위치 정보 저장 table=meeting_location
		return new MeetingLocationDAO().addLocation(locationDto);
	}
	
	public boolean saveMeetingInfo(MeetingDTO dto) {
		return new MeetingDAO().insert(dto);
	}
	public boolean updateMeetingInfo(MeetingDTO dto, double latitude, double longitude) {
		System.out.println("Service: updateMeetingInfo");
		if (dto.getDate() == null) {
		    throw new IllegalArgumentException("모임 날짜가 존재하지 않습니다.");
		}
		
		LocalDate meetingDate = dto.getDate().toLocalDateTime().toLocalDate();
	    LocalDate today = LocalDate.now();

	    long diff = ChronoUnit.DAYS.between(today, meetingDate);

	    int dayIndex = (int) diff;

	    // 유효성 검사 (0~7일 안에 있어야)
	    if (dayIndex < 0 || dayIndex > 7) {
	        throw new RuntimeException("날씨 조회는 7일 이내 날짜만 가능합니다");
	    }
	    
	    String jsonStr = callAnAPI(latitude, longitude);
	    System.out.println("jsonStr:"+jsonStr);
	    if (jsonStr == null) {
	        dto.setWeather("정보없음");
	        return new MeetingDAO().updateMeet(dto);
	    }
	    String weather = extractWeather(jsonStr, dayIndex);
	    dto.setWeather(weather);
		
		return new MeetingDAO().updateMeet(dto);
	}
	//게시글 작성
	
	//게시글 수정
	
	//게시글 삭제
	
}

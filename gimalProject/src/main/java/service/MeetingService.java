package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonObject;

import dao.MeetingLocationDAO;
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

	        return sb.toString();

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;

	    } finally {
	        if(br != null) try { br.close(); } catch(Exception ignored){}
	        if(conn != null) conn.disconnect();
	    }
	}
	
	public boolean saveLocation(String roadAddress, String jibunAddress, String addrDetail, String latitudeStr, String longitudeStr) {
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
	//게시글 작성
	
	//게시글 수정
	
	//게시글 삭제
	
}

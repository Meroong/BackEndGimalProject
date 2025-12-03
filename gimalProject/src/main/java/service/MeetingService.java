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
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.MeetingDAO;
import dao.MeetingLocationDAO;
import dto.MeetingDTO;
import dto.MeetingInfoDTO;
import dto.MeetingLocationDTO;
import dto.ResponseDTO;

public class MeetingService {
	public MeetingInfoDTO getMeetingInfo(long meetingId) {
		System.out.println("Service: getMeeting");
		MeetingInfoDTO infoDto = new MeetingInfoDTO();
		
		//주소 가져오기
		MeetingLocationDTO locationDto = new MeetingLocationDAO().getLocation(meetingId);
		
		if(locationDto == null) {
			throw new IllegalArgumentException("MeetingLocationDTO가 존재하지않습니다.");
		}
		//모임정보 가져오기
		MeetingDTO meetingDto = new MeetingDAO().getPostDetail();
		
		if(meetingDto == null) {
			throw new IllegalArgumentException("MeetingDTO가 존재하지않습니다.");
		}
		    
		infoDto.setMeetingId(meetingDto.getMeetingId());   // id
		infoDto.setTitle(meetingDto.getTitle());           // 제목
		infoDto.setContent(meetingDto.getContent());       // 설명
		infoDto.setDate(meetingDto.getDate());             // 모임 날짜
		infoDto.setLocationId(meetingDto.getLocationId()); // 장소 ID (FK)
		infoDto.setMaxMembers(meetingDto.getMaxMembers()); // 최대 인원
		infoDto.setCurrentMembers(meetingDto.getCurrentMembers()); // 현재 인원
		infoDto.setCost(meetingDto.getCost());             // 참가비
		infoDto.setTag(meetingDto.getTag());               // 태그 문자열
		infoDto.setStatus(meetingDto.getStatus());         // OPEN / CLOSED / COMPLETED
		infoDto.setCreatedAt(meetingDto.getCreatedAt());   // 생성일
		infoDto.setUpdatedAt(meetingDto.getUpdatedAt());   // 수정일
		infoDto.setWeather(meetingDto.getWeather());       // 날씨
		
		infoDto.setLocationId(locationDto.getId());            // 모임 장소 ID
		infoDto.setRoadAddress(locationDto.getRoadAddress());  // 도로명 주소
		infoDto.setJibunAddress(locationDto.getJibunAddress()); // 지번 주소
		infoDto.setAddrDetail(locationDto.getAddrDetail());    // 상세 주소
		infoDto.setLatitude(locationDto.getLatitude());        // 위도
		infoDto.setLongitude(locationDto.getLongitude());      // 경도

		
		return infoDto;
		
		//새로 게시용 DTO를 만들자 
	}
	public ArrayList<MeetingDTO> getMeetingList(){
		//meeting_id, title, date, location, maxMembers, currentMembers, tag, status
		System.out.println("Service: getMeetingList");
		
		//모임정보 가져오기 
		return new MeetingDAO().getPostList(); 
	}
	
	 //모임 장소 업데이트
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
	//모임 장소 디비저장
	public long insertLocation(MeetingLocationDTO dto) throws Exception {
		System.out.println("Service: insertLocation");

		//위치 정보 저장 table=meeting_location
		Long rs = new MeetingLocationDAO().insertLocation(dto);
		
		if(rs != null) {
			return rs;
		}
			throw new Exception("모임장소 아이디값 없음");
	}
	//모임생성
	public boolean insertMeetingInfo(MeetingDTO dto, double latitude, double longitude) {
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
	        return new MeetingDAO().insert(dto);
	    }
	    String weather = extractWeather(jsonStr, dayIndex);
	    System.out.println(weather);
	    dto.setWeather(weather);
		return new MeetingDAO().insert(dto);
	}
	
	//모임 업데이트
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
	
	//오픈웨더 API 호출용 서비스
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
	
	//게시글 삭제
	
}

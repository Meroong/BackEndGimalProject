package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.FileResourceDAO;
import dao.MeetingDAO;
import dao.MeetingLocationDAO;
import dao.MeetingParticipantDAO;
import dto.FileResourceDTO;
import dto.MeetingDTO;
import dto.MeetingInfoDTO;
import dto.MeetingLocationDTO;
import dto.MeetingParticipantDTO;
import util.TimeUtil;

public class MeetingService {
	//회비 조회용
	public int getMeetingCost(long meetingId) {
	    Integer cost = new MeetingDAO().getMeetingCostByMeetingId(meetingId);

	    if (cost == null) {
	        throw new IllegalArgumentException("모임 회비 정보를 찾을 수 없습니다.");
	    }

	    return cost;
	}

    // 특정 모임 상세 조회
    public MeetingInfoDTO getMeetingInfo(long meetingId) {
        System.out.println("Service: getMeeting");

        MeetingInfoDTO infoDto = new MeetingInfoDTO();

        // 모임정보 가져오기
        MeetingDTO meetingDto = new MeetingDAO().getPostDetail(meetingId);
        if (meetingDto == null) throw new IllegalArgumentException("MeetingDTO가 존재하지않습니다.");

        // 주소 가져오기
        MeetingLocationDTO locationDto = new MeetingLocationDAO().getLocation(meetingDto.getLocationId());
        if (locationDto == null) throw new IllegalArgumentException("MeetingLocationDTO가 존재하지않습니다.");

        // 모임정보 세팅
        infoDto.setMeetingId(meetingDto.getMeetingId());
        infoDto.setTitle(meetingDto.getTitle());
        infoDto.setContent(meetingDto.getContent());
        infoDto.setDate(meetingDto.getDate());
        infoDto.setLocationId(meetingDto.getLocationId());
        infoDto.setMaxMembers(meetingDto.getMaxMembers());
        infoDto.setCurrentMembers(meetingDto.getCurrentMembers());
        infoDto.setCost(meetingDto.getCost());
        infoDto.setTag(meetingDto.getTag());
        infoDto.setStatus(meetingDto.getStatus());
        infoDto.setCreatedAt(meetingDto.getCreatedAt());
        infoDto.setUpdatedAt(meetingDto.getUpdatedAt());
        infoDto.setWeather(meetingDto.getWeather());
        infoDto.setCreatorId(meetingDto.getCreatorId()); // 게시자 ID 추가

        // 장소 정보 세팅
        infoDto.setRoadAddress(locationDto.getRoadAddress());
        infoDto.setJibunAddress(locationDto.getJibunAddress());
        infoDto.setAddrDetail(locationDto.getAddrDetail());
        System.out.println(locationDto.getAddrDetail());
        infoDto.setLatitude(locationDto.getLatitude());
        infoDto.setLongitude(locationDto.getLongitude());

        // 이미지 정보 가져오기
        ImageService imageService = new ImageService(); 
        List<FileResourceDTO> imageUrls = imageService.getMeetingImage(meetingId, "MEETING");
        infoDto.setImages(imageUrls);

        return infoDto;
    }
    
    //특정 모임의 모든 참가자 조회
    public ArrayList<MeetingParticipantDTO> getParticipantsInfo(long meetingId){
    	System.out.println("Service: getParticipantsInfo");
    	return new MeetingParticipantDAO().getParticipantsByMeetId(meetingId);
    }

    // 게시판 리스트 조회
    public ArrayList<MeetingInfoDTO> getMeetingList(long loginUserId) {
        System.out.println("Service: getMeetingList");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ArrayList<MeetingInfoDTO> aList =  new MeetingDAO().getPostList();
        
        //상태 보정 (자동 마감)
        int closedCount = new MeetingDAO().closeExpiredMeetings();
        System.out.println("자동 마감 처리된 모임 수: " + closedCount);
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (MeetingInfoDTO dto : aList) {
        	//timeAgo 세팅 몇분전
            if (dto.getCreatedAt() != null) {
                String timeAgo = TimeUtil.toTimeAgo(dto.getCreatedAt());
                dto.setTimeAgo(timeAgo);
            }
            //데이트 추출 후 문자열로 포멧팅
            if (dto.getDate() != null) {
                dto.setDateStr(sdf.format(dto.getDate()));
            }
            dto.setDong(extractAreaUnit(dto.getJibunAddress()));
        
        // 작성자인지
        boolean isCreator = (loginUserId != -1 && loginUserId == dto.getCreatorId());
        dto.setCreator(isCreator);

        // 참여자인지
        boolean isParticipant = false;
        if (loginUserId != -1) {
            isParticipant = new MeetingParticipantDAO()
                    .isParticipant(dto.getMeetingId(), loginUserId);
        }
        dto.setParticipant(isParticipant);
    }
        return aList;
        
    }
    // 🔍 필터가 적용된 게시판 리스트 조회 (카테고리 + 기간 + 키워드 + 상태 + 날씨)
    public ArrayList<MeetingInfoDTO> getMeetingListFiltered(String category,
                                                            String dateFrom,
                                                            String dateTo,
                                                            String keyword,
                                                            String status,
                                                            String weather) {
        System.out.println("Service: getMeetingListFiltered");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        ArrayList<MeetingInfoDTO> aList =
                new MeetingDAO().getPostListFiltered(category, dateFrom, dateTo, keyword, status, weather);

        for (MeetingInfoDTO dto : aList) {
            if (dto.getCreatedAt() != null) {
                String timeAgo = TimeUtil.toTimeAgo(dto.getCreatedAt());
                dto.setTimeAgo(timeAgo);
            }
            if (dto.getDate() != null) {
                dto.setDateStr(sdf.format(dto.getDate()));
            }
            dto.setDong(extractAreaUnit(dto.getJibunAddress()));
        }
        return aList;
    }




    // 모임 장소 업데이트
    public boolean updateLocation(
            long locationId,
            String roadAddress,
            String jibunAddress,
            String addrDetail,
            double latitude,
            double longitude
    ) throws Exception {
        if (locationId <= 0) throw new IllegalArgumentException("주소정보가 없습니다.");

        MeetingLocationDTO dto = new MeetingLocationDTO();
        dto.setId(locationId);
        dto.setRoadAddress(roadAddress);
        dto.setJibunAddress(jibunAddress);
        dto.setAddrDetail(addrDetail);
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);

        boolean result = new MeetingLocationDAO().updateLocation(dto);
        if (!result) throw new Exception("모임 장소 업데이트 실패");
        return result;
    }

    // 모임 장소 저장
    public long insertLocation(
            String roadAddress,
            String jibunAddress,
            String addrDetail,
            double latitude,
            double longitude
    ) throws Exception {
        System.out.println("Service: insertLocation");

        MeetingLocationDTO dto = new MeetingLocationDTO();
        dto.setRoadAddress(roadAddress);
        dto.setJibunAddress(jibunAddress);
        dto.setAddrDetail(addrDetail);
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);

        Long rs = new MeetingLocationDAO().insertLocation(dto);
        if (rs != null) return rs;

        throw new Exception("모임장소 아이디값 없음");
    }

    // 모임 생성 (게시자 ID 포함)
    public long insertMeetingInfo(
            String title,
            String content,
            Timestamp date,
            long locationId,
            int maxMembers,
            int currentMembers,
            int cost,
            String tag,
            String status,
            double latitude,
            double longitude,
            long creatorId // 로그인 세션에서 받은 게시자 ID
    ) throws Exception {
        System.out.println("Service: insertMeetingInfo");
        if (date == null) throw new IllegalArgumentException("모임 날짜가 존재하지 않습니다.");

        LocalDate meetingDate = date.toLocalDateTime().toLocalDate();
        LocalDate today = LocalDate.now();
        int dayIndex = (int) ChronoUnit.DAYS.between(today, meetingDate);
        if (dayIndex < 0 || dayIndex > 7) throw new RuntimeException("날씨 조회는 7일 이내 날짜만 가능합니다");

        MeetingDTO dto = new MeetingDTO();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setDate(date);
        dto.setLocationId(locationId);
        dto.setMaxMembers(maxMembers);
        dto.setCurrentMembers(currentMembers);
        dto.setCost(cost);
        dto.setTag(processTags(tag));
        dto.setStatus(status);
        dto.setCreatorId(creatorId); // 게시자 ID 세팅
        
        String jsonStr = callAnAPI(latitude, longitude);
        if (jsonStr == null) dto.setWeather("정보없음");
        else dto.setWeather(extractWeather(jsonStr, dayIndex));
        
        return new MeetingDAO().insert(dto);
    }
    public boolean increaseViewCount(long meetId) {
    	return new MeetingDAO().increaseViewCount(meetId);

    }
    // 모임 업데이트
    public boolean updateMeetingInfo(
            long meetingId,
            String title,
            String content,
            Timestamp date,
            long locationId,
            int maxMembers,
            int currentMembers,
            int cost,
            String tag,
            String status,
            double latitude,
            double longitude,
            long creatorId // 로그인 세션에서 받은 게시자 ID
    ) throws Exception {
        if (date == null) throw new IllegalArgumentException("모임 날짜가 존재하지 않습니다.");

        LocalDate meetingDate = date.toLocalDateTime().toLocalDate();
        LocalDate today = LocalDate.now();
        int dayIndex = (int) ChronoUnit.DAYS.between(today, meetingDate);
        if (dayIndex < 0 || dayIndex > 7) throw new RuntimeException("날씨 조회는 7일 이내 날짜만 가능합니다");

        MeetingDTO dto = new MeetingDTO();
        dto.setMeetingId(meetingId);
        dto.setTitle(title);
        dto.setContent(content);
        dto.setDate(date);
        dto.setLocationId(locationId);
        dto.setMaxMembers(maxMembers);
        dto.setCurrentMembers(currentMembers);
        dto.setCost(cost);
        dto.setTag(processTags(tag));
        dto.setStatus(status);
        dto.setCreatorId(creatorId); // 게시자 ID 세팅

        String jsonStr = callAnAPI(latitude, longitude);
        if (jsonStr == null) dto.setWeather("정보없음");
        else dto.setWeather(extractWeather(jsonStr, dayIndex));

        boolean result = new MeetingDAO().updateMeet(dto);
        if (!result) throw new Exception("모임 정보 업데이트 실패");
        return result;
    }
    //참가자인지 확인
    public boolean isParticipant(long meetId, long userId) {
    	boolean result = new MeetingParticipantDAO().isParticipant(meetId, userId);
    	return result;
    }
    //모임 참여 
    public boolean joinMeet(long meetId, long userId) throws Exception {
    	System.out.println("Service: joinMeet");
    	MeetingParticipantDTO participateDto = new MeetingParticipantDTO();
    	participateDto.setMeetingId(meetId);
    	participateDto.setUserId(userId);
    			
    	
    	boolean result = new MeetingParticipantDAO().insertParticipant(participateDto);
    	if(!result) {
    		throw new Exception("참여 실패");
    	}
    	result = new MeetingDAO().increaseCurrentMembers(meetId);
    	if(!result) {
    		throw new Exception("인원수 증가 실패");
    	}
    	return result;
    }
    //모임 나오기
    public boolean quitMeet(long meetId, long userId) throws Exception {
    	System.out.println("Service: quitMeet");
    	MeetingParticipantDTO participateDto = new MeetingParticipantDTO();
    	participateDto.setMeetingId(meetId);
    	participateDto.setUserId(userId);
    			
    	
    	boolean result = new MeetingParticipantDAO().deleteParticipant(participateDto);
    	if(!result) {
    		throw new Exception("모임에서 나오기 실패");
    	}
    	result = new MeetingDAO().decreaseCurrentMembers(meetId);
    	if(!result) {
    		throw new Exception("인원수 감소 실패");
    	}
    	return result;
    }

    // 오픈웨더 API 호출
    public String callAnAPI(double lat, double lon) {
        String key = "fcdf715f2e4faa898d33ff124104cafe";
        BufferedReader br = null;
        HttpURLConnection conn = null;

        String apiUrl = "https://api.openweathermap.org/data/3.0/onecall?lat=" + lat + "&lon=" + lon + "&appid=" + key + "&units=metric&lang=kr";

        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int status = conn.getResponseCode();
            if (status != 200) {
                System.out.println("요청실패: " + status);
                throw new RuntimeException("API 요청 실패: " + status);
            }

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb);
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if (br != null) try { br.close(); } catch (Exception ignored) {}
            if (conn != null) conn.disconnect();
        }
    }
    //지불여부 변경 용 서비스
    public void markAsPaid(long meetingId, long userId) {
    	System.out.println("Service: markAsPaid");
    	boolean result = new MeetingParticipantDAO().markAsPaid(meetingId, userId);;
        if (! result) {
            throw new RuntimeException("회비 납부 처리 실패");
        }
    }
    //지불 여부 체크
    public boolean hasUserPaid(long meetingId, long userId) {
    	System.out.println("Service: hasUserPaid");
        return new MeetingParticipantDAO().hasUserPaid(meetingId, userId);
    }



    // 날씨 정보 추출
    public String extractWeather(String jsonStr, int dayIndex) {
        JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
        JsonArray dailyArr = json.getAsJsonArray("daily");

        if (dailyArr == null || dailyArr.size() <= dayIndex) {
            System.out.println("배열 데이터 오류 or 날짜 인덱스 인자 오류");
            return null;
        }

        JsonObject dayObj = dailyArr.get(dayIndex).getAsJsonObject();
        JsonArray weatherArr = dayObj.getAsJsonArray("weather");
        if (weatherArr == null || weatherArr.size() == 0) {
            System.out.println("weather 데이터 없음");
            return null;
        }

        JsonObject weatherObj = weatherArr.get(0).getAsJsonObject();
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
            default: return "기타";
        }
    }

    // 게시글 삭제
    public boolean deleteMeeting(long meetingId, long creatorId) {

        MeetingDAO meetingDao = new MeetingDAO();
        ImageService imageService = new ImageService();

        //업로드 실제 경로
        String uploadPath = "C:/upload/meeting";

        // 모임 이미지 (DB + 실제 파일) 전체 삭제
        imageService.deleteAllByUsed("MEETING", meetingId, uploadPath);

        // location 삭제
        Long locationId = meetingDao.getLocationIdByMeetingId(meetingId);
        if (locationId != null) {
            new MeetingLocationDAO().deleteLocation(locationId);
        }

        //meeting 삭제 (participant, chat_room 등은 CASCADE)
        return meetingDao.delete(meetingId, creatorId);
    }
    
    public void updateMeetingStatus(long meetingId, String status) {
        if (!"OPEN".equals(status) && !"CLOSED".equals(status)) {
            throw new IllegalArgumentException("잘못된 상태값");
        }

        boolean result = new MeetingDAO().updateStatus(meetingId, status);
        if (!result) {
            throw new RuntimeException("모임 상태 변경 실패");
        }
    }
    private String processTags(String rawTag) {
        if (rawTag == null || rawTag.isBlank()) return null;

        String[] arr = rawTag.split(",");
        LinkedHashSet<String> set = new LinkedHashSet<>();

        for (String t : arr) {
            String tag = t.trim();
            if (!tag.isEmpty()) {
                set.add(tag);
            }
            if (set.size() >= 5) break; // 최대 5개 제한
        }

        return set.isEmpty() ? null : String.join(",", set);
    }
    public void reopenMeeting(long meetingId, Long loginUserId) {
        MeetingDTO meeting = new MeetingDAO().getPostDetail(meetingId);

        if (meeting == null) {
            throw new IllegalArgumentException("존재하지 않는 모임입니다.");
        }

        // 작성자 검증
        if (!loginUserId.equals(meeting.getCreatorId())) {
            throw new SecurityException("권한이 없습니다.");
        }

        // 🔥 날짜 검증
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (meeting.getDate().before(now)) {
            throw new IllegalStateException("이미 지난 모임은 모집을 재개할 수 없습니다.");
        }

        new MeetingDAO().updateStatus(meetingId, "OPEN");
    }
    private String extractAreaUnit(String jibunAddress) {
        if (jibunAddress == null || jibunAddress.isBlank()) {
            return "구로동"; // 기본값
        }

        String[] parts = jibunAddress.split(" ");
        for (int i = parts.length - 1; i >= 0; i--) {
            String p = parts[i];
            if (
                p.endsWith("동") ||
                p.endsWith("읍") ||
                p.endsWith("면") ||
                p.endsWith("리")
            ) {
                return p;
            }
        }
        return "구로동";
    }
    //홈 지도용 - 활성 모임 조회 (최소 정보)
    public List<MeetingInfoDTO> getActiveMeetingsForMap() {
        return new MeetingDAO().findActiveMeetingsForMap();
    }
 
}

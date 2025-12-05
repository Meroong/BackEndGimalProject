package dto;

import java.sql.Timestamp;
import java.util.List;

public class MeetingInfoDTO {

    // === Meeting Location ===
    private Long locationId;         // 모임 장소 ID
    private String roadAddress;      // 도로명 주소
    private String jibunAddress;     // 지번 주소
    private String addrDetail;       // 상세 주소
    private Double latitude;         // 위도
    private Double longitude;        // 경도

    // === Meeting ===
    private Long meetingId;          // 모임 ID
    private String title;            // 제목
    private String content;          // 설명
    private Timestamp date;          // 모임 날짜
    private int maxMembers;          // 최대 인원
    private int currentMembers;      // 현재 인원
    private int cost;                // 참가비
    private String tag;              // 태그
    private String status;           // OPEN / CLOSED / COMPLETED
    private long creatorId;        // 게시자 ID (새로 추가)
    
    public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	private Timestamp createdAt;     // 생성일
    private Timestamp updatedAt;     // 수정일

    // === Weather ===
    private String weather;          // 날씨 (맑음/흐림/비/눈 등)

    // === Image URLs (여러 장 가능) ===
    private List<String> images;   // 이미지 리스트

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getRoadAddress() {
		return roadAddress;
	}

	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}

	public String getJibunAddress() {
		return jibunAddress;
	}

	public void setJibunAddress(String jibunAddress) {
		this.jibunAddress = jibunAddress;
	}

	public String getAddrDetail() {
		return addrDetail;
	}

	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getMaxMembers() {
		return maxMembers;
	}

	public void setMaxMembers(int maxMembers) {
		this.maxMembers = maxMembers;
	}

	public int getCurrentMembers() {
		return currentMembers;
	}

	public void setCurrentMembers(int currentMembers) {
		this.currentMembers = currentMembers;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public java.util.List<String> getImages() {
		return images;
	}

	public void setImages(java.util.List<String> images) {
		this.images = images;
	}

    
    
}

package dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 관리자용 모임 DTO
 * - AdminMeetingDAO 에서 사용하는 메서드 (setContent, setDate, setMaxMembers ...)
 * - meetingDetail.jsp 에서 사용하는 필드 (title, status, description, meetingDate ...)
 *   를 모두 지원하도록 통합한 클래스
 */
public class AdminMeetingDTO {

    // ====== DB 기본 컬럼 기반 필드 ======
    private long id;              // 모임 번호
    private String title;         // 제목
    private String content;       // 내용(소개)
    private Timestamp date;       // 모임 일시(원본)
    private int maxMembers;       // 최대 인원
    private int currentMembers;   // 현재 인원
    private int cost;             // 비용
    private String tag;           // 태그
    private Timestamp createdAt;  // 생성 일시
    private String status;        // 상태: OPEN / CLOSED / COMPLETED

    // ====== 화면 편의를 위한 추가 필드 ======
    private String hostNickname;
    private String hostUserId;
    private String category;
    private String location;

    // 문자열 형태의 날짜/시간 (JSP에서 출력용)
    private String meetingDate;
    private String meetingTime;

    // JSP에서 쓰는 이름 (참가 인원)
    private int currentParticipants;
    private int maxParticipants;

    // 별도 설명 필드 (없으면 content와 동일하게 사용)
    private String description;

    public AdminMeetingDTO() { }

    // ====== 기본 getter / setter ======

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    // ---- content <-> description 매핑 ----
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
        // description이 따로 안 세팅된 경우 content를 그대로 사용
        if (this.description == null) {
            this.description = content;
        }
    }

    public String getDescription() {
        return (description != null) ? description : content;
    }
    public void setDescription(String description) {
        this.description = description;
        if (this.content == null) {
            this.content = description;
        }
    }

    // ---- 날짜/시간 매핑 ----
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;

        if (date != null) {
            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");

            this.meetingDate = dfDate.format(date);
            this.meetingTime = dfTime.format(date);
        }
    }

    public String getMeetingDate() {
        return meetingDate;
    }
    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }
    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    // ---- 인원 매핑 ----
    public int getMaxMembers() {
        return maxMembers;
    }
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
        this.maxParticipants = maxMembers;
    }

    public int getCurrentMembers() {
        return currentMembers;
    }
    public void setCurrentMembers(int currentMembers) {
        this.currentMembers = currentMembers;
        this.currentParticipants = currentMembers;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
        this.maxMembers = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }
    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
        this.currentMembers = currentParticipants;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // ====== 추가 필드 (모임장, 카테고리, 장소 등) ======

    public String getHostNickname() {
        return hostNickname;
    }
    public void setHostNickname(String hostNickname) {
        this.hostNickname = hostNickname;
    }

    public String getHostUserId() {
        return hostUserId;
    }
    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}

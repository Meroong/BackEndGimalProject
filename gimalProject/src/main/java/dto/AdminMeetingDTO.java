package dto;

import java.sql.Timestamp;

public class AdminMeetingDTO {

    private long id;               // 모임 ID (PK, meeting.id)
    private String title;          // 모임 제목
    private String content;        // 모임 설명
    private Timestamp date;        // 모임 날짜/시간
    private String location;       // 장소
    private int maxMembers;        // 최대 인원
    private int currentMembers;    // 현재 인원
    private int cost;              // 참가비
    private String tag;            // 태그
    private String status;         // 상태 (OPEN / CLOSED / COMPLETED)
    private Timestamp createdAt;   // 생성일시

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
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
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
}

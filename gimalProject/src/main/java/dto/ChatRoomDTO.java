package dto;

public class ChatRoomDTO { 
    private long roomId;            // 채팅방 고유번호
    private Long itemId;     		// 거래 게시판 전용(거래 채팅일 경우)
    private String roomType;      	// 채팅방 유형 (PRIVATE / GROUP)
    private Long hostId;      		// 방장
    private Long meetingId;  		// 모임 게시판 전용
    private String createdAt;     	// 생성일시
    
    
	public long getRoomId() {
		return roomId;
	}
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}
	public Long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
    
    
	



}

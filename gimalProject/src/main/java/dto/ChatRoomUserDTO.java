package dto;

import java.sql.Timestamp;

public class ChatRoomUserDTO {
    private Long roomId;
    private Long userId;
    private Timestamp joinedAt;
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Timestamp getJoinedAt() {
		return joinedAt;
	}
	public void setJoinedAt(Timestamp joinedAt) {
		this.joinedAt = joinedAt;
	}
    
    
}


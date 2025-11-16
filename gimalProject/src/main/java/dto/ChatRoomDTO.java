package dto;

public class ChatRoomDTO {
	private int userId;			//유저 고유아이디 db수정 필요!!!!!!!!!!!!
    private long roomId;              // 채팅방 고유번호
    private long itemId;          // 상품 ID (거래 채팅일 경우)
    private String roomType;      // 채팅방 유형 (PRIVATE / GROUP)
    private String createdAt;     // 생성일시
    
    
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public long getRoomId() {
		return roomId;
	}
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}


}

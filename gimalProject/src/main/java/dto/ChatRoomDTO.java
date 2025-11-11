package dto;

public class ChatRoomDTO {
    private long id;              // 채팅방 식별자
    private long itemId;          // 상품 ID (거래 채팅일 경우)
    private String roomType;      // 채팅방 유형 (PRIVATE / GROUP)
    private String createdAt;     // 생성일시

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getItemId() { return itemId; }
    public void setItemId(long itemId) { this.itemId = itemId; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

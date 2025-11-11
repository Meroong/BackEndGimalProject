package dto;

public class ChatMessageDTO {
    private long id;            // 메시지 식별자
    private long roomId;        // 채팅방 ID
    private long senderId;      // 보낸 사람 ID
    private String content;     // 메시지 내용
    private String sentAt;      // 전송 시각

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getRoomId() { return roomId; }
    public void setRoomId(long roomId) { this.roomId = roomId; }

    public long getSenderId() { return senderId; }
    public void setSenderId(long senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSentAt() { return sentAt; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }
}

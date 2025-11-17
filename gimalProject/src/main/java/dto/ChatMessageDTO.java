package dto;

import java.sql.Timestamp;

public class ChatMessageDTO {
    private Long messageId;
    private Long roomId;
    private Long senderId;
    private String content;
    private Timestamp sentAt;
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getSentAt() {
		return sentAt;
	}
	public void setSentAt(Timestamp sentAt) {
		this.sentAt = sentAt;
	}



}

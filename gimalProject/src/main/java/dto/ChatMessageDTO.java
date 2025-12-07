package dto;

import java.sql.Timestamp;

public class ChatMessageDTO {
    //기본 테이블 구성 필드
	private Long messageId;
    private Long roomId;
    private Long senderId;
    private String content;
    private Timestamp sentAt;
    
    //백쪽 추가 필드 
    private String senderNickname;
    private String senderProfile;
    
	public String getSenderProfile() {
		return senderProfile;
	}
	public void setSenderProfile(String senderProfile) {
		this.senderProfile = senderProfile;
	}
	public String getSenderNickname() {
		return senderNickname;
	}
	public void setSenderNickname(String senderNickname) {
		this.senderNickname = senderNickname;
	}
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

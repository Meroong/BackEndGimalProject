package dto;

import java.sql.Timestamp;

public class ChatMessageDTO {
    //기본 테이블 구성 필드
	private Long messageId;
    private Long roomId;
    private Long senderId;
    
	// 메시지 타입: TEXT / IMAGE / FILE
    private String messageType;

    // TEXT일 때만 사용 (IMAGE면 null)
    private String content;

    private Timestamp sentAt;
    
    //백쪽 추가 필드 
    private String senderNickname;
    private String senderProfile;
    

    // 이미지 메시지일 경우 사용
    private String imageUrl;         // file_resource.file_url
    private String imageName;        // 파일명 (선택)
    private Long imageSize;          // 파일 사이즈 (선택)
    
    
    public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public Long getImageSize() {
		return imageSize;
	}
	public void setImageSize(Long imageSize) {
		this.imageSize = imageSize;
	}
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

package dto;

import java.security.Timestamp;

public class chatParticipantsUserDTO {
    // 미팅 참여자 정보 필드 
    private long participantId;
    private long meetingId;
    private boolean paid;

    // 유저 필드
    private String nickname;
    private String userId;
    private String profileImage;
    
    //채팅방 참여여부 
    private boolean inChat;
    
	public boolean isInChat() {
		return inChat;
	}
	public void setInChat(boolean inChat) {
		this.inChat = inChat;
	}
	public long getParticipantId() {
		return participantId;
	}
	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}
	public long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(long meetingId) {
		this.meetingId = meetingId;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
}
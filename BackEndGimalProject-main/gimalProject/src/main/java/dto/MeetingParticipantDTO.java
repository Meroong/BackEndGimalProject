package dto;

import java.sql.Timestamp;

public class MeetingParticipantDTO {

    private long id;              // 참가 ID (PK)
    private long meetingId;       // 모임 ID
    private long userId;          // 유저 ID
    private boolean paid;         // 참가비 지불 여부
    private Timestamp joinedAt;   // 참여 일시 (TIMESTAMP)

    public long getId() { 
        return id; 
    }
    public void setId(long id) { 
        this.id = id; 
    }

    public long getMeetingId() { 
        return meetingId; 
    }
    public void setMeetingId(long meetingId) { 
        this.meetingId = meetingId; 
    }

    public long getUserId() { 
        return userId; 
    }
    public void setUserId(long userId) { 
        this.userId = userId; 
    }

    public boolean isPaid() { 
        return paid; 
    }
    public void setPaid(boolean paid) { 
        this.paid = paid; 
    }

    public Timestamp getJoinedAt() { 
        return joinedAt; 
    }
    public void setJoinedAt(Timestamp joinedAt) { 
        this.joinedAt = joinedAt; 
    }
}

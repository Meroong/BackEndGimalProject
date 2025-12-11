package dto;

import java.sql.Timestamp;
import java.util.List;

public class PollDTO {
    private long id;
    private long roomId;
    private String title;
    private Timestamp expireAt;
    private Timestamp createdAt;
    private List<PollOptionDTO> options;
    private boolean closed;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getRoomId() { return roomId; }
    public void setRoomId(long roomId) { this.roomId = roomId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Timestamp getExpireAt() { return expireAt; }
    public void setExpireAt(Timestamp expireAt) { this.expireAt = expireAt; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<PollOptionDTO> getOptions() { return options; }
    public void setOptions(List<PollOptionDTO> options) { this.options = options; }

    public boolean isClosed() { return closed; }
    public void setClosed(boolean closed) { this.closed = closed; }
}

package dto;

import java.sql.Timestamp;

public class PollVoteDTO {
    private long pollId;
    private long userId;
    private long optionId;
    private Timestamp votedAt;

    public long getPollId() { return pollId; }
    public void setPollId(long pollId) { this.pollId = pollId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getOptionId() { return optionId; }
    public void setOptionId(long optionId) { this.optionId = optionId; }

    public Timestamp getVotedAt() { return votedAt; }
    public void setVotedAt(Timestamp votedAt) { this.votedAt = votedAt; }
}

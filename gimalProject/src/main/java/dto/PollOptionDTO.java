package dto;

public class PollOptionDTO {
    private long id;
    private long pollId;
    private String optionText;
    private int voteCount;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getPollId() { return pollId; }
    public void setPollId(long pollId) { this.pollId = pollId; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
}

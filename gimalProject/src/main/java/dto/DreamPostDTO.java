package dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class DreamPostDTO {

    private Long dreamId;
    private Long writerId;
    private String writerType;      // WORKER / EMPLOYER / ADMIN
    private String title;
    private String content;
    private String categoryCode;    // TOY, STROLLER ...
    private String conditionCode;   // NEW, LIKE_NEW, USED
    private int price;
    private String dong;
    private List<String> imagesUrl;
    private String status;          // OPEN, CLOSED ...
    private int viewCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String timeAgoLabel;

    public Long getDreamId() {
        return dreamId;
    }

    public void setDreamId(Long dreamId) {
        this.dreamId = dreamId;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }

    public String getWriterType() {
        return writerType;
    }

    public void setWriterType(String writerType) {
        this.writerType = writerType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp timestamp) {
        this.createdAt = timestamp;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setTimeAgoLabel(String timeAgoLabel) {
    	this.timeAgoLabel = timeAgoLabel;
    }
    
    public String getTimeAgoLabel() {
    	return timeAgoLabel;
    }
}

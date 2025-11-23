package dto;

import java.sql.Timestamp;

public class ReportDTO {

    private long id;              // 신고 ID
    private long reporterId;      // 신고자 ID
    private long targetUserId;    // 대상자 ID
    private String targetType;    // USER / ITEM / MEETING
    private String reason;        // 신고 사유
    private String status;        // PENDING / RESOLVED
    private Timestamp createdAt;  // 등록일시

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReporterId() {
        return reporterId;
    }

    public void setReporterId(long reporterId) {
        this.reporterId = reporterId;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

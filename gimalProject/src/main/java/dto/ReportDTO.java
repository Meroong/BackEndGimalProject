package dto;

import java.sql.Timestamp;

public class ReportDTO {
	private int reportId;          // 신고 ID (PK)
    private int reporterId;        // 신고자 ID (FK - user_id)
    private int targetUserId;      // 신고 대상자 ID (FK - user_id)
    private String targetType;     // 신고 유형 (게시글 / 댓글 / 사용자 등)
    private String reason;         // 신고 사유
    private Timestamp createdAt;   // 신고 일자
    private String status;         // 처리 상태 (대기 / 처리완료 등)
    
    
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
	public int getReporterId() {
		return reporterId;
	}
	public void setReporterId(int reporterId) {
		this.reporterId = reporterId;
	}
	public int getTargetUserId() {
		return targetUserId;
	}
	public void setTargetUserId(int targetUserId) {
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
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
}

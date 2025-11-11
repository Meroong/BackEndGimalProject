package dto;

import java.sql.Timestamp;

public class ReviewDTO {
	private int reviewId;          // 리뷰 ID (PK)
    private int reviewerId;        // 작성자 ID
    private int revieweeId;        // 대상자 ID
    private int itemId;            // 아이템 ID (FK)
    private int ratingManner;      // 매너 점수 (1~5)
    private String content;        // 리뷰 내용
    private Timestamp createdAt;   // 작성일
    
    
	public int getReviewId() {
		return reviewId;
	}
	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}
	public int getReviewerId() {
		return reviewerId;
	}
	public void setReviewerId(int reviewerId) {
		this.reviewerId = reviewerId;
	}
	public int getRevieweeId() {
		return revieweeId;
	}
	public void setRevieweeId(int revieweeId) {
		this.revieweeId = revieweeId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getRatingManner() {
		return ratingManner;
	}
	public void setRatingManner(int ratingManner) {
		this.ratingManner = ratingManner;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
    
    
    
}

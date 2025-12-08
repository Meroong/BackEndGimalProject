package dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDTO {
    private int item_id;		// 상품 ID
    private int seller_id;		// 드림자 ID
    private int category_id;	// 카테고리 ID
    private String title;		// 상품 제목
    private String content;		// 상품 설명
    private int price;			// 드림 가격
    private String trade_type;  // SALE, RENTAL, DREAM 거래 유형
    private String status;		// 상품 상태
    private Timestamp created_at;// 등록 일시
    private Timestamp updated_at;// 수정 일시
    
    // 조인해서 가져올 추가 정보 (화면 표시용)
    private String seller_nickname;
    private String dong_name; 
    private String thumbnail; // 대표 이미지
    private String time_ago; // "방금 전" 등

    public ItemDTO() {}

    // Getters & Setters
  
    public int getItem_id() { return item_id; }
	public void setItem_id(int item_Id) { this.item_id = item_Id; }
	
	public int getSeller_id() { return seller_id; }
	public void setSeller_id(int seller_Id) { this.seller_id = seller_Id; }
	
	public int getCategory_id() { return category_id; }
	public void setCategory_id(int category_Id) { this.category_id = category_Id; }
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	
	public int getPrice() { return price; }
	public void setPrice(int price) { this.price = price; }
	
	public String getTrade_type() { return trade_type; }
	public void setTrade_type(String trade_Type) { this.trade_type = trade_Type; }
	
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	
	public Timestamp getCreated_at() { return created_at; }
	public void setCreated_at(Timestamp created_at) { 
		this.created_at = created_at;
		calculateTimeAgo(); // 시간 계산 자동 실행
	}
	
	public Timestamp getUpdated_at() { return updated_at; }
	public void setUpdated_at(Timestamp updated_at) { this.updated_at = updated_at; }

	// 추가 필드
	public String getSeller_nickname() { return seller_nickname; }
	public void setSeller_nickname(String sellerNickname) { this.seller_nickname = sellerNickname; }
    public String getDong_name() { return dong_name; }
    public void setDong_name(String dongName) { this.dong_name = dongName; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public String getTime_ago() { return time_ago; }

    // 시간 계산 로직
    private void calculateTimeAgo() {
        if (this.created_at == null) return;
        long diff = new Date().getTime() - this.created_at.getTime();
        int sec = (int)(diff / 1000);
        
        if (sec < 60) this.time_ago = "방금 전";
        else if (sec < 3600) this.time_ago = (sec / 60) + "분 전";
        else if (sec < 86400) this.time_ago = (sec / 3600) + "시간 전";
        else if (sec < 604800) this.time_ago = (sec / 86400) + "일 전";
        else this.time_ago = new SimpleDateFormat("yyyy-MM-dd").format(this.created_at);
    }
}
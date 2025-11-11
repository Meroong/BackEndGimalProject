package dto;

import java.sql.Timestamp;

public class ItemDTO {
	 	private int itemId;            // 상품 ID (PK)
	    private int sellerId;          // 판매자 ID (FK - user_id)
	    private int categoryId;        // 카테고리 ID
	    private String title;          // 상품 제목
	    private String content;        // 상품 설명
	    private int price;             // 상품 가격
	    private boolean rentalInfo;    // 대여 가능 여부
	    private String status;         // 판매 상태 (판매중 / 예약중 / 판매완료)
	    private Timestamp createdAt;   // 등록일
	    private Timestamp updatedAt;   // 수정일

}

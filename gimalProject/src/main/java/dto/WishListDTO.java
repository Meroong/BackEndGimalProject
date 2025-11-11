package dto;

public class WishListDTO {
    private long id;          // 찜목록 식별자
    private long userId;      // 유저 ID
    private long itemId;      // 상품 ID
    private String createdAt; // 등록일시

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getItemId() { return itemId; }
    public void setItemId(long itemId) { this.itemId = itemId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

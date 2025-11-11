package dto;

public class TransactionDTO {
    private long id;               // 거래 식별자
    private long itemId;           // 거래 상품 ID
    private long buyerId;          // 구매자 ID
    private long sellerId;         // 판매자 ID
    private String status;         // 거래 상태
    private String createdAt;      // 거래 시작일
    private String completedAt;    // 거래 완료일

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getItemId() { return itemId; }
    public void setItemId(long itemId) { this.itemId = itemId; }

    public long getBuyerId() { return buyerId; }
    public void setBuyerId(long buyerId) { this.buyerId = buyerId; }

    public long getSellerId() { return sellerId; }
    public void setSellerId(long sellerId) { this.sellerId = sellerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getCompletedAt() { return completedAt; }
    public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }
}

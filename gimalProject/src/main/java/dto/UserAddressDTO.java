package dto;

import java.sql.Timestamp;

public class UserAddressDTO {
    private Long userId;          // 유저 ID (PK)
    private String roadAddress;   // 도로명 주소
    private String jibunAddress;  // 지번 주소
    private String addrDetail;    // 상세 주소
    private Double latitude;      // 위도
    private Double longitude;     // 경도
    private Timestamp createdAt;  // 등록일시
    private Timestamp updatedAt;  // 수정일시

    public UserAddressDTO() {}


    // ---------------------------
    // Getter / Setter
    // ---------------------------
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRoadAddress() { return roadAddress; }
    public void setRoadAddress(String roadAddress) { this.roadAddress = roadAddress; }

    public String getJibunAddress() { return jibunAddress; }
    public void setJibunAddress(String jibunAddress) { this.jibunAddress = jibunAddress; }

    public String getAddrDetail() { return addrDetail; }
    public void setAddrDetail(String addrDetail) { this.addrDetail = addrDetail; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "UserAddressDTO{" +
                "userId=" + userId +
                ", roadAddress='" + roadAddress + '\'' +
                ", jibunAddress='" + jibunAddress + '\'' +
                ", addrDetail='" + addrDetail + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
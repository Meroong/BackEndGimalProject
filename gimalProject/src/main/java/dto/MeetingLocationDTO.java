package dto;

public class MeetingLocationDTO {

    private Long id;               // 모임 장소 ID
    private String roadAddress;    // 도로명 주소
    private String jibunAddress;   // 지번 주소
    private String addrDetail;     // 상세 주소
    private String dongName;
    private Double latitude;       // 위도
    private Double longitude;      // 경도

    public MeetingLocationDTO() {}

    public MeetingLocationDTO(Long id, String roadAddress, String jibunAddress,
                              String addrDetail, Double latitude, Double longitude) {
        this.id = id;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.addrDetail = addrDetail;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDongName() {
		return dongName;
	}

	public void setDongName(String dongName) {
		this.dongName = dongName;
	}

	// Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getJibunAddress() {
        return jibunAddress;
    }

    public void setJibunAddress(String jibunAddress) {
        this.jibunAddress = jibunAddress;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

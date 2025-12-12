package dto;

import java.sql.Timestamp;

public class WeatherDTO {
    private double temperature; // 현재 온도
    private String weather;     // 날씨 상태 (맑음, 흐림, 비 등)
    private int pm10;           // 미세먼지 농도
    private Timestamp createdAt; // 데이터 생성 시각
    private Double latitude;         // 위도
    private Double longitude;        // 경도

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

	// 기본 생성자
    public WeatherDTO() {}

    // 생성자
    public WeatherDTO(double temperature, String weather, int pm10, Timestamp createdAt) {
        this.temperature = temperature;
        this.weather = weather;
        this.pm10 = pm10;
        this.createdAt = createdAt;
    }

    // Getter & Setter
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "WeatherDTO{" +
                "temperature=" + temperature +
                ", weather='" + weather + '\'' +
                ", pm10=" + pm10 +
                ", createdAt=" + createdAt +
                '}';
    }
}


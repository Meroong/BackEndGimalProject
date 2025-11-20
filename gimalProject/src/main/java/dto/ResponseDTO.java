package dto;

public class ResponseDTO {
    private Boolean status;   // "true" or "false"
    private String message;  // 메시지 내용
    private Object data;     // 실제 반환할 데이터 (채팅방 목록 등)

    public ResponseDTO() {}

    public ResponseDTO(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseDTO(Boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 기존 getter / setter
    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    // 추가: 상태를 쉽게 확인할 수 있는 메서드
    public boolean isSuccess() {
        return status != null && status;
    }
}

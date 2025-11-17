package dto;

public class ResponseDTO {
    private String status;   // "success" or "fail"
    private String message;  // 메시지 내용
    private Object data;     // 실제 반환할 데이터 (채팅방 목록 등)

    public ResponseDTO() {}

    public ResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseDTO(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // getter / setter
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}

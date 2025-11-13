package dto;

public class ResponseDTO {
    private String status;   // "success" or "fail"
    private String message;  // 메시지 내용

    public ResponseDTO() {}
    public ResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // getter / setter
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}


package dto;

public class FileResourceDTO {

    private Long id;
    private String fileUrl;
    private String fileName;
    private String originalName;
    private String fileType;
    private Long size;
    private String usedType;
    private Long usedId;
    private String createdAt;

    public FileResourceDTO() {}

    public FileResourceDTO(Long id, String fileUrl, String fileName, String originalName,
                           String fileType, Long size, String usedType,
                           Long usedId, String createdAt) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.originalName = originalName;
        this.fileType = fileType;
        this.size = size;
        this.usedType = usedType;
        this.usedId = usedId;
        this.createdAt = createdAt;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }

    public String getUsedType() { return usedType; }
    public void setUsedType(String usedType) { this.usedType = usedType; }

    public Long getUsedId() { return usedId; }
    public void setUsedId(Long usedId) { this.usedId = usedId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}


package service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import dao.FileResourceDAO;
import dto.FileResourceDTO;
import jakarta.servlet.http.Part;

public class ImageService {
	
	//프로필 사진 조회 
	public String getProfileImage(long autoId, String usedType) {
		System.out.println("work service: getImage");
		String profileUrl = new FileResourceDAO().getFileUrl(autoId, usedType);
	    if(profileUrl == null || profileUrl.isEmpty()) {
	            profileUrl = "/resources/images/default_profile.png"; // 기본 이미지 추가해야함
	    }
		return profileUrl; 
	}
	//모임 사진 조회
	public List<FileResourceDTO> getMeetingImage(long meetingId, String usedType){
		System.out.println("work service: getImages");
		List<FileResourceDTO> aList = new FileResourceDAO().getFileUrls(meetingId, usedType);
		 
		 return aList;
	}
	public boolean deleteProfile(String usedType, long autoId, String uploadPath) {
		System.out.println("work service: deleteProfile");
		
		String oldFileName = new FileResourceDAO().getFileUrl(autoId, usedType);
		
		//디렉토리 설정
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) uploadDir.mkdirs();
		
        //실제 파일 삭제 (기본 이미지면 삭제하지 않음)
        if(oldFileName != null && !oldFileName.isEmpty()) {
            File file = new File(uploadDir, oldFileName.substring(oldFileName.lastIndexOf('/')+1));
            if(file.exists()) file.delete();
        }
		
		return new FileResourceDAO().deleteFileByUsed(usedType, autoId);
	}
	//이미지 업로드 서비스  usedType으로 구분하도록
	public boolean uploadFile(long usedId, Part filePart, String uploadPath, String usedType) {
	    System.out.println("Service: uploadFile " + usedType);

	    FileResourceDAO fileDao = new FileResourceDAO();

	    String ogName = filePart.getSubmittedFileName();
	    String fileType = filePart.getContentType();
	    long size = filePart.getSize();

	    // 확장자
	    String ext = "";
	    int dotIndex = ogName.lastIndexOf(".");
	    if (dotIndex != -1) ext = ogName.substring(dotIndex);

	    // 저장 파일명
	    String savedFileName = UUID.randomUUID().toString() + ext;

	    // 폴더 생성
	    File dir = new File(uploadPath);
	    if (!dir.exists()) dir.mkdirs();

	    // 저장
	    try {
	        filePart.write(uploadPath + File.separator + savedFileName);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }

	    // DB 저장용 URL
	    String dbUrl = "/upload/" + usedType.toLowerCase() + "/" + savedFileName;

	    FileResourceDTO dto = new FileResourceDTO();
	    dto.setUsedId(usedId);
	    dto.setUsedType(usedType.toUpperCase());
	    dto.setOriginalName(ogName);
	    dto.setFileName(savedFileName);
	    dto.setFileType(fileType);
	    dto.setSize(size);
	    dto.setFileUrl(dbUrl);

	    // 기존 파일 삭제 여부
	    if (usedType.equalsIgnoreCase("PROFILE") && fileDao.isExist(usedId, usedType)) {
	        fileDao.deleteFileByUsed(usedType, usedId);
	    }

	    return fileDao.insertFile(dto);
	}
	
	public boolean uploadFileForPost(long usedId, Part filePart, String uploadPath, String usedType, long dreamId) {
	    System.out.println("Service: uploadFile " + usedType);

	    FileResourceDAO fileDao = new FileResourceDAO();

	    String ogName = filePart.getSubmittedFileName();
	    String fileType = filePart.getContentType();
	    long size = filePart.getSize();

	    // 확장자
	    String ext = "";
	    int dotIndex = ogName.lastIndexOf(".");
	    if (dotIndex != -1) ext = ogName.substring(dotIndex);

	    // 저장 파일명
	    String savedFileName = UUID.randomUUID().toString() + ext;

	    // 폴더 생성
	    File dir = new File(uploadPath);
	    if (!dir.exists()) dir.mkdirs();

	    // 저장
	    try {
	        filePart.write(uploadPath + File.separator + savedFileName);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }

	    // DB 저장용 URL
	    String dbUrl = "/upload/" + usedType.toLowerCase() + "/" + dreamId + "/" + savedFileName;

	    FileResourceDTO dto = new FileResourceDTO();
	    dto.setUsedId(usedId);
	    dto.setUsedType(usedType.toUpperCase());
	    dto.setOriginalName(ogName);
	    dto.setFileName(savedFileName);
	    dto.setFileType(fileType);
	    dto.setSize(size);
	    dto.setFileUrl(dbUrl);

	    // 기존 파일 삭제 여부
	    if (usedType.equalsIgnoreCase("PROFILE") && fileDao.isExist(usedId, usedType)) {
	        fileDao.deleteFileByUsed(usedType, usedId);
	    }

	    return fileDao.insertFile(dto);
	}
	public boolean deleteFile(long fileId, long autoId, String usedType) {
		System.out.println("Service: deleteFile");
		boolean result = new FileResourceDAO().deleteFileById(fileId, autoId, usedType);
		
		return result;
	}
	//  다중 파일 삭제용 사용타입 + 사용ID 기준 전체 이미지 삭제
	public boolean deleteAllByUsed(String usedType, long usedId, String uploadRoot) {
	    System.out.println("Service: deleteAllByUsed (with physical files)");

	    FileResourceDAO fileDao = new FileResourceDAO();
	    List<FileResourceDTO> files = fileDao.getFileUrls(usedId, usedType);

	    for (FileResourceDTO file : files) {

	        // 실제 파일 삭제
	        String fileUrl = file.getFileUrl(); // /upload/meeting/uuid.jpg
	        if (fileUrl != null && !fileUrl.isEmpty()) {

	            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
	            File realFile = new File(uploadRoot, fileName);

	            if (realFile.exists()) {
	                boolean deleted = realFile.delete();
	                System.out.println("파일 삭제: " + deleted + " / " + realFile.getAbsolutePath());
	            }
	        }

	        // DB 삭제
	        fileDao.deleteFileById(file.getId(), usedId, usedType);
	    }

	    return true;
	}
	
	public boolean deletePostImage(long usedId, long dreamId, String fileUrl, String uploadRoot) {
	    System.out.println("Service: deletePostImage");

	    if (fileUrl == null || fileUrl.isEmpty()) {
	        return false;
	    }

	    // 1) 로컬 파일 삭제
	    // uploadRoot 예: "C:/upload/post"
	    String dirPath = uploadRoot + File.separator + dreamId;
	    File dir = new File(dirPath);
	    if (dir.exists() && dir.isDirectory()) {
	        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
	        File file = new File(dir, fileName);
	        if (file.exists()) {
	            boolean deleted = file.delete();
	            System.out.println("로컬 파일 삭제: " + deleted + " (" + file.getAbsolutePath() + ")");
	        }
	    }

	    // 2) DB 삭제
	    FileResourceDAO dao = new FileResourceDAO();
	    return dao.deletePostFileByUrl(usedId, "POST", fileUrl);
	}
}

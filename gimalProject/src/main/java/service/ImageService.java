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
	public List<String> getMeetingImage(long meetingId, String usedType){
		System.out.println("work service: getImages");
		List<String> urls = new FileResourceDAO().getFileUrls(meetingId, usedType);
		if(urls == null || urls.isEmpty()) {
			//흑백 사진으로 대체?해야함
			urls.add("/resources/images/default_profile.png");
		}
		return urls;
	}
	//프로필 업로드
	public boolean uploadProfile(long autoId, Part imgPart, String uploadPath) {
		System.out.println("work service: uploadProfile");
		FileResourceDAO fileDao = new FileResourceDAO();
		
		//파일명 가져오기
		String ogName = imgPart.getSubmittedFileName();
		
		//업로드 디렉토리 설정
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) uploadDir.mkdirs();
		
		//파일정보 추출
		String fileType = imgPart.getContentType();
		long size = imgPart.getSize();
		
		
		// 확장자 추출 
	    String ext = "";
	    int dotIndex = ogName.lastIndexOf("."); //.의 인덱스를 반환 없는 경우 -1을 반환
	    if (dotIndex != -1) { 					//확장자가 있는 경우만 실행 -1이 아닌 경우
	        ext = ogName.substring(dotIndex);
	    }
	    
	  //파일명 중복방지 동시 업로드 시에도 방지하기 위해 UUID사용
		String savedFileName = UUID.randomUUID().toString() + ext;
		
        // 파일 저장
        try {
			imgPart.write(uploadPath + File.separator + savedFileName);
			System.out.println("파일 저장 성공!");
		} catch (IOException e) {
			System.out.println("파일 저장 실패!");
			e.printStackTrace();
			return false;
		}
        // 파일 존재 여부 확인
        File savedFile = new File(uploadPath + File.separator + savedFileName);
        if (!savedFile.exists() || !savedFile.isFile()) {
            System.out.println("파일 저장 확인 실패!");
            return false;
        }
        
        // DB에 저장할 경로 
        String dbPath = "/uploads/profile/" + savedFileName;
        
        FileResourceDTO dto = new FileResourceDTO();
        
        dto.setFileUrl(dbPath);
        dto.setFileName(savedFileName);
        dto.setOriginalName(ogName);
        dto.setFileType(fileType);
        dto.setSize(size);
        dto.setUsedType("PROFILE");
        dto.setUsedId(autoId);
        
        // 기존 파일 존재 시 삭제
        if (fileDao.isExist(autoId, "PROFILE")) {
            fileDao.deleteFile("PROFILE", autoId);
        }
        return fileDao.insertFile(dto);

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
		
		return new FileResourceDAO().deleteFile(usedType, autoId);
	}
	public FileResourceDTO uploadMeetImg(long meetId, Part imgPart, String uploadPath) {
		System.out.println("work service: uploadMeeting");
		FileResourceDAO fileDao = new FileResourceDAO();
		
		//파일명 가져오기
		String ogName = imgPart.getSubmittedFileName();
		
		//업로드 디렉토리 설정
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) uploadDir.mkdirs();
		
		//파일정보 추출
		String fileType = imgPart.getContentType();
		long size = imgPart.getSize();
		
		
		// 확장자 추출 
	    String ext = "";
	    int dotIndex = ogName.lastIndexOf("."); //.의 인덱스를 반환 없는 경우 -1을 반환
	    if (dotIndex != -1) { 					//확장자가 있는 경우만 실행 -1이 아닌 경우
	        ext = ogName.substring(dotIndex);
	    }
	    
	  //파일명 중복방지 동시 업로드 시에도 방지하기 위해 UUID사용
		String savedFileName = UUID.randomUUID().toString() + ext;
		
        // 파일 저장
        try {
			imgPart.write(uploadPath + File.separator + savedFileName);
			System.out.println("파일 저장 성공!");
		} catch (IOException e) {
			System.out.println("파일 저장 실패!");
			e.printStackTrace();
			return false;
		}
        // 파일 존재 여부 확인
        File savedFile = new File(uploadPath + File.separator + savedFileName);
        if (!savedFile.exists() || !savedFile.isFile()) {
            System.out.println("파일 저장 확인 실패!");
            return false;
        }
        
        // DB에 저장할 경로 
        String dbPath = "/uploads/meeting/" + savedFileName;
        
        FileResourceDTO dto = new FileResourceDTO();
        
        dto.setFileUrl(dbPath);
        dto.setFileName(savedFileName);
        dto.setOriginalName(ogName);
        dto.setFileType(fileType);
        dto.setSize(size);
        dto.setUsedType("PROFILE");
        dto.setUsedId(autoId);
        
        // 기존 파일 존재 시 삭제
        if (fileDao.isExist(autoId, "PROFILE")) {
            fileDao.deleteFile("PROFILE", autoId);
        }

		return new FileResourceDTO();
	}
}
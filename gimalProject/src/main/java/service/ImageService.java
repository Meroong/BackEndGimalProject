package service;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.http.Part;

public class ImageService {
	
	//프로필 업로드
	public boolean uploadProfile(Part imgPart, String uploadPath) {
		
		//파일명 가져오기
		String fileName = imgPart.getSubmittedFileName();
		
		File uploadDir = new File(uploadPath);
		
		if (!uploadDir.exists()) uploadDir.mkdirs();
		
        // 저장
        try {
			imgPart.write(uploadPath + File.separator + fileName);
			System.out.println("파일 저장 성공!");
		} catch (IOException e) {
			System.out.println("파일 저장 실패!");
			e.printStackTrace();
		}
        
        // DB에 저장할 경로 (예시)
        String dbPath = "/uploads/profile/" + fileName;
		
	}
}

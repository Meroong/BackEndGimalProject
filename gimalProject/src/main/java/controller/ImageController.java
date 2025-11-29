package controller;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import service.ImageService;
import service.UserService;

@WebServlet("/upload/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,     // 1MB 메모리 임계값
        maxFileSize = 1024 * 1024 * 10,      // 파일 최대 10MB
        maxRequestSize = 1024 * 1024 * 50    // 요청 전체 크기 50MB
)
public class ImageController extends HttpServlet {
    private ImageService imageService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		imageService = new ImageService();
		System.out.println("imageController: ON");
	}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	String path = req.getPathInfo(); //            /* 위치에 주소만 가져옴
        
    	// 업로드된 파일 가져오기
        Part imgPart = req.getPart("img");
        
		//저장 경로 설정 웹 경로를 실제 저장 경로로 변경
		String uploadPath = req.getServletContext().getRealPath("uploads/profile");

		imageService.uploadProfile(imgPart, uploadPath);
    }
}

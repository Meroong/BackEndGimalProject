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
import util.AuthUtil;

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
    	boolean result = false;
    	String uploadPath =null;

		// ---------- 로그인 검증 ---------- /util/authUtil.java 에 넣어둠 JwtAuth는 토큰 생성 검증만 하는게
		// 좋아서
		Long autoId = AuthUtil.getAutoId(req);

		if (autoId == -1) {
			resp.sendRedirect("/views/user/login.jsp");
			return;
		}
		switch (path) {
			
			case "/profileUpload":
				System.out.println("upload/profileUpload: ");
		    	// 업로드된 파일 가져오기
		        Part imgPart = req.getPart("img");
		        
				//저장 경로 설정 웹 경로를 실제 저장 경로로 변경
				uploadPath = req.getServletContext().getRealPath("uploads/profile");

				result =imageService.uploadProfile(autoId, imgPart, uploadPath);
				
				if(result) {
					//프로필 url 세션저장
					String profileUrl = new ImageService().getProfileImage(autoId, "PROFILE");
					req.getSession().setAttribute("profileUrl", profileUrl);
					System.out.println(profileUrl);
					
					resp.sendRedirect(req.getContextPath() + "/views/user/mypage.jsp");
				}
				else resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				break;
			
			  case "/profileDelete": 
				  //임시 설정용
				  String usedType = "PROFILE";
				  boolean deleteResult = imageService.deleteProfile(usedType, autoId, req.getServletContext().getRealPath("uploads/profile")); 
				  if(deleteResult) {
					  resp.sendRedirect(req.getContextPath() + "/views/user/mypage.jsp");
				  }
				  else {
					  resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				  } 
				  break;
			  case "meetUpload":
					System.out.println("upload/meetUpload: ");
					long meetingId = req.getParameter(meeting_id);
			    	// 업로드된 파일 가져오기
			        Part meetImgPart = req.getPart("img");
			        
					//저장 경로 설정 웹 경로를 실제 저장 경로로 변경
					uploadPath = req.getServletContext().getRealPath("uploads/meeting");

					result =imageService.uploadProfile(autoId, meetImgPart, uploadPath);
					
					if(result) {
						//프로필 url 세션저장
						String profileUrl = new ImageService().getProfileImage(meetingID, "MEETING");
						req.getSession().setAttribute("profileUrl", profileUrl);
						System.out.println(profileUrl);
						
						resp.sendRedirect(req.getContextPath() + "/views/user/mypage.jsp");
					}
					else resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					break;
				  
			 
		}

    }
}

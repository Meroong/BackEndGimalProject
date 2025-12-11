package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import dao.FileResourceDAO;
import dto.FileResourceDTO;
import dto.MeetingDTO;
import dto.MeetingInfoDTO;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import service.ChattingService;
import service.ImageService;
import service.MeetingService;
import service.ReportService;
import util.AuthUtil;

@WebServlet("/meeting/*")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,    // 1MB
	    maxFileSize = 5 * 1024 * 1024,      // 5MB
	    maxRequestSize = 10 * 1024 * 1024   // 10MB
	)
public class MeetingController extends HttpServlet {
	MeetingService meetingService;

	public void init(ServletConfig config) throws ServletException {
		meetingService = new MeetingService();
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		
		switch(path) {
			case "/list":
			    ArrayList<MeetingInfoDTO> aList = meetingService.getMeetingList();
			    req.setAttribute("meetingList", aList);
			    System.out.println(new Timestamp(System.currentTimeMillis()));
			    // 리스트가 비어 있어도 그대로 JSP로 보냄
			    req.getRequestDispatcher("/views/meet/list.jsp").forward(req, resp);
			    return;
			//게시글 상세 조회 미팅 아이디를 인자로 받음
			case "/info":
				
				// 로그인 여부 확인
			    Long userId = AuthUtil.getAutoId(req);
			    
				boolean isParticipant = false;
					if (userId != -1) {
						isParticipant = meetingService.isParticipant(Long.parseLong(req.getParameter("meetingId")), userId);
				    }

				req.setAttribute("isParticipant", isParticipant);
				MeetingInfoDTO infoDto = meetingService.getMeetingInfo(Long.parseLong(req.getParameter("meetingId")));
				
				 // DTO 전체 정보 출력
			    System.out.println("===== MeetingInfoDTO =====");
			    System.out.println("ID: " + infoDto.getMeetingId());
			    System.out.println("제목: " + infoDto.getTitle());
			    System.out.println("내용: " + infoDto.getContent());
			    System.out.println("날짜: " + infoDto.getDate());
			    System.out.println("장소ID: " + infoDto.getLocationId());
			    System.out.println("최대 인원: " + infoDto.getMaxMembers());
			    System.out.println("현재 인원: " + infoDto.getCurrentMembers());
			    System.out.println("참가비: " + infoDto.getCost());
			    System.out.println("태그: " + infoDto.getTag());
			    System.out.println("상태: " + infoDto.getStatus());
			    System.out.println("생성일: " + infoDto.getCreatedAt());
			    System.out.println("수정일: " + infoDto.getUpdatedAt());
			    System.out.println("날씨: " + infoDto.getWeather());

			    System.out.println("--- 장소 정보 ---");
			    System.out.println("도로명 주소: " + infoDto.getRoadAddress());
			    System.out.println("지번 주소: " + infoDto.getJibunAddress());
			    System.out.println("상세 주소: " + infoDto.getAddrDetail());
			    System.out.println("위도: " + infoDto.getLatitude());
			    System.out.println("경도: " + infoDto.getLongitude());

			    System.out.println("--- 이미지 ---");
			    if(infoDto.getImages() != null) {
			        for(FileResourceDTO imgDto : infoDto.getImages()) {
			            System.out.println("이미지 URL: " + imgDto.getFileUrl());
			        }
			    } else {
			        System.out.println("이미지 없음");
			    }
			    // 신고 여부 체크
			    boolean hasReported = false;

			    if (userId != -1) {
			        hasReported = new ReportService().hasAlreadyReported(
			                userId,
			                infoDto.getCreatorId(),
			                "MEETING"
			        );
			    }

			    req.setAttribute("hasReported", hasReported);
			    //조회수 증가시키기
			    meetingService.increaseViewCount(infoDto.getMeetingId());
			    
			    if(AuthUtil.getAutoId(req) == infoDto.getCreatorId()) {
			    	boolean isCreator =true;
			    	req.setAttribute("isCreator", isCreator);
			    }
				
				if(infoDto != null) {
					req.setAttribute("meetingInfo", infoDto);
					req.getRequestDispatcher("/views/meet/info.jsp").forward(req, resp);
				}
				return;
				
			case "/edit":
			    long meetingId = Long.parseLong(req.getParameter("meetingId"));
			    MeetingInfoDTO dto = meetingService.getMeetingInfo(meetingId);

			    if(dto != null) {
			        req.setAttribute("meetingInfo", dto);
			        req.getRequestDispatcher("/views/meet/meetUpdate.jsp").forward(req, resp);
			    } else {
			        resp.sendRedirect(req.getContextPath() + "/meeting/list");
			    }
			    return;

		}
}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo(); // /roomMake
        
        // ---------- 로그인 검증 ----------
        Long autoId = AuthUtil.getAutoId(req);

        if (autoId == -1) {
            HttpSession session = req.getSession();

            // 현재 요청 URL + 쿼리스트링 조회
            String currentUrl = req.getRequestURI();
            String queryString = req.getQueryString();
            if (queryString != null && !queryString.isEmpty()) {
                currentUrl += "?" + queryString;
            }

            // 세션에 저장
            session.setAttribute("redirectAfterLogin", currentUrl);

            // 로그인 페이지로 이동
            resp.sendRedirect(req.getContextPath() + "/views/user/login.jsp");
            return;
        }
        
        ImageService imageService = new ImageService();
	    String latitudeStr = req.getParameter("latitude");
	    String longitudeStr = req.getParameter("longitude");

	    double latitude = (latitudeStr != null && !latitudeStr.isEmpty()) ? Double.parseDouble(latitudeStr) : 37.1;
	    double longitude = (longitudeStr != null && !longitudeStr.isEmpty()) ? Double.parseDouble(longitudeStr) : 107.1;
	    String uploadPath = "C:/upload/meeting";
	    String usedType = "MEETING";
	    
	    try {
	    	System.out.println(path);
	        switch (path) {
	        
	        
	        
	        //INSERT create로 변경할까 
            case "/insert":
                // location insert
                long newLocationId = meetingService.insertLocation(
                        req.getParameter("roadAddress"),
                        req.getParameter("jibunAddress"),
                        req.getParameter("addrDetail"),
                        latitude,
                        longitude
                );

                // meeting insert
                String dateStrInsert = req.getParameter("date");

                if (dateStrInsert == null || dateStrInsert.isEmpty()) {
                    throw new Exception("날짜 값이 전달되지 않았습니다.");
                }

                dateStrInsert = dateStrInsert.length() == 10 ? dateStrInsert + " 00:00:00" : dateStrInsert;

                long meetingId = meetingService.insertMeetingInfo(
                        req.getParameter("title"),
                        req.getParameter("content"),
                        Timestamp.valueOf(dateStrInsert),
                        newLocationId,
                        Integer.parseInt(req.getParameter("maxMembers")),
                        Integer.parseInt(req.getParameter("currentMembers")),
                        Integer.parseInt(req.getParameter("cost")),
                        req.getParameter("tag"),
                        req.getParameter("status"),
                        latitude,
                        longitude,
                        autoId //로그인 추가시 동작
                );

                /* ================================
                 *   다중 이미지 업로드 처리
                 * ================================ */
                for (Part part : req.getParts()) {
                    if ("images".equals(part.getName()) && part.getSize() > 0) {
                        imageService.uploadFile(
                                meetingId,
                                part,
                                uploadPath,
                                usedType);
                    }
                }
                //호스트 모임참가자에 넣기 
                meetingService.joinMeet(meetingId, autoId);
                new ChattingService().makeGroupRoom(meetingId, "Group", autoId); // meetingId, hostId(creator)
                
                resp.sendRedirect(req.getContextPath() + "/meeting/list");
                return;


	            /* ========================
	             * UPDATE
	             * ======================== */
	            case "/update":
	                long locationId = Long.parseLong(req.getParameter("locationId"));
	                long meetingUpId = Long.parseLong(req.getParameter("meetingId"));

	                // location 업데이트
	                meetingService.updateLocation(
	                        locationId,
	                        req.getParameter("roadAddress"),
	                        req.getParameter("jibunAddress"),
	                        req.getParameter("addrDetail"),
	                        latitude,
	                        longitude
	                );

	                // meeting 업데이트
	                String dateStr = req.getParameter("date");
	                dateStr = dateStr.length() == 10 ? dateStr + " 00:00:00" : dateStr;
	                Timestamp date = Timestamp.valueOf(dateStr);

	                meetingService.updateMeetingInfo(
	                        meetingUpId,
	                        req.getParameter("title"),
	                        req.getParameter("content"),
	                        date,
	                        locationId,
	                        Integer.parseInt(req.getParameter("maxMembers")),
	                        Integer.parseInt(req.getParameter("currentMembers")),
	                        Integer.parseInt(req.getParameter("cost")),
	                        req.getParameter("tag"),
	                        req.getParameter("status"),
	                        latitude,
	                        longitude,
	                        autoId  //로그인 추가 시 해결
	                );
	        
	                //이미지 삭제 처리
	                String[] deleteIds = req.getParameterValues("deleteImageIds");

	                if (deleteIds != null) {
	                    FileResourceDAO fileDao = new FileResourceDAO();

	                    for (String idStr : deleteIds) {

	                        if (idStr == null || idStr.isBlank() || idStr.equals("null")) {
	                            continue; // ← 건너뛰기
	                        }

	                        long fileId = Long.parseLong(idStr);
	                        imageService.deleteFile(fileId, meetingUpId, usedType);
	                    }
	                }
	                //새 이미지 업로드 처리
	                for (Part part : req.getParts()) {
	                    if ("images".equals(part.getName()) && part.getSize() > 0) {
	                        imageService.uploadFile(
	                                meetingUpId,
	                                part,
	                                uploadPath,
	                                "MEETING"
	                        );
	                    }
	                }
	                
	                

	                // 성공 시
	                resp.sendRedirect(req.getContextPath() + "/meeting/list");
	                return;
	                
	            case "/join":

				    // meetingId 파라미터
				    long meetId = Long.parseLong(req.getParameter("meetingId"));

				    try {
				 
				        boolean result	=	meetingService.joinMeet(meetId, autoId);
				        if (result) {
				            resp.sendRedirect(req.getContextPath() + "/meeting/info?meetingId=" + meetId);
				        }
				    } catch (Exception e) {
				        e.printStackTrace();
				        req.setAttribute("errorMsg", e.getMessage());

				        // 에러 메시지와 함께 상세 페이지로 되돌리기
				        req.getRequestDispatcher("/meeting/info?meetingId=" + meetId)
				           .forward(req, resp);
				    }
				    return;
				    
				    /* ========================================
	                   QUIT
	                 ======================================== */
	                case "/quit":

	                    long quitMeetId = Long.parseLong(req.getParameter("meetingId"));

	                    try {
	                        boolean quitResult = meetingService.quitMeet(quitMeetId, autoId);

	                        if (quitResult) {
	                            resp.sendRedirect(req.getContextPath() + "/meeting/info?meetingId=" + quitMeetId);
	                        }

	                    } catch (Exception e) {
	                        e.printStackTrace();
	                        req.setAttribute("errorMsg", e.getMessage());
	                        req.getRequestDispatcher("/meeting/info?meetingId=" + quitMeetId).forward(req, resp);
	                    }
	                    return;
	                    
	                case "/delete":
	                    long deleteMeetingId = Long.parseLong(req.getParameter("meetingId"));
	                    Long loginUserId = AuthUtil.getAutoId(req);

	                    // 로그인 검증(이미 위에서 처리되지만 혹시 모르니)
	                    if (loginUserId == -1) {
	                        resp.sendRedirect(req.getContextPath() + "/views/user/login.jsp");
	                        return;
	                    }

	                    // 해당 모임 정보 조회
	                    MeetingInfoDTO meetingInfo = meetingService.getMeetingInfo(deleteMeetingId);

	                    if (meetingInfo == null) {
	                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "삭제할 모임을 찾을 수 없습니다.");
	                        return;
	                    }

	                    // 작성자 검증
	                    if (!loginUserId.equals(meetingInfo.getCreatorId())) {
	                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "모임 작성자만 삭제할 수 있습니다.");
	                        return;
	                    }

	                    // 삭제 실행
	                    boolean deleted = meetingService.deleteMeeting(deleteMeetingId, loginUserId);

	                    if (deleted) {
	                        resp.sendRedirect(req.getContextPath() + "/meeting/list");
	                    } else {
	                        resp.setContentType("text/html; charset=UTF-8");
	                        resp.getWriter().println("<script>alert('삭제 실패. 관리자에게 문의하세요.'); history.back();</script>");
	                    }
	                    return;




	            default:
	                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "잘못된 요청 경로입니다.");
	        }

	    	} catch (Exception e) {
	        e.printStackTrace();

	        // 에러 메시지를 request에 담아서 회원가입/모임 페이지로 포워딩
	        req.setAttribute("errorMsg", e.getMessage());

	        // 기존 입력값도 유지
	        req.setAttribute("roadAddress", req.getParameter("roadAddress"));
	        req.setAttribute("jibunAddress", req.getParameter("jibunAddress"));
	        req.setAttribute("addrDetail", req.getParameter("addrDetail"));
	        req.setAttribute("title", req.getParameter("title"));
	        req.setAttribute("content", req.getParameter("content"));
	        req.setAttribute("date", req.getParameter("date"));
	        req.setAttribute("maxMembers", req.getParameter("maxMembers"));
	        req.setAttribute("currentMembers", req.getParameter("currentMembers"));
	        req.setAttribute("cost", req.getParameter("cost"));
	        req.setAttribute("tag", req.getParameter("tag"));
	        req.setAttribute("status", req.getParameter("status"));

	        // 포워딩
	        req.getRequestDispatcher("/meetingForm.jsp").forward(req, resp);
	    }
	}

}



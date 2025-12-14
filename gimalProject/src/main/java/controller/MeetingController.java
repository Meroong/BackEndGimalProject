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
	
	private static final long serialVersionUID = 1L;
	MeetingService meetingService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		meetingService = new MeetingService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("URI = " + req.getRequestURI());
		System.out.println("PathInfo = " + req.getPathInfo());
		String path = req.getPathInfo();
		switch(path) {
		case "/list": {

		    Long listUserId = AuthUtil.getAutoId(req);

		    // 필터 파라미터 읽기
		    String category = req.getParameter("category");
		    String dateFrom = req.getParameter("dateFrom");   // yyyy-MM-dd
		    String dateTo   = req.getParameter("dateTo");     // yyyy-MM-dd
		    String keyword  = req.getParameter("keyword");
		    String status   = req.getParameter("status");
		    String weather  = req.getParameter("weather");

		    System.out.println("=== Meeting list filter ===");
		    System.out.println("category = " + category);
		    System.out.println("dateFrom = " + dateFrom);
		    System.out.println("dateTo   = " + dateTo);
		    System.out.println("keyword  = " + keyword);
		    System.out.println("status   = " + status);
		    System.out.println("weather  = " + weather);

		    // 필터 존재 여부 판단
		    boolean hasFilter =
		           (category != null && !category.isBlank() && !"전체".equals(category))
		        || (dateFrom != null && !dateFrom.isBlank())
		        || (dateTo   != null && !dateTo.isBlank())
		        || (keyword  != null && !keyword.isBlank())
		        || (status   != null && !status.isBlank() && !"ALL".equals(status))
		        || (weather  != null && !weather.isBlank() && !"ALL".equals(weather));

		    ArrayList<MeetingInfoDTO> aList;

		    // 필터 / 전체 분기
		    if (hasFilter) {
		        aList = meetingService.getMeetingListFiltered(
		                category,
		                dateFrom,
		                dateTo,
		                keyword,
		                status,
		                weather
		        );
		    } else {
		        aList = meetingService.getMeetingList(listUserId);
		    }

		    // JSP 전달 (선택 상태 유지용)
		    req.setAttribute("meetingList", aList);
		    req.setAttribute("selectedCategory", category);
		    req.setAttribute("selectedDateFrom", dateFrom);
		    req.setAttribute("selectedDateTo", dateTo);
		    req.setAttribute("keyword", keyword);
		    req.setAttribute("selectedStatus", status);
		    req.setAttribute("selectedWeather", weather);

		    req.getRequestDispatcher("/WEB-INF/views/meet/list.jsp").forward(req, resp);
		    return;
		}

			//게시글 상세 조회 미팅 아이디를 인자로 받음
			case "/info": {
	
			    // meetingId 파라미터
			    long meetingId = Long.parseLong(req.getParameter("meetingId"));
	
			    // 로그인 사용자
			    Long userId = AuthUtil.getAutoId(req);
	
			    // 참여 여부
			    boolean isParticipant = false;
			    if (userId != -1) {
			        isParticipant = meetingService.isParticipant(meetingId, userId);
			    }
			    req.setAttribute("isParticipant", isParticipant);
	
			    // 모임 조회
			    MeetingInfoDTO infoDto = meetingService.getMeetingInfo(meetingId);
	
			    if (infoDto == null) {
			        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			        return;
			    }
	
			    /* ===== 로그 출력 ===== */
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
			    System.out.println("날씨: " + infoDto.getWeather());
	
			    System.out.println("--- 장소 정보 ---");
			    System.out.println("도로명 주소: " + infoDto.getRoadAddress());
			    System.out.println("지번 주소: " + infoDto.getJibunAddress());
			    System.out.println("상세 주소: " + infoDto.getAddrDetail());
			    System.out.println("위도: " + infoDto.getLatitude());
			    System.out.println("경도: " + infoDto.getLongitude());
	
			    System.out.println("--- 이미지 ---");
			    if (infoDto.getImages() != null) {
			        for (FileResourceDTO imgDto : infoDto.getImages()) {
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
	
			    // 조회수 증가
			    meetingService.increaseViewCount(meetingId);
	
			    // 작성자 여부
			    if (userId != -1 && userId.equals(infoDto.getCreatorId())) {
			        req.setAttribute("isCreator", true);
			    }
	
			    // JSP 전달
			    req.setAttribute("meetingInfo", infoDto);
			    req.getRequestDispatcher("/WEB-INF/views/meet/info.jsp").forward(req, resp);
			    return;
			}

				
			/* =========================
			 *  모임 수정 화면
			 * ========================= */
			case "/edit": {
				System.out.println("edit");
				long meetingId = Long.parseLong(req.getParameter("meetingId"));
				MeetingInfoDTO dto = meetingService.getMeetingInfo(meetingId);
				
			    Long autoId = AuthUtil.getAutoId(req);
			    if (autoId == -1) {
			        // ✅ 로그인 후 다시 edit로 돌아오게
			        String currentUrl = req.getRequestURI();
			        String qs = req.getQueryString();
			        if (qs != null && !qs.isBlank()) currentUrl += "?" + qs;

			        // 컨텍스트 제거한 path로 저장 (로그인 컨트롤러가 contextPath 붙여서 redirect 하게)
			        String page = currentUrl.replace(req.getContextPath(), "");
			        req.getSession().setAttribute("LOGIN_REDIRECT", page);

			        resp.sendRedirect(req.getContextPath() + "/page/login");
			        return;
			    }

				if (dto != null) {
					req.setAttribute("meetingInfo", dto);
					req.getRequestDispatcher("/WEB-INF/views/meet/meetUpdate.jsp").forward(req, resp);
				} else {
					resp.sendRedirect(req.getContextPath() + "/meeting/list");
				}
				return;
			}

			default:
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "잘못된 요청 경로입니다.");
				return;
			}
		}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo(); // /roomMake
        
        // ---------- 로그인 검증 ----------
        Long autoId = AuthUtil.getAutoId(req);

        if (autoId == -1) {
            HttpSession session = req.getSession();

            String referer = req.getHeader("Referer");
            if (referer != null && !referer.isBlank()) {
                // referer가 전체 URL이든 뭐든, contextPath 이후만 뽑아서 저장
                String ctx = req.getContextPath(); // /gimalProject
                int idx = referer.indexOf(ctx);

                if (idx != -1) {
                    String page = referer.substring(idx + ctx.length()); // /meeting/info?meetingId=5 형태
                    if (page.isBlank()) page = "/home";
                    if (!page.startsWith("/")) page = "/" + page;

                    session.setAttribute("LOGIN_REDIRECT", page);
                }
            }

            resp.sendRedirect(req.getContextPath() + "/page/login");
            return;
        }
        
        ImageService imageService = new ImageService();
	    String latitudeStr = req.getParameter("latitude");
	    String longitudeStr = req.getParameter("longitude");

	    double latitude = (latitudeStr != null && !latitudeStr.isEmpty()) ? Double.parseDouble(latitudeStr) : 37.1;
	    double longitude = (longitudeStr != null && !longitudeStr.isEmpty()) ? Double.parseDouble(longitudeStr) : 107.1;
	    String uploadPath = "C:/upload/meeting";
	    String usedType = "MEETING";
	    
	    
	    	System.out.println(path);
	        switch (path) {
	        
	        
	        
	        //INSERT create로 변경할까 
	        case "/insert":
	            try {
	                // ========================
	                // 1. location insert
	                // ========================
	                long locationId = meetingService.insertLocation(
	                        req.getParameter("roadAddress"),
	                        req.getParameter("jibunAddress"),
	                        req.getParameter("addrDetail"),
	                        latitude,
	                        longitude
	                );

	                // ========================
	                // 2. meeting date 처리
	                // ========================
	                String dateStr = req.getParameter("date");
	                if (dateStr == null || dateStr.isBlank()) {
	                    throw new Exception("날짜 값이 전달되지 않았습니다.");
	                }

	                dateStr = dateStr.replace("T", " ");
	                if (dateStr.length() == 16) dateStr += ":00";
	                if (dateStr.length() == 10) dateStr += " 00:00:00";

	                Timestamp meetingDate = Timestamp.valueOf(dateStr);

	                // ========================
	                // 3. meeting insert
	                // ========================
	                long meetingId = meetingService.insertMeetingInfo(
	                        req.getParameter("title"),
	                        req.getParameter("content"),
	                        meetingDate,
	                        locationId,
	                        Integer.parseInt(req.getParameter("maxMembers")),
	                        Integer.parseInt(req.getParameter("currentMembers")) - 1,
	                        Integer.parseInt(req.getParameter("cost")),
	                        req.getParameter("tag"),
	                        req.getParameter("status"),
	                        latitude,
	                        longitude,
	                        autoId
	                );

	                // ========================
	                // 4. 이미지 업로드
	                // ========================
	                for (Part part : req.getParts()) {
	                    if ("images".equals(part.getName()) && part.getSize() > 0) {
	                        imageService.uploadFile(
	                                meetingId,
	                                part,
	                                uploadPath,
	                                usedType
	                        );
	                    }
	                }

	                // ========================
	                // 5. 참가 + 채팅방 생성
	                // ========================
	                meetingService.joinMeet(meetingId, autoId);
	                new ChattingService().makeGroupRoom(meetingId, "Group", autoId);

	                // 성공 → 목록
	                resp.sendRedirect(req.getContextPath() + "/meeting/list");
	                return;

	            } catch (Exception e) {
	                e.printStackTrace();

	                // ========================
	                // ❗ 입력값 유지
	                // ========================
	                req.setAttribute("errorMsg", e.getMessage());

	                req.setAttribute("title", req.getParameter("title"));
	                req.setAttribute("content", req.getParameter("content"));
	                req.setAttribute("date", req.getParameter("date"));
	                req.setAttribute("maxMembers", req.getParameter("maxMembers"));
	                req.setAttribute("currentMembers", req.getParameter("currentMembers"));
	                req.setAttribute("cost", req.getParameter("cost"));
	                req.setAttribute("tag", req.getParameter("tag"));
	                req.setAttribute("status", req.getParameter("status"));

	                req.setAttribute("roadAddress", req.getParameter("roadAddress"));
	                req.setAttribute("jibunAddress", req.getParameter("jibunAddress"));
	                req.setAttribute("addrDetail", req.getParameter("addrDetail"));

	                // ❗ 생성 페이지로만 복귀
	                req.getRequestDispatcher("/WEB-INF/views/meet/meetForm.jsp").forward(req, resp);
	                return;
	            }

	            /* ========================
	             * UPDATE
	             * ======================== */
            case "/update": {

                try {
                    // ---------- 파라미터 파싱 ----------
                    long locationId = Long.parseLong(req.getParameter("locationId"));
                    long meetingUpId = Long.parseLong(req.getParameter("meetingId"));

                    String roadAddress = req.getParameter("roadAddress");
                    String jibunAddress = req.getParameter("jibunAddress");
                    String addrDetail = req.getParameter("addrDetail");

                    int maxMembers = Integer.parseInt(req.getParameter("maxMembers"));
                    int currentMembers = Integer.parseInt(req.getParameter("currentMembers"));
                    int cost = Integer.parseInt(req.getParameter("cost"));

                    String title = req.getParameter("title");
                    String content = req.getParameter("content");
                    String tag = req.getParameter("tag");
                    String status = req.getParameter("status");

                    // ---------- 날짜 처리 ----------
                    String dateStr = req.getParameter("date");
                    if (dateStr == null || dateStr.isBlank()) {
                        throw new Exception("날짜 값이 전달되지 않았습니다.");
                    }
                    dateStr = dateStr.replace("T", " ");
                    if (dateStr.length() == 16) dateStr += ":00";
                    if (dateStr.length() == 10) dateStr += " 00:00:00";
                    Timestamp date = Timestamp.valueOf(dateStr);

                    // ---------- 위치 업데이트 ----------
                    meetingService.updateLocation(
                            locationId,
                            roadAddress,
                            jibunAddress,
                            addrDetail,
                            latitude,
                            longitude
                    );

                    // ---------- 모임 정보 업데이트 ----------
                    meetingService.updateMeetingInfo(
                            meetingUpId,
                            title,
                            content,
                            date,
                            locationId,
                            maxMembers,
                            currentMembers,
                            cost,
                            tag,
                            status,
                            latitude,
                            longitude,
                            autoId   // 작성자 검증용
                    );

                    // ---------- 이미지 처리  ----------
                    String[] deleteIds = req.getParameterValues("deleteImageIds");

                    boolean imageResult = imageService.updateMeetingImages(
                            meetingUpId,
                            deleteIds,
                            req.getParts(),
                            uploadPath
                    );

                    if (!imageResult) {
                        System.out.println("⚠ 이미지 처리 중 일부 실패");
                    }

                    // ---------- 성공 ----------
                    resp.sendRedirect(req.getContextPath() + "/meeting/list");
                    return;

                } catch (Exception e) {
                    e.printStackTrace();

                    resp.setContentType("text/html; charset=UTF-8");
                    resp.getWriter().println(
                        "<script>alert('모임 수정 중 오류가 발생했습니다.'); history.back();</script>"
                    );
                    return;
                }
            }

	                
	            case "/status": {

	                Long loginUserId = AuthUtil.getAutoId(req);
	                if (loginUserId == -1) {
	                	resp.sendRedirect(req.getContextPath() + "/page/login");
	                    return;
	                }

	                long statusMeetId = Long.parseLong(req.getParameter("meetingId"));
	                String status = req.getParameter("status"); // OPEN / CLOSED

	                // 모임 조회
	                MeetingInfoDTO meetingInfo = meetingService.getMeetingInfo(statusMeetId);
	                if (meetingInfo == null) {
	                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	                    return;
	                }

	                // 작성자 검증
	                if (!loginUserId.equals(meetingInfo.getCreatorId())) {
	                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
	                    return;
	                }
	                //모임 재개 시 날짜 검증
	                if ("OPEN".equals(status)) {
	                    Timestamp now = new Timestamp(System.currentTimeMillis());

	                    if (meetingInfo.getDate() != null && meetingInfo.getDate().before(now)) {
	                        resp.setContentType("text/html; charset=UTF-8");
	                        resp.getWriter().println(
	                            "<script>alert('이미 지난 모임은 모집을 재개할 수 없습니다.'); history.back();</script>"
	                        );
	                        return;
	                    }
	                }

	                // 상태 변경
	                meetingService.updateMeetingStatus(statusMeetId, status);

	                resp.sendRedirect(req.getContextPath() + "/meeting/info?meetingId=" + statusMeetId);
	                return;
	            }
	                
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
				        resp.sendRedirect(req.getContextPath() + "/meeting/info?meetingId=" + meetId);
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
	                        resp.sendRedirect(req.getContextPath() + "/meeting/info?meetingId=" + quitMeetId);
	                    }
	                    return;
	                    
	                case "/delete": {

	                    // 로그인 확인
	                    Long loginUserId = AuthUtil.getAutoId(req);
	                    if (loginUserId == -1) {
	                    	resp.sendRedirect(req.getContextPath() + "/page/login");
	                        return;
	                    }

	                    // 파라미터 검증
	                    String meetingIdParam = req.getParameter("meetingId");
	                    if (meetingIdParam == null) {
	                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
	                        return;
	                    }

	                    long deleteMeetId;
	                    try {
	                    	deleteMeetId = Long.parseLong(meetingIdParam);
	                    } catch (NumberFormatException e) {
	                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 모임 ID입니다.");
	                        return;
	                    }

	                    // 모임 존재 여부 + 작성자 조회
	                    MeetingInfoDTO meetingInfo = meetingService.getMeetingInfo(deleteMeetId);
	                    if (meetingInfo == null) {
	                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "삭제할 모임이 존재하지 않습니다.");
	                        return;
	                    }

	                    // 작성자 검증 (🔥 Controller 책임)
	                    if (!loginUserId.equals(meetingInfo.getCreatorId())) {
	                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "모임 작성자만 삭제할 수 있습니다.");
	                        return;
	                    }

	                    // 서비스 호출
	                    boolean result = meetingService.deleteMeeting(deleteMeetId, loginUserId);

	                    // 결과 처리
	                    if (result) {
	                        resp.sendRedirect(req.getContextPath() + "/meeting/list");
	                    } else {
	                        resp.setContentType("text/html; charset=UTF-8");
	                        resp.getWriter().println(
	                            "<script>alert('삭제 중 오류가 발생했습니다.'); history.back();</script>"
	                        );
	                    }

	                    return;
	                }


	            default:
	                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "잘못된 요청 경로입니다.");
	        }
	}

}


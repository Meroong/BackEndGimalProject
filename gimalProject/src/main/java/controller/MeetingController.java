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

		    req.getRequestDispatcher("/views/meet/list.jsp").forward(req, resp);
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
			    req.getRequestDispatcher("/views/meet/info.jsp").forward(req, resp);
			    return;
			}

				
			/* =========================
			 *  모임 수정 화면
			 * ========================= */
			case "/edit": {
				long meetingId = Long.parseLong(req.getParameter("meetingId"));
				MeetingInfoDTO dto = meetingService.getMeetingInfo(meetingId);

				if (dto != null) {
					req.setAttribute("meetingInfo", dto);
					req.getRequestDispatcher("/views/meet/meetUpdate.jsp").forward(req, resp);
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

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo(); // /insert, /update, /join, /quit 등

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

			/* ========================
			 *  INSERT
			 * ======================== */
			case "/insert": {
				// location insert
				long newLocationId = meetingService.insertLocation(
						req.getParameter("roadAddress"),
						req.getParameter("jibunAddress"),
						req.getParameter("addrDetail"),
						latitude,
						longitude);

				// meeting insert
				String dateStrInsert = req.getParameter("date");

				if (dateStrInsert == null || dateStrInsert.isEmpty()) {
					throw new Exception("날짜 값이 전달되지 않았습니다.");
				}

				dateStrInsert = (dateStrInsert.length() == 10)
						? dateStrInsert + " 00:00:00"
						: dateStrInsert;

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
						autoId // 로그인 사용자 ID
				);

				// 다중 이미지 업로드
				for (Part part : req.getParts()) {
					if ("images".equals(part.getName()) && part.getSize() > 0) {
						imageService.uploadFile(
								meetingId,
								part,
								uploadPath,
								usedType);
					}
				}

				// 호스트를 모임 참가자로 등록
				meetingService.joinMeet(meetingId, autoId);
				new ChattingService().makeGroupRoom(meetingId, "Group", autoId);

				resp.sendRedirect(req.getContextPath() + "/meeting/list");
				return;
			}

			/* ========================
			 *  UPDATE
			 * ======================== */
			case "/update": {
				long locationId = Long.parseLong(req.getParameter("locationId"));
				long meetingUpId = Long.parseLong(req.getParameter("meetingId"));

				// location 업데이트
				meetingService.updateLocation(
						locationId,
						req.getParameter("roadAddress"),
						req.getParameter("jibunAddress"),
						req.getParameter("addrDetail"),
						latitude,
						longitude);

				// meeting 업데이트
				String dateStr = req.getParameter("date");
				dateStr = (dateStr.length() == 10) ? dateStr + " 00:00:00" : dateStr;
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
						autoId);

				// 이미지 삭제 처리
				String[] deleteIds = req.getParameterValues("deleteImageIds");

				if (deleteIds != null) {
					FileResourceDAO fileDao = new FileResourceDAO(); // 필요 시 내부에서 사용

					for (String idStr : deleteIds) {
						if (idStr == null || idStr.isBlank() || "null".equals(idStr)) {
							continue;
						}
						long fileId = Long.parseLong(idStr);
						imageService.deleteFile(fileId, meetingUpId, usedType);
					}
				}

				// 새 이미지 업로드 처리
				for (Part part : req.getParts()) {
					if ("images".equals(part.getName()) && part.getSize() > 0) {
						imageService.uploadFile(
								meetingUpId,
								part,
								uploadPath,
								"MEETING");
					}
				}

				resp.sendRedirect(req.getContextPath() + "/meeting/list");
				return;
			}

			/* ========================
			 *  JOIN
			 * ======================== */
			case "/join": {
				long meetId = Long.parseLong(req.getParameter("meetingId"));

				try {
					boolean result = meetingService.joinMeet(meetId, autoId);
					if (result) {
						resp.sendRedirect(req.getContextPath() + "/meeting/info?meetingId=" + meetId);
					}
				} catch (Exception e) {
					e.printStackTrace();
					req.setAttribute("errorMsg", e.getMessage());
					req.getRequestDispatcher("/meeting/info?meetingId=" + meetId)
							.forward(req, resp);
				}
				return;
			}

			/* ========================
			 *  QUIT
			 * ======================== */
			case "/quit": {
				long quitMeetId = Long.parseLong(req.getParameter("meetingId"));

				try {
					boolean quitResult = meetingService.quitMeet(quitMeetId, autoId);

					if (quitResult) {
						resp.sendRedirect(req.getContextPath() + "/meeting/info?meetingId=" + quitMeetId);
					}

				} catch (Exception e) {
					e.printStackTrace();
					req.setAttribute("errorMsg", e.getMessage());
					req.getRequestDispatcher("/meeting/info?meetingId=" + quitMeetId)
							.forward(req, resp);
				}
				return;
			}

			default:
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "잘못된 요청 경로입니다.");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();

			// 에러 메시지를 request에 담아서 폼으로 포워딩
			req.setAttribute("errorMsg", e.getMessage());

			// 기존 입력값 유지
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

			// 폼 JSP 경로는 프로젝트 구조에 맞게 조정
			req.getRequestDispatcher("/views/meet/meetForm.jsp").forward(req, resp);
		}
	}
}

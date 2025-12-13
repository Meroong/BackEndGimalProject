package controller;

import java.io.IOException;
import java.util.ArrayList;

import dto.ChatMessageDTO;
import dto.ChatRoomDTO;
import dto.MeetingInfoDTO;
import dto.MeetingParticipantDTO;
import dto.UserDTO;
import dto.chatParticipantsUserDTO;
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
import service.PollService;
import service.UserService;
import util.AuthUtil;

@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,  // 1MB
	    maxFileSize = 5 * 1024 * 1024,     // 5MB
	    maxRequestSize = 20 * 1024 * 1024  // 20MB
	)
@WebServlet("/chat/*")
public class ChattingController extends HttpServlet {
    ChattingService service;

    public void init(ServletConfig config) throws ServletException {
        service = new ChattingService();
        System.out.println("ChattingController: ON");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo();   // /rooms, /roomDelete/12

        // ---------- 로그인 검증 ----------  /util/authUtil.java 에 넣어둠 JwtAuth는 토큰 생성 검증만 하는게 좋아서
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

        // ---------- 채팅방 리스트 ----------
        if ("/roomList".equals(path)) {
        	System.out.println("roomList 요청");
            ArrayList<ChatRoomDTO> chatRooms =  service.getRoomList(autoId); //chatList를 그대로 받아
            req.setAttribute("chatList", chatRooms);
            req.getRequestDispatcher("/views/chat/chatRoomList.jsp").forward(req, resp);
            return;
        }
        
        // !!---------- 선택한 채팅방 메시지 ----------  
        else if (path != null && path.startsWith("/room/")) {
        	//chatting.jsp에서 현재 로그인 사용자 확인하기 위해 사용
        	req.setAttribute("loginUserId", autoId);
            String[] parts = path.split("/");
            if (parts.length != 3) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Room ID Missing");
                return;
            }

            Long roomId = Long.valueOf(parts[2]);

            // 방 참여 여부 확인
            if (!service.checkUserInRoom(autoId, roomId)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Not allowed");
                return;
            }

            // 채팅 메시지 조회
            req.setAttribute("messages", service.getMessage(roomId));
            req.setAttribute("selectedRoomId", roomId);

            // 방 정보 조회
            ChatRoomDTO roomDto = service.getRoomInfo(roomId);
            req.setAttribute("roomInfo", roomDto);

            // 채팅방 참여자(UserDTO)
            req.setAttribute("chatUsers", service.getUserInfoListInRoom(roomId));
            
            // 그룹인 경우 회비정보 가져오기
            if ("GROUP".equalsIgnoreCase(roomDto.getRoomType())
                    && roomDto.getMeetingId() != null) {

                try {
                    int meetingCost = new MeetingService().getMeetingCost(roomDto.getMeetingId());
                    req.setAttribute("meetingCost", meetingCost);
                } catch (Exception e) {
                    // 회비 정보 못 가져와도 채팅방은 정상 진입하게 처리
                    req.setAttribute("meetingCost", null);
                }
                try {
                    boolean hasPaid = new MeetingService().hasUserPaid(roomDto.getMeetingId(), autoId);
                    req.setAttribute("hasPaid", hasPaid);
                } catch (Exception e) {
                    req.setAttribute("hasPaid", false);
                }
                PollService pollService = new PollService();
                try {
					req.setAttribute("voteList", pollService.getPollListByRoom(roomId));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            // 호스트 여부
            boolean isHost = (autoId == roomDto.getHostId());
            req.setAttribute("isHost", isHost);

            // 그룹일 경우 → 모임 전체 참여자 정보
            if (isHost && "GROUP".equalsIgnoreCase(roomDto.getRoomType())) {
                ArrayList<chatParticipantsUserDTO> users = service.getParticipantUsers(roomDto);
                req.setAttribute("participantUsers", users);
            }
            // 체크: wallet/pay에서 전달한 메시지 처리
            HttpSession session = req.getSession();

            String success = (String) session.getAttribute("successMessage");
            if (success != null) {
                req.setAttribute("successMessage", success);  // JSP에서 1회 사용
                session.removeAttribute("successMessage");    // 1회성 메시지 제거!
            }

            String error = (String) session.getAttribute("errorMessage");
            if (error != null) {
                req.setAttribute("errorMessage", error);
                session.removeAttribute("errorMessage");
            }

            req.getRequestDispatcher("/views/chat/chatting.jsp").forward(req, resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    
    // POST 영역: /roomMake 채팅방 생성
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
        
        // !! 개인 거래채팅방 개설
     // !! 개인 거래 채팅방 개설
        if ("/private".equals(path)) {
            System.out.println("/private 요청");

            HttpSession session = req.getSession();
            UserDTO loginUser = (UserDTO) session.getAttribute("userInfo");

            if (loginUser == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            long itemId = Long.parseLong(req.getParameter("itemId"));
            long hostId = Long.parseLong(req.getParameter("hostId"));

            ChattingService chatService = new ChattingService();

            // 기존 PRIVATE 채팅방 조회 or 생성
            long roomId = chatService.getOrCreatePrivateRoom(
                itemId, "PRIVATE", hostId, autoId
            );

            //  팝업으로 채팅방 이동
            resp.sendRedirect(
                req.getContextPath() + "/chat/room/" + roomId
            );
            return;
        }
        
        // !! 채팅 보내기 기능
        if ("/sendChat".equals(path)) {
            System.out.println("sendChat 요청");

            // multipart 환경에서도 roomId는 getParameter 가능
            String roomIdStr = req.getParameter("roomId");
            if (roomIdStr == null || roomIdStr.isBlank()) {
                throw new RuntimeException("roomId 파라미터 누락");
            }
            long roomId = Long.parseLong(roomIdStr);

            //  텍스트
            String content = req.getParameter("content");

            //  이미지 Part
            Part imagePart = null;
            try {
                imagePart = req.getPart("image"); // 없으면 null
            } catch (Exception ignore) {}

            //  업로드 경로 (※ chat 폴더 기준)
            String uploadPath = "C:/upload";

            // 서비스 단일 진입점 호출
            boolean result = service.sendChat(
                autoId,
                roomId,
                content,
                imagePart,
                uploadPath
            );

            resp.sendRedirect(req.getContextPath() + "/chat/room/" + roomId);
            return;
        }

        // !! 채팅방 삭제  //일단 냅두기로 관리자용도 있어도 되니까
        if (path.startsWith("/roomDelete/")) {
        	System.out.println("roomDelte 요청");
            String[] parts = path.split("/");   // ["", "roomDelete", "12"]
            if (parts.length != 3) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Room ID Missing");
                return;
            }

            Long roomId = Long.valueOf(parts[2]);
            service.deleteRoomById(autoId, roomId);

            resp.sendRedirect(req.getContextPath() + "/chat/roomList");
            return;
        }
        
        // !! 채팅방 나오기
        if (path.startsWith("/roomQuit/")) {
            System.out.println("roomQuit 요청");

            String[] parts = path.split("/");
            if (parts.length != 3) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Room ID Missing");
                return;
            }

            Long roomId = Long.valueOf(parts[2]);

            // ✅ 방 정보 조회(재활용)
            ChatRoomDTO roomDto = service.getRoomInfo(roomId);
            if (roomDto == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Room not found");
                return;
            }

            // ✅ 1) 우선 채팅방에서 나가기(공통)
            boolean chatQuit = service.quitRoomById(autoId, roomId);

            // ✅ 2) GROUP이면 모임에서도 나가기 (호스트는 삭제 유도 or 못 나가게 처리)
            if ("GROUP".equalsIgnoreCase(roomDto.getRoomType()) && roomDto.getMeetingId() != null) {

                // 호스트면 나가기 대신 “삭제” 유도하거나 막는 게 깔끔함
                if (autoId == roomDto.getHostId()) {
                    resp.setContentType("text/html; charset=UTF-8");
                    resp.getWriter().println("<script>alert('호스트는 방 나가기 대신 모임을 삭제하세요.');history.back();</script>");
                    return;
                }

                try {
                    new MeetingService().quitMeet(roomDto.getMeetingId(), autoId);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 모임 나가기는 실패했어도 채팅 나가기는 됐을 수 있으니 목록으로 보냄
                }
            }

            resp.sendRedirect(req.getContextPath() + "/chat/roomList");
            return;
        }

        if ("/invite".equals(path)) {
            System.out.println("/invite 요청");

            try {
            	System.out.println(req.getParameter("meetId"));
                long roomId = Long.parseLong(req.getParameter("roomId"));
                long meetId = Long.parseLong(req.getParameter("meetId"));
                long receiverId = Long.parseLong(req.getParameter("receiverId"));
                long hostId = autoId; // 로그인 유저가 초대 권한 있는 호스트

                boolean added = service.inviteUser(meetId, roomId, hostId, receiverId);

                if (added) {
                    req.setAttribute("successMsg", "유저 초대 완료");
                } else {
                    req.setAttribute("errorMsg", "유저 초대 실패");
                }

                // 초대 후 다시 채팅방 상세 페이지로
                resp.sendRedirect(req.getContextPath() + "/chat/room/" + roomId);

            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("errorMsg", e.getMessage());

                // 실패 시에도 채팅방 상세 페이지로 포워딩
                long roomId = Long.parseLong(req.getParameter("roomId"));
                resp.sendRedirect(req.getContextPath() + "/chat/room/" + roomId);
            }

            return;
        }
        if ("/kick".equals(path)) {
            System.out.println("/kick 요청");

            long roomId = Long.parseLong(req.getParameter("roomId"));
            long targetUserId = Long.parseLong(req.getParameter("targetUserId")); //강퇴 대상
            long meetingId = Long.parseLong(req.getParameter("meetId"));
            try {
                service.kickUser(autoId, roomId, targetUserId, meetingId);
                resp.sendRedirect(req.getContextPath() + "/chat/room/" + roomId);
            } catch (Exception e) {
                e.printStackTrace();
                // 메시지는 JSP에서 표시되도록
                req.getSession().setAttribute("errorMsg", e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/chat/room/" + roomId);
            }
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}

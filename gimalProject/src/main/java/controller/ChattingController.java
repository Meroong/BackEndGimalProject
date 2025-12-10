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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ChattingService;
import service.ImageService;
import service.MeetingService;
import service.PollService;
import service.UserService;
import util.AuthUtil;

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
                req.setAttribute("voteList", pollService.getPollListByRoom(roomId));
            }
            
            // 호스트 여부
            boolean isHost = (autoId == roomDto.getHostId());
            req.setAttribute("isHost", isHost);

            // 그룹일 경우 → 모임 전체 참여자 정보
            if (isHost && "GROUP".equalsIgnoreCase(roomDto.getRoomType())) {
                ArrayList<chatParticipantsUserDTO> users = service.getParticipantUsers(roomDto);
                req.setAttribute("participantUsers", users);
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
        if ("/pRoomMake".equals(path)) {
        	System.out.println("/pRoomMake 요청");
            long itemId = Long.parseLong(req.getParameter("itemId"));
            long hostId = Long.parseLong(req.getParameter("hostId"));
            long receiverId = Long.parseLong(req.getParameter("receiverId"));

            // 개인용 채팅방 생성
            boolean result = service.makePrivateRoom(itemId, "PRIVATE", hostId, receiverId);

            req.getRequestDispatcher("/views/chat/chatRoomList.jsp").forward(req, resp);
            return;
        }
        
        // !! 채팅 보내기 기능
        if("/sendChat".equals(path)) {
        	System.out.println("sendChat 요청");
            long roomId = Long.parseLong(req.getParameter("roomId"));
            String content = req.getParameter("content");
            
            service.chattingWithUserAndRoomId(autoId, roomId,content);
            
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
        	if(parts.length != 3) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Room ID Missing");
                return;
        	}
        	Long roomId = Long.valueOf(parts[2]);
        	boolean result =service.quitRoomById(autoId, roomId);
        	
        	// 추후 수정 result 활용해야함
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

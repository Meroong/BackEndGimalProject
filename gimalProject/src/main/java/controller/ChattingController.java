package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ChattingService;
import util.AuthUtil;
import dto.ChatMessageDTO;
import dto.ChatRoomDTO;
import dto.ResponseDTO;
import auth.JwtAuth;
import io.jsonwebtoken.Claims;

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
        
        if(autoId == -1) {
        	resp.sendRedirect("/views/user/login.jsp");
        	return;
        }

        // ---------- 채팅방 리스트 ----------
        if ("/roomList".equals(path)) {
        	System.out.println("roomList 요청");
            ArrayList<ChatRoomDTO> chatRooms = (ArrayList<ChatRoomDTO>) service.getRoomList(autoId).getData(); //리스폰스 디티오의 오브젝트를 변환
            req.setAttribute("chatList", chatRooms);
            req.getRequestDispatcher("/views/chat/chatRoomList.jsp").forward(req, resp);
            return;
        }
        
        // !!---------- 선택한 채팅방 메시지 ----------  
        else if (path != null && path.startsWith("/room/")) {
        	System.out.println("room/요청");
            String[] parts = path.split("/"); // ["", "room", "15"]
            if (parts.length != 3) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Room ID Missing");
                return;
            }

            Long roomId = Long.valueOf(parts[2]);

            // 로그인 유저가 이 방에 속하는지 체크          보안에 신경썼다  해커가 url로 접근해도 검증을 거치기 때문에 좀 더 안전하다.
            boolean allowed = service.checkUserInRoom(autoId, roomId);
            if (!allowed) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Not allowed");
                return;
            }

            // 채팅 메시지 조회
            ArrayList<ChatMessageDTO> messages = (ArrayList<ChatMessageDTO>) service.getMessage(roomId).getData();
            req.setAttribute("messages", messages);
            req.setAttribute("selectedRoomId", roomId);

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
        
        if(autoId == -1) {
        	resp.sendRedirect("/views/user/login.jsp");
        	return;
        }
        
        // !! 개인 거래채팅방 개설
        if ("/pRoomMake".equals(path)) {
        	System.out.println("/pRoomMake 요청");
            long itemId = Long.parseLong(req.getParameter("itemId"));
            long hostId = Long.parseLong(req.getParameter("hostId"));
            long receiverId = Long.parseLong(req.getParameter("receiverId"));

            // 개인용 채팅방 생성
            ResponseDTO response = service.makePrivateRoom(itemId, "PRIVATE", hostId, receiverId);

            // 성공/실패에 따라 리다이렉트 혹은 메시지 표시
            req.setAttribute("message", response.getMessage());
            req.getRequestDispatcher("/views/chat/chatRoomList.jsp").forward(req, resp);
            return;
        }
        // !! 모임용 채팅방 개설
        if ("/gRoomMake".equals(path)) {
        	System.out.println("gRoomMake 요청");
            long meetingId = Long.parseLong(req.getParameter("meetingId"));
            long hostId = Long.parseLong(req.getParameter("hostId"));

            
            ResponseDTO response = service.makeGroupRoom(meetingId, "Group", hostId);

            // 성공/실패에 따라 리다이렉트 혹은 메시지 표시
            req.setAttribute("message", response.getMessage());
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
        	service.quitRoomById(autoId, roomId);
        	
        	resp.sendRedirect(req.getContextPath() + "/chat/roomList");
        	return;
        }
        

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }


    public void destroy() {}
}

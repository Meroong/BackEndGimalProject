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
import dto.ChatRoomDTO;
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

        // ---------- 로그인 검증 ----------
        HttpSession session = req.getSession();
        String jwt = (String) session.getAttribute("Authorization");

        JwtAuth auth = new JwtAuth();
        Claims claims = auth.validateToken(jwt);

        if (claims == null) {
            req.setAttribute("error", "Invalid Token");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        long autoId = (Integer) claims.get("autoId");

        // ---------- 채팅방 리스트 ----------
        if ("/rooms".equals(path)) {

            ArrayList<ChatRoomDTO> chatRooms = service.getRoomList(autoId);
            req.setAttribute("chatList", chatRooms);
            req.getRequestDispatcher("/chat/chatRoomList.jsp").forward(req, resp);
            return;
        }

        // ---------- 채팅방 삭제 ----------
        else if (path.startsWith("/roomDelete/")) {

            String[] parts = path.split("/");   // ["", "roomDelete", "12"]
            if (parts.length != 3) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Room ID Missing");
                return;
            }

            Long roomId = Long.valueOf(parts[2]);
            service.deleteRoomById(autoId, roomId);

            resp.sendRedirect(req.getContextPath() + "/chat/rooms");
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    
    // POST 영역: /roomMake 채팅방 생성
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getPathInfo(); // /roomMake
        
        // ---------- 로그인 검증 ----------
        HttpSession session = req.getSession();
        String jwt = (String) session.getAttribute("Authorization");

        JwtAuth auth = new JwtAuth();
        Claims claims = auth.validateToken(jwt);

        if (claims == null) {
            req.setAttribute("error", "Invalid Token");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            return;
        }

        long autoId = (Integer) claims.get("autoId");

        if ("/roomMake".equals(path)) {

            String title = req.getParameter("title");
            long itemId = Long.parseLong(req.getParameter("itemId"));
            String roomType = req.getParameter("roomType");
            long hostId = Long.parseLong(req.getParameter("hostId"));

            service.makeRoom(title, itemId, roomType, hostId);
            
            resp.sendRedirect(req.getContextPath() + "/chat/rooms");
            return;
        }
        if("/sendChat".equals(path)) {
        	
            long roomId = Long.parseLong(req.getParameter("roomId"));
            String content = req.getParameter("content");
            
            service.chattingWithUserAndRoomId(autoId, roomId,content);
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }


    public void destroy() {}
}

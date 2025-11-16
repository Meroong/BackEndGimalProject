package service;

import java.net.http.HttpRequest;
import java.util.ArrayList;

import auth.JwtAuth;
import dao.ChatRoomDAO;
import dto.ChatRoomDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ChattingService {
	
	public ArrayList<ChatRoomDTO> getChattingRoom(HttpServletRequest request){
		ArrayList<ChatRoomDTO> chatList = new ArrayList<ChatRoomDTO>();
		HttpSession session = request.getSession();
		String jwt = (String)session.getAttribute("Authorization");
		JwtAuth auth = new JwtAuth();
		ChatRoomDAO dao = new ChatRoomDAO();
		
		//토큰 검증
		Claims claims = auth.validateToken(jwt);
		if(claims == null) {
			throw new RuntimeException("Invalid Token");
		}
		int autoId = (Integer) claims.get("autoId");
		chatList = dao.getChatRoomById(autoId); 
		
		//empty 로직체크는 프론트에서 
		return chatList;
	}

}

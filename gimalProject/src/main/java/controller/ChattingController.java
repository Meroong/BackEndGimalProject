package controller;

import java.io.IOException;

import dto.ResponseDTO;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ChattingService;


@WebServlet("/chat/*")
public class ChattingController extends HttpServlet {
	ChattingService service;


	
	public void init(ServletConfig config) throws ServletException {
        service  = new ChattingService();
        System.out.println("ChattingController: ON");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        ResponseDTO result = null;
        switch (path) {
        case "/rooms": //채팅방 조회
       
            result = service.loginUser(id, pw, session);
            break;

        case "/register":
    	    String userId = req.getParameter("userId");
    	    String password = req.getParameter("userPassword");
    	    String nickName = req.getParameter("nickName");
    	    String userName = req.getParameter("userName"); // 회원 이름
    	    String addressIdStr = req.getParameter("addressId"); // 문자열로 받아옴
    	    String addressDetail = req.getParameter("addressDetail");
        	
            result = service.
            break;

        default:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
    }

    resp.setContentType("application/json; charset=UTF-8");

    // Gson으로 JSON 출력 (수동 문자열 X)
    String json = new Gson().toJson(result);
    resp .getWriter().write(json);
}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}


	public void destroy() {
		
	}

}


package controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.UserService;

import java.io.IOException;

import dto.ResponseDTO;
@WebServlet("/user/*")
public class UserController extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = new UserService();
        System.out.println("userController: ON");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String path = req.getPathInfo();
        ResponseDTO result = null;

        switch (path) {
            case "/login":
                String id = req.getParameter("userId");
                String pw = req.getParameter("userPassword");
                HttpSession session = req.getSession();
                result = userService.loginUser(id, pw, session);
                break;

            case "/register":
        	    String userId = req.getParameter("userId");
        	    String password = req.getParameter("userPassword");
        	    String nickName = req.getParameter("nickName");
        	    String userName = req.getParameter("userName"); // 회원 이름
        	    String addressIdStr = req.getParameter("addressId"); // 문자열로 받아옴
        	    String addressDetail = req.getParameter("addressDetail");
            	
                result = userService.registerUser(userId, password, nickName, userName, addressIdStr, addressDetail); // 필요하면 register도 request 없애는 게 좋음
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
        }

        resp.setContentType("application/json; charset=UTF-8");

        // Gson으로 JSON 출력 (수동 문자열 X)
        String json = new Gson().toJson(result);
        resp.getWriter().write(json);
    }



	public void destroy() {
		
	}

}

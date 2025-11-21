package controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.UserService;
import dto.ResponseDTO;
import com.google.gson.Gson;

import java.io.IOException;

@WebServlet("/user/*") //    /user/이하로 들어오는 모든 url 요청을 처리 /user는 딱 /user만 가능하게 함
public class UserController extends HttpServlet {
    private UserService userService;
    private Gson gson = new Gson();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); 
        userService = new UserService();
        System.out.println("userController: ON");
    }

    	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        
        //로그아웃 요청을 get요청으로 처리 
        if ("/logout".equals(path)) {
        	
        	//유저 서비스내에 로그아웃 로직처리 인자는 세션
            userService.logoutUser(req.getSession());
            
            //로그아웃 서비스 처리 후 index.jsp 페이지로 리다이렉트  
            resp.sendRedirect(req.getContextPath() + "/index.jsp"); // 로그아웃 후 메인으로 이동
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
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

                if(result.isSuccess()) {
                    // 로그인 성공 시 index.jsp로 redirect
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                    return;
                } 
                break;

            case "/register":
                String userId = req.getParameter("userId");
                String password = req.getParameter("userPassword");
                String nickName = req.getParameter("nickName");
                String userName = req.getParameter("userName");
                String addressIdStr = req.getParameter("addressId");
                String addressDetail = req.getParameter("addressDetail");

                result = userService.registerUser(userId, password, nickName, userName, addressIdStr, addressDetail);
                break;

            case "/update":
                // 서비스에 맞춰 DTO 만들어서 updateUser 호출
                // 예: UserDTO dto = new UserDTO();
                // result = userService.updateUser(dto);
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
        }

        // JSON 응답 처리
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }
}

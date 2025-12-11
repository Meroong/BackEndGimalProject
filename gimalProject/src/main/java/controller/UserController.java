package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ImageService;
import service.UserService;
import util.AuthUtil;
import dto.UserAddressDTO;
import dto.UserDTO;
import auth.JwtAuth;

import java.io.IOException;

@WebServlet("/user/*")
public class UserController extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = new UserService();
        System.out.println("userController: ON");
    }

    // =============================
    // GET 요청 처리
    // =============================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getPathInfo();

        if ("/logout".equals(path)) {
            
        	Long autoId = AuthUtil.getAutoId(req);
            
            if(autoId == -1) {
            	resp.sendRedirect(req.getContextPath() + "/views/user/login.jsp");
            	return;
            }
            HttpSession session = req.getSession(false);
            if (session != null) {
                userService.logoutUser(session);
            }
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    // =============================
    // POST 요청 처리
    // =============================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = req.getPathInfo();

        switch (path) {

        /* ==========================================================
         * 로그인
         * ========================================================== */
        case "/login":
            String id = req.getParameter("userId");
            String pw = req.getParameter("userPassword");
            HttpSession session = req.getSession();


            UserDTO userDto = userService.loginUser(id, pw);

            if (userDto != null) {
                UserAddressDTO addressDto = userService.getAddressInfo(userDto.getAutoId());

                UserDTO sessionUser = new UserDTO();
                sessionUser.setUserId(userDto.getUserId());
                sessionUser.setUserName(userDto.getUserName());
                sessionUser.setNickname(userDto.getNickname());
                session.setAttribute("userInfo", sessionUser);
                session.setAttribute("addressInfo", addressDto);

                String profileUrl = new ImageService().getProfileImage(userDto.getAutoId(), "PROFILE");
                session.setAttribute("profileUrl", profileUrl);
                String jwt = JwtAuth.generateToken(userDto.getUserId(), userDto.getAutoId(), userDto.getRole());
                session.setAttribute("Authorization", "Bearer " + jwt);

             // 이전 URL 체크 후 리다이렉트
                String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
                if (redirectUrl != null) {
                    session.removeAttribute("redirectAfterLogin"); // 한 번만 사용
                    resp.sendRedirect(redirectUrl);
                } else {
                	resp.sendRedirect(req.getContextPath() + "/home");
                }

                return;

            } else {
                req.setAttribute("errorMsg", "아이디 또는 비밀번호를 확인해주세요.");
                req.getRequestDispatcher("/views/user/login.jsp").forward(req, resp);
                return;
            }

        /* ==========================================================
         * 회원가입 (예외 기반)
         * ========================================================== */
        case "/register":
            String userId = req.getParameter("userId");
            String password = req.getParameter("userPassword");
            String nickName = req.getParameter("nickName");
            String userName = req.getParameter("userName");
            String roadAddress = req.getParameter("roadAddress");
            String jibunAddress = req.getParameter("jibunAddress");
            String addrDetail = req.getParameter("addressDetail");

            try {
                userService.registerUser(userId, password, nickName, userName,
                        roadAddress, jibunAddress, addrDetail);
                resp.sendRedirect(req.getContextPath() + "/home");
                return;

            }catch (Exception e) {
                req.setAttribute("errorMsg", e.getMessage());
                req.setAttribute("userId", userId);
                req.setAttribute("userName", userName);
                req.setAttribute("nickName", nickName);
                req.setAttribute("roadAddress", roadAddress);
                req.setAttribute("jibunAddress", jibunAddress);
                req.setAttribute("addrDetail", addrDetail);

                req.getRequestDispatcher("/views/user/register.jsp").forward(req, resp);
                return;
            }


        /* ==========================================================
         * 회원 정보 수정 (예외 기반)
         * ========================================================== */
        case "/update":
        	Long updateAutoId = AuthUtil.getAutoId(req);

        	if (updateAutoId == -1) {
        	    HttpSession updateSession = req.getSession();

        	    // 현재 요청 URL + 쿼리스트링 조회
        	    String currentUrl = req.getRequestURI();
        	    String queryString = req.getQueryString();
        	    if (queryString != null && !queryString.isEmpty()) {
        	        currentUrl += "?" + queryString;
        	    }

        	    // 세션에 저장
        	    updateSession.setAttribute("redirectAfterLogin", currentUrl);

        	    // 로그인 페이지로 이동
        	    resp.sendRedirect(req.getContextPath() + "/views/user/login.jsp");
        	    return;
        	}

            String newPassword = req.getParameter("newPassword");
            String newNickname = req.getParameter("newNickname");
            String newRoadAddress = req.getParameter("roadAddress");
            String newJibunAddress = req.getParameter("jibunAddress");
            String newAddrDetail = req.getParameter("addrDetail");
            String newLatitude = req.getParameter("latitude");
            String newLongitude = req.getParameter("longitude");

            try {
                userService.updateUser(updateAutoId, newPassword, newNickname,
                        newRoadAddress, newJibunAddress, newAddrDetail,
                        newLatitude, newLongitude);

                // 세션 갱신
                UserDTO updatedUser = userService.getUserInfo(updateAutoId);
                UserAddressDTO updatedAddress = userService.getAddressInfo(updateAutoId);
                req.getSession().setAttribute("userInfo", updatedUser);
                req.getSession().setAttribute("addressInfo", updatedAddress);

                resp.sendRedirect(req.getContextPath() + "/views/user/mypage.jsp");
                return;

            } catch (Exception e) {
                req.setAttribute("errorMsg", e.getMessage());
                req.getRequestDispatcher("/views/user/mypage.jsp").forward(req, resp);
                return;
            }

        /* ==========================================================
         * 회원 탈퇴 (예외 기반)
         * ========================================================== */
        case "/delete":
        	Long delAutoId = AuthUtil.getAutoId(req);
        	if (delAutoId == -1) {
        	    HttpSession deleteSession = req.getSession();

        	    // 현재 요청 URL + 쿼리스트링 조회
        	    String currentUrl = req.getRequestURI();
        	    String queryString = req.getQueryString();
        	    if (queryString != null && !queryString.isEmpty()) {
        	        currentUrl += "?" + queryString;
        	    }

        	    // 세션에 저장
        	    deleteSession.setAttribute("redirectAfterLogin", currentUrl);

        	    // 로그인 페이지로 이동
        	    resp.sendRedirect(req.getContextPath() + "/views/user/login.jsp");
        	    return;
        	}

            try {
                userService.deleteUser(delAutoId);
                req.getSession().invalidate();
                resp.sendRedirect(req.getContextPath() + "/home");
                return;

            } catch (Exception e) {
                req.setAttribute("errorMsg", e.getMessage());
                req.getRequestDispatcher("/views/user/mypage.jsp").forward(req, resp);
                return;
            }

        default:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

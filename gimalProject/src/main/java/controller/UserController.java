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
import dto.UserDTO;
import com.google.gson.Gson;
import auth.JwtAuth;

import java.io.IOException;

@WebServlet("/user/*")
public class UserController extends HttpServlet {

    private UserService userService;
    private Gson gson = new Gson();

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
            // 로그아웃 처리
            HttpSession session = req.getSession(false);
            if (session != null) {
                userService.logoutUser(session);
            }
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
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
        ResponseDTO result = null;

        switch (path) {
            // -----------------------------
            // 로그인
            // -----------------------------
            case "/login":
                String id = req.getParameter("userId");
                String pw = req.getParameter("userPassword");
                HttpSession session = req.getSession();

                // 서비스에서 DTO 반환
                UserDTO dto = userService.loginUser(id, pw);

                if (dto != null) {
                    // 화면용 세션 저장
                    UserDTO sessionUser = new UserDTO();
                    sessionUser.setAutoId(dto.getAutoId());
                    sessionUser.setUserName(dto.getUserName());
                    sessionUser.setNickname(dto.getNickname());
                    sessionUser.setAddressId(dto.getAddressId());
                    sessionUser.setAddressDetail(dto.getAddressDetail());
                    session.setAttribute("userInfo", sessionUser);

                    // JWT 생성
                    String jwt = JwtAuth.generateToken(dto.getUserId(), dto.getAutoId(), dto.getRole());
                    session.setAttribute("Authorization", "Bearer " + jwt);

                    // 로그인 성공 후 메인 페이지 이동
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                    return;
                } else {
                    req.setAttribute("errorMsg", "아이디 또는 비밀번호를 확인해주세요.");
                    req.getRequestDispatcher("/views/user/login.jsp").forward(req, resp);
                    return;
                }

            // -----------------------------
            // 회원가입
            // -----------------------------
            case "/register":
                String userId = req.getParameter("userId");
                String password = req.getParameter("userPassword");
                String nickName = req.getParameter("nickName");
                String userName = req.getParameter("userName");
                String addressIdStr = req.getParameter("addressId");
                String addressDetail = req.getParameter("addressDetail");

                result = userService.registerUser(userId, password, nickName, userName, addressIdStr, addressDetail);
                break;

            // -----------------------------
            // 회원 정보 수정
            // -----------------------------
            case "/update":
                long autoId = Long.parseLong(req.getParameter("autoId"));
                String newPassword = req.getParameter("newPassword");
                String newNickname = req.getParameter("newNickname");
                String addrIdStr = req.getParameter("addressId");
                String addrDetail = req.getParameter("addressDetail");

                result = userService.updateUser(autoId, newPassword, newNickname, addrIdStr, addrDetail);

                if (result.isSuccess()) {
                    // 세션 갱신
                    UserDTO updatedUser = userService.getMyInfo((int) autoId);
                    req.getSession().setAttribute("userInfo", updatedUser);

                    // 수정 성공 시 마이페이지로 리다이렉트
                    resp.sendRedirect(req.getContextPath() + "/views/user/mypage.jsp");
                    return;
                } else {
                    req.setAttribute("errorMsg", result.getMessage());
                    req.getRequestDispatcher("/views/user/mypage.jsp").forward(req, resp);
                    return;
                }

            // -----------------------------
            // 회원 탈퇴
            // -----------------------------
            case "/delete":
                long delAutoId = Long.parseLong(req.getParameter("autoId"));
                boolean deleteResult = userService.deleteUser((int) delAutoId);

                result = deleteResult
                        ? new ResponseDTO(true, "회원 탈퇴 성공")
                        : new ResponseDTO(false, "회원 탈퇴 실패");

                if (deleteResult) {
                    req.getSession().invalidate();
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                    return;
                }
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

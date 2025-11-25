package controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.UserService;
import util.AuthUtil;
import dto.ResponseDTO;
import dto.UserAddressDTO;
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
                UserDTO userDto = userService.loginUser(id, pw);
                
                if (userDto != null) {
                	UserAddressDTO addressDto = userService.getAddressInfo(userDto.getAutoId());
                	//데이터 확인용 syso
                	System.out.println("login반환데이터:"+addressDto);
                	
                    // 화면용 세션 저장
                    UserDTO sessionUser = new UserDTO();
                    sessionUser.setUserId(userDto.getUserId());
                    sessionUser.setUserName(userDto.getUserName());
                    sessionUser.setNickname(userDto.getNickname());
                    session.setAttribute("userInfo", sessionUser);
                    session.setAttribute("addressInfo", addressDto);

                    // JWT 생성
                    String jwt = JwtAuth.generateToken(userDto.getUserId(), userDto.getAutoId(), userDto.getRole());
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
                String roadAddress = req.getParameter("roadAddress");
                String jibunAddress = req.getParameter("jibunAddress");
                String addrDetail = req.getParameter("addressDetail");
                //추후 확장 예정
				/*
				 * String latitudeStr = req.getParameter("latitude"); String longitudeStr =
				 * req.getParameter("longitude");
				 */

                result = userService.registerUser(userId, password, nickName, userName,
						roadAddress, jibunAddress, addrDetail/*
																 * , latitudeStr, longitudeStr
																 */
						);
                break;

            // -----------------------------
            // 회원 정보 수정
            // -----------------------------
            case "/update":
                long autoId = AuthUtil.getAutoId(req); //이 방식으로 수정 예정(손주성)
                String newPassword = req.getParameter("newPassword");
                String newNickname = req.getParameter("newNickname");
                String newRoadAddress = req.getParameter("roadAddress");
                String newJibunAddress = req.getParameter("jibunAddress");
                String newAddrDetail = req.getParameter("addrDetail");
				/*//추후 확장예정
														 * String newLatitude = req.getParameter("latitude"); String
														 * newLongitude = req.getParameter("longitude");
														 */

                result = userService.updateUser(
                        autoId, newPassword, newNickname,
						newRoadAddress, newJibunAddress, newAddrDetail/*
																		 * , newLatitude, newLongitude
																		 */
                        );
                if (result.isSuccess()) {
                    // 세션 갱신
                    UserDTO updatedUser = userService.getMyInfo(autoId);
                    UserAddressDTO updatedAddress = userService.getAddressInfo(autoId);
                    req.getSession().setAttribute("userInfo", updatedUser);
                    req.getSession().setAttribute("addressInfo", updatedAddress);

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
                long delAutoId = AuthUtil.getAutoId(req);
                boolean deleteResult = userService.deleteUser(delAutoId);

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

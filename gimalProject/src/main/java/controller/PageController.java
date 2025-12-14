package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/page/*")
public class PageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {

            // 🔐 로그인
            case "/login":
                forward(req, resp, "/WEB-INF/views/user/login.jsp");
                break;

            // 📝 회원가입
            case "/register":
                forward(req, resp, "/WEB-INF/views/user/register.jsp");
                break;

            // 👤 마이페이지 (로그인 필요)
            case "/mypage":
                if (!isLogin(req)) {
                    resp.sendRedirect(req.getContextPath() + "/page/login");
                    return;
                }
                forward(req, resp, "/WEB-INF/views/user/myPage.jsp");
                break;

            // 🧩 모임 생성
            case "/meet/form":
                if (!isLogin(req)) {
                    resp.sendRedirect(req.getContextPath() + "/page/login");
                    return;
                }
                forward(req, resp, "/WEB-INF/views/meet/meetForm.jsp");
                break;

            // 🧩 모임 수정
            case "/meet/update":
                if (!isLogin(req)) {
                    resp.sendRedirect(req.getContextPath() + "/page/login");
                    return;
                }
                forward(req, resp, "/WEB-INF/views/meet/meetUpdate.jsp");
                break;
            case "/address-return":
                forward(req, resp, "/WEB-INF/views/util/addressPopupReturn.jsp");
                break;
                
            case "/wallet":
                if (!isLogin(req)) {
                    resp.sendRedirect(req.getContextPath() + "/page/login");
                    return;
                }
                forward(req, resp, "/WEB-INF/views/wallet/wallet_page.jsp");
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws ServletException, IOException {
        req.getRequestDispatcher(jsp).forward(req, resp);
    }

    private boolean isLogin(HttpServletRequest req) {
        return req.getSession().getAttribute("userInfo") != null;
    }
}


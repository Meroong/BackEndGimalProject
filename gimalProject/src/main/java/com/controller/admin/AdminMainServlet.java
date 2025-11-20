package com.controller.admin;

import auth.JwtAuth;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
//어드민만 접근 가능 일반 유저 메인으로 튕김
@WebServlet("/admin")
public class AdminMainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String authHeader = (session != null)
                ? (String) session.getAttribute("Authorization")
                : null;

        // 1) 로그인 안 되어 있으면 로그인 페이지로
        if (authHeader == null) {
            response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
            return;
        }

        // 2) JWT 에서 role 꺼내서 ADMIN인지 확인
        String role = JwtAuth.getRole(authHeader);
        if (!"ADMIN".equals(role)) {
            // 관리자 아니면 메인으로 돌려보내기
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 3) 관리자일 때만 관리자 메인 JSP로 포워딩
        request.getRequestDispatcher("/WEB-INF/views/admin/adminMain.jsp")
               .forward(request, response);
    }
}

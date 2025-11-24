package com.controller.admin;

import java.io.IOException;

import dto.AdminStatsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminStatsService;

@WebServlet("/admin/stats")
public class AdminStatsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AdminStatsService statsService = new AdminStatsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 통계 데이터 한 번에 조회
        AdminStatsDTO stats = statsService.getStats();

        // JSP에서 사용할 이름으로 전달
        request.setAttribute("stats", stats);

        // 통계 JSP로 포워드
        request.getRequestDispatcher("/WEB-INF/views/admin/stats.jsp")
               .forward(request, response);
    }
}

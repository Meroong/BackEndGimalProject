package com.controller.admin;

import java.io.IOException;
import java.util.List;

import dto.ReportDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminReportService;

@WebServlet("/admin/reports")
public class AdminReportListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AdminReportService adminReportService = new AdminReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 서비스에서 실제 신고 목록 가져오기
        List<ReportDTO> reportList = adminReportService.getReportList();

        // JSP에서 쓸 이름으로 세팅
        request.setAttribute("reportList", reportList);

        // 신고 목록 JSP로 포워드
        request.getRequestDispatcher("/WEB-INF/views/admin/reportList.jsp")
               .forward(request, response);
    }
}

package com.controller.admin;

import java.io.IOException;

import dto.ReportDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminReportService;

@WebServlet("/admin/reports/detail")
public class AdminReportDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AdminReportService adminReportService = new AdminReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id = Long.parseLong(request.getParameter("id"));

        ReportDTO report = adminReportService.getReportById(id);

        request.setAttribute("report", report);

        request.getRequestDispatcher("/WEB-INF/views/admin/reportDetail.jsp")
               .forward(request, response);
    }
}

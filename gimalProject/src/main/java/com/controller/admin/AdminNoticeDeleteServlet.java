package com.controller.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminNoticeService;

@WebServlet("/admin/notices/delete")
public class AdminNoticeDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AdminNoticeService noticeService = new AdminNoticeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id = Long.parseLong(request.getParameter("id"));

        boolean success = noticeService.deleteNotice(id);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/notices");
        } else {
            response.sendError(500, "삭제 실패");
        }
    }
}

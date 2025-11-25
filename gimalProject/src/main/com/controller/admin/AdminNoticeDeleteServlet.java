package com.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import service.AdminNoticeService;

@WebServlet("/admin/notices/delete")
public class AdminNoticeDeleteServlet extends HttpServlet {

    private AdminNoticeService noticeService = new AdminNoticeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/admin/notices");
            return;
        }

        long id = Long.parseLong(idParam);

        noticeService.deleteNotice(id);

        response.sendRedirect(request.getContextPath() + "/admin/notices");
    }
}

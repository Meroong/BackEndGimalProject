package com.controller.admin;

import java.io.IOException;
import java.util.List;

import dto.AdminNoticeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminNoticeService;

@WebServlet("/admin/notices")
public class AdminNoticeListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AdminNoticeService adminNoticeService = new AdminNoticeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<AdminNoticeDTO> noticeList = adminNoticeService.getNoticeList();

        request.setAttribute("noticeList", noticeList);

        request.getRequestDispatcher("/WEB-INF/views/admin/noticeList.jsp")
               .forward(request, response);
    }
}

package com.controller.admin;

import java.io.IOException;

import dto.AdminNoticeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminNoticeService;

@WebServlet("/admin/notices/edit")
public class AdminNoticeEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AdminNoticeService noticeService = new AdminNoticeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id = Long.parseLong(request.getParameter("id"));

        // 기존 내용 조회
        AdminNoticeDTO dto = noticeService.getNoticeList()
                .stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .orElse(null);

        request.setAttribute("notice", dto);
        request.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        AdminNoticeDTO dto = new AdminNoticeDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setContent(content);

        boolean success = noticeService.updateNotice(dto);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/notices");
        } else {
            request.setAttribute("error", "수정 실패");
            request.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp")
                   .forward(request, response);
        }
    }
}

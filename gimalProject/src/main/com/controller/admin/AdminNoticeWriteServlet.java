package com.controller.admin;

import java.io.IOException;

import dto.AdminNoticeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminNoticeService;

@WebServlet("/admin/notices/write")
public class AdminNoticeWriteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AdminNoticeService adminNoticeService = new AdminNoticeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 공지 작성 폼으로 이동
        request.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        // 일단 writer는 하드코딩 또는 나중에 세션에서 관리자 정보 가져오도록 수정 가능
        String writer = "ADMIN";

        AdminNoticeDTO dto = new AdminNoticeDTO();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setWriter(writer);

        boolean success = adminNoticeService.writeNotice(dto);

        if (success) {
            // 등록 성공하면 목록으로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/admin/notices");
        } else {
            // 실패 시 다시 폼으로 보내거나 에러 메시지 표시
            request.setAttribute("error", "공지 등록에 실패했습니다.");
            request.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp")
                   .forward(request, response);
        }
    }
}

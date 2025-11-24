package com.controller.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminUserService;

@WebServlet("/admin/users/unblock")
public class AdminUserUnblockServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AdminUserService service = new AdminUserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id = Long.parseLong(request.getParameter("id"));

        service.unblockUser(id);

        // 다시 상세보기 화면으로
        response.sendRedirect(request.getContextPath() + "/admin/users/detail?id=" + id);
    }
}

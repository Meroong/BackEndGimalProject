package com.controller.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.AdminUserService;


@WebServlet("/admin/users/delete")
public class AdminUserDeleteServlet extends HttpServlet {

    private AdminUserService service = new AdminUserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long autoId = Long.parseLong(request.getParameter("id"));

        int result = service.deleteUser(autoId);

        if (result > 0) {
            System.out.println("삭제 완료");
        } else {
            System.out.println("삭제 실패");
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}

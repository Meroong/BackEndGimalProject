package com.controller.admin;

import java.io.IOException;

import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminUserService;

@WebServlet("/admin/users/detail")
public class AdminUserDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AdminUserService adminUserService = new AdminUserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // URL에서 id 가져오기
        long id = Long.parseLong(request.getParameter("id"));

        // 서비스에서 회원정보 가져오기
        UserDTO user = adminUserService.getUserById(id);

        request.setAttribute("user", user);

        // JSP로 이동
        request.getRequestDispatcher("/WEB-INF/views/admin/userDetail.jsp")
               .forward(request, response);
    }
}

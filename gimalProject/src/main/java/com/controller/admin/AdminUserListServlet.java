package com.controller.admin;

import java.io.IOException;
import java.util.List;

import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminService;

@WebServlet("/admin/users")
public class AdminUserListServlet extends HttpServlet {

    private AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1) 전체 회원 리스트 가져오기
        List<UserDTO> userList = adminService.getAllUsers();

        // 2) JSP로 보낼 준비
        request.setAttribute("userList", userList);

        // 3) 관리자용 회원 목록 JSP로 포워딩
        request.getRequestDispatcher("/WEB-INF/views/admin/userList.jsp")
               .forward(request, response);
    }
}

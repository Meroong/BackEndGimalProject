package com.controller.admin;

import java.io.IOException;
import java.util.List;

import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminUserService;

@WebServlet("/admin/users")
public class AdminUserListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AdminUserService adminUserService = new AdminUserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 서비스에서 회원 목록 가져오기
        List<UserDTO> userList = adminUserService.getUserList();

        // JSP에서 그대로 쓰도록 세팅
        request.setAttribute("userList", userList);

        // 원래 있던 userList.jsp로 포워드 (경로만 정확하면 OK)
        request.getRequestDispatcher("/WEB-INF/views/admin/userList.jsp")
               .forward(request, response);
    }
}

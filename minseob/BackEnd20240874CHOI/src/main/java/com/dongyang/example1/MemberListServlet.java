package com.dongyang.example1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/list.do")
public class MemberListServlet extends HttpServlet {
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberDAO dao  =new MemberDAO();
		ArrayList<MemberDTO> mList = dao.selectmemberList();
		request.setAttribute("memList", mList);
		
		request.setAttribute("name", "최민섭");
		HttpSession session = request.getSession();
		session.setAttribute("loginCheck", "ok");
		
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("memberList2.jsp");
		dispatcher.forward(request, response);
	}

}

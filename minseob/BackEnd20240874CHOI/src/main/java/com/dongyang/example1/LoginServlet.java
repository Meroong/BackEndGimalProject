package com.dongyang.example1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
		System.out.println("init 메서드 호출!");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 파라미터 받음
		String id = request.getParameter("id");
		String password = request.getParameter("pw");
		//2. JDBC연동
		
		MemberDAO mdao = new MemberDAO();
		MemberDTO mdto = new MemberDTO();
		
		mdto.setMemberid(id);
		mdto.setPassword(password);
		
		mdao.loginCheck(mdto);
		
	//	if(id.equals("dong")&&password.equals("123")) {
			//로그인 성공
			//HttpSession session = request.getSession();
			//session.setAttribute("sn", "동양미래대");
			//session.setAttribute("name", "김동양");
			//response.sendRedirect("loginOk.jsp"); // 호출시 바로 이동
			
			ServletContext application = request.getServletContext();
			application.setAttribute("logCheck", "ok" );
			
	//	} else {
			//로그인 실패
			//request.setAttribute("name", "최민섭");
			//RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp"); // 바로 이동X forward 메소드 호출시 이동
			//dispatcher.forward(request, response);
	//	}
		response.sendRedirect("index.jsp");
		//3. 응답할 문서 응답
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

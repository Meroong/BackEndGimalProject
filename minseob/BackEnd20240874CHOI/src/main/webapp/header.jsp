<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<h3> 메뉴 : 홈<a href="intro.jsp">대학소개 </a> 
<% 
	String lo = (String) application.getAttribute("logCheck");
	if(lo != null){
		out.println("<a href=#> 마이페이지 </a>");
	}
	else{
		out.println("<a href=index.jsp> 회원가입 </a>");
	}


%>
	<a href="list.do"> 회원목록 </a>
	
</h3>
<a href="loginForm.jsp">로그인</a>
<%@page import="com.dongyang.example1.MemberDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> 회원목록 </title>
</head>
<body>
<%@ include file = "header.jsp" %>
<% 
	//java.util.ArrayList
	ArrayList<MemberDTO> aList = (ArrayList<MemberDTO>) request.getAttribute("memList");
	if(aList == null){
		response.sendRedirect("index.jsp");
	}
	else if(aList.isEmpty()){
		
	}
	else{
%>

<h1> 회원목록 </h1>
<table border=1>
	<tr>
		<th> 아이디 </th> <th>암호</th> <th>이름</th><th>이메일</th>
	</tr>
<%
	for( MemberDTO dto: aList ){
%>
		<tr>
			<td><%= dto.getMemberid() %></td>
			<td><%= dto.getPassword() %></td>
			<td><%= dto.getName() %></td>
			<td><%= dto.getEmail() %></td>
		</tr>
<%
		
	}
%>

</table>

<%
}
%>
</body>
</html>
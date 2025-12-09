<%@page import="com.dongyang.example1.MemberDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="errorMessage.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> 회원목록 </title>
</head>
<body>
<%@ include file = "header.jsp" %>

<% Integer.parseInt("aaa"); %>

<h1> 이름: ${name}  , 로그인 여부: ${loginCheck} </h1>





<h1> 회원목록 </h1>


<table border=1>
	<tr>
		<th> 아이디 </th> <th>암호</th> <th>이름</th><th>이메일</th>
	</tr>
	<c:forEach	items="${memList}" var="dto">
		<tr>
			<td> ${dto.memberid} </td>
			 <td>${dto.password}</td> <td>${dto.name}</td><td>${dto.email}</td>
		</tr>
	</c:forEach>
</table>

</body>
</html>
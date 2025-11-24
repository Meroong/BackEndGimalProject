<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.UserDTO" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 회원 관리</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/home.css">

</head>
<body>
<div class="container">

    <%-- 상단 제목 --%>
    <header>
        <div class="logo">
            <img src="<%=request.getContextPath()%>/resources/images/logo.png" alt="logo">
            도란도란 관리자
        </div>
        <div class="header-buttons">
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin'">
                관리자 메인
            </button>
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/'">
                메인으로
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">회원 관리</div>

        <p style="margin-bottom: 12px; color:#555;">
            도란도란에 가입된 회원 목록입니다. (나중에 검색·권한변경 기능 붙일 예정)
        </p>

        <table border="1" style="width:100%; border-collapse:collapse; text-align:center;">
            <thead>
            <tr style="background:#f3f4f6;">
                <th>번호(autoId)</th>
                <th>아이디</th>
                <th>이름</th>
                <th>닉네임</th>
                <th>권한</th>
                <th>신뢰도</th>
                <th>가입일</th>
                <th>상세</th>
            </tr>
            </thead>
            <tbody>
            <%
                // 나중에 서블릿에서 "userList"라는 이름으로 List<UserDTO>를 넣어줄 거예요.
                List<UserDTO> userList = (List<UserDTO>) request.getAttribute("userList");

                if (userList == null || userList.isEmpty()) {
            %>
                <tr>
                    <td colspan="7">조회된 회원이 없습니다.</td>
                </tr>
            <%
                } else {
                    for (UserDTO u : userList) {
            %>
                <tr>
                    <td><%= u.getAutoId() %></td>
                    <td><%= u.getUserId() %></td>
                    <td><%= u.getUserName() %></td>
                    <td><%= u.getNickname() %></td>
                    <td><%= u.getRole() %></td>
                    <td><%= u.getTrustScore() %></td>
                    <td><%= u.getCreatedAt() %></td>
                    <td>
    				<a href="<%=request.getContextPath()%>/admin/users/detail?id=<%= u.getAutoId() %>">
        				상세보기
   				    </a>
					</td>
                    
                </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </section>
</div>
</body>
</html>

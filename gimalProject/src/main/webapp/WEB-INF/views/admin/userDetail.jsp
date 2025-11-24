<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dto.UserDTO" %>

<%
    UserDTO u = (UserDTO) request.getAttribute("user");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 회원 상세보기</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/admin.css">


</head>
<body>
<div class="container">

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
                    onclick="location.href='<%=request.getContextPath()%>/admin/users'">
                회원 목록
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">회원 상세정보</div>

        <table border="1" style="width:100%; border-collapse:collapse;">
            <tr><th>회원번호(autoId)</th><td><%= u.getAutoId() %></td></tr>
            <tr><th>아이디(userId)</th><td><%= u.getUserId() %></td></tr>
            <tr><th>이름(userName)</th><td><%= u.getUserName() %></td></tr>
            <tr><th>닉네임(nickname)</th><td><%= u.getNickname() %></td></tr>
            <tr><th>권한(role)</th><td><%= u.getRole() %></td></tr>
            <tr><th>주소 ID(addressId)</th><td><%= u.getAddressId() %></td></tr>
            <tr><th>상세주소(addressDetail)</th><td><%= u.getAddressDetail() %></td></tr>
            <tr><th>신뢰도(trustScore)</th><td><%= u.getTrustScore() %></td></tr>
            <tr><th>가입일(createdAt)</th><td><%= u.getCreatedAt() %></td></tr>
            <tr><th>수정일(updatedAt)</th><td><%= u.getUpdatedAt() %></td></tr>
        </table>

        <br>

        <%-- 관리자 계정(ADMIN)은 정지/해제 버튼 숨김 --%>
        <% if (!"ADMIN".equals(u.getRole())) { %>

            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin/users/block?id=<%= u.getAutoId() %>'">
                계정 정지
            </button>

            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin/users/unblock?id=<%= u.getAutoId() %>'">
                정지 해제
            </button>
			<button class="log-btn"
       			 onclick="location.href='<%=request.getContextPath()%>/admin/users/delete?id=<%= u.getAutoId() %>'"
       			 style="background:#ff4d4d; color:white;">
   				 회원 탈퇴(삭제)
			</button>

            <br><br>
        <% } %>

        <button class="log-btn"
                onclick="location.href='<%=request.getContextPath()%>/admin/users'">
            목록으로
        </button>

    </section>

</div>
</body>
</html>

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

    <%-- 상단 헤더 --%>
    <header>
        <h1>회원 상세정보</h1>

        <div class="header-buttons">
        <button class="log-btn" onclick="location.href='<%=request.getContextPath()%>/'">홈으로</button>
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
        <div class="box-title">회원 기본정보</div>

        <%
            if (u == null) {
        %>
            <p>회원 정보를 찾을 수 없습니다.</p>
        <%
            } else {
        %>

        <table class="admin-table">
            <tr>
                <th>회원 PK(autoId)</th>
                <td><%= u.getAutoId() %></td>
            </tr>
            <tr>
                <th>아이디(userId)</th>
                <td><%= u.getUserId() %></td>
            </tr>
            <tr>
                <th>이름(userName)</th>
                <td><%= u.getUserName() %></td>
            </tr>
            <tr>
                <th>닉네임(nickname)</th>
                <td><%= u.getNickname() %></td>
            </tr>
            <tr>
                <th>권한(role)</th>
                <td>
                    <% if ("BLOCKED".equals(u.getRole())) { %>
                        <span style="color:#dc2626; font-weight:bold;">BLOCKED (정지)</span>
                    <% } else { %>
                        <%= u.getRole() %>
                    <% } %>
                </td>
            </tr>
            <tr>
                <th>주소 ID(addressId)</th>
                <td><%= u.getAddressId() %></td>
            </tr>
            <tr>
                <th>상세주소(addressDetail)</th>
                <td><%= u.getAddressDetail() %></td>
            </tr>
            <tr>
                <th>신뢰도(trustScore)</th>
                <td><%= u.getTrustScore() %></td>
            </tr>
            <tr>
                <th>가입일(createdAt)</th>
                <td><%= u.getCreatedAt() %></td>
            </tr>
            <tr>
                <th>수정일(updatedAt)</th>
                <td><%= u.getUpdatedAt() %></td>
            </tr>
        </table>

        <br>

        <%-- ADMIN은 정지 / 탈퇴 불가 --%>
        <% if (!"ADMIN".equals(u.getRole())) { %>

            <%-- 현재 상태에 따라 버튼 분리 --%>
            <% if ("BLOCKED".equals(u.getRole())) { %>
                <%-- 이미 정지 상태: 정지 해제만 보이기 --%>
                <button class="log-btn"
                        onclick="location.href='<%=request.getContextPath()%>/admin/users/unblock?id=<%= u.getAutoId() %>'">
                    정지 해제
                </button>
            <% } else { %>
                <%-- 일반 유저: 계정 정지만 보이기 --%>
                <button class="log-btn"
                        onclick="location.href='<%=request.getContextPath()%>/admin/users/block?id=<%= u.getAutoId() %>'">
                    계정 정지
                </button>
            <% } %>

            <%-- 탈퇴(삭제)는 NON-ADMIN이면 항상 가능하게 유지 --%>
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin/users/delete?id=<%= u.getAutoId() %>'"
                    style="background:#ff4d4d; color:white;">
                회원 탈퇴(삭제)
            </button>

            <br><br>
        <% } %>

        <% } %> <%-- u != null 닫는 부분 --%>

        <button class="log-btn"
                onclick="location.href='<%=request.getContextPath()%>/admin/users'">
            목록으로
        </button>

    </section>

</div>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.UserDTO" %>

<%
    List<UserDTO> userList = (List<UserDTO>) request.getAttribute("userList");
%>

<!DOCTYPE html>
<html lang="ko">
<head>


    <meta charset="UTF-8">
    <title>도란도란 - 회원 목록</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/admin.css">
    <style>
        .role-badge {
            display:inline-block;
            padding:2px 8px;
            border-radius:999px;
            font-size:12px;
            font-weight:600;
        }
        .role-user {
            background:#dbeafe;
            color:#1d4ed8;
        }
        .role-admin {
            background:#fef3c7;
            color:#92400e;
        }
        .role-blocked {
            background:#fee2e2;
            color:#b91c1c;
        }
        .row-blocked {
            opacity:0.7;
        }
    </style>
</head>
<body>

<div class="container">

    <header>
        <h1>회원 관리</h1>

        <div class="header-buttons">
        <button class="log-btn" onclick="location.href='<%=request.getContextPath()%>/'">홈으로</button>
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin'">
                관리자 메인
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">회원 목록</div>

        <table class="admin-table">
            <thead>
            <tr style="background:#f3f4f6;">
                <th>PK(autoId)</th>
                <th>아이디</th>
                <th>이름</th>
                <th>닉네임</th>
                <th>권한 / 상태</th>
                <th>신뢰도</th>
                <th>가입일</th>
                <th>상세</th>
            </tr>
            </thead>

            <tbody>
            <%
                if (userList == null || userList.isEmpty()) {
            %>
                <tr>
                    <td colspan="8">등록된 회원이 없습니다.</td>
                </tr>
            <%
                } else {
                    for (UserDTO u : userList) {
                        String role = u.getRole();
                        boolean isBlocked = "BLOCKED".equals(role);
            %>
                <tr class="<%= isBlocked ? "row-blocked" : "" %>">
                    <td><%= u.getAutoId() %></td>
                    <td><%= u.getUserId() %></td>
                    <td><%= u.getUserName() %></td>
                    <td><%= u.getNickname() %></td>
                    <td>
                        <% if ("ADMIN".equals(role)) { %>
                            <span class="role-badge role-admin">ADMIN</span>
                        <% } else if ("BLOCKED".equals(role)) { %>
                            <span class="role-badge role-blocked">BLOCKED (정지)</span>
                        <% } else { %>
                            <span class="role-badge role-user">USER</span>
                        <% } %>
                    </td>
                    <td><%= u.getTrustScore() %></td>
                    <td><%= u.getCreatedAt() %></td>
                    <td>
                        <a href="<%=request.getContextPath()%>/admin/users/detail?id=<%= u.getAutoId() %>">
                            상세
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

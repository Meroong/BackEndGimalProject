<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="util.AuthUtil"%>

<!-- 헤더 CSS -->
<style>
    /* 헤더 전체 */
    header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        padding-top: 10px;
    }

    .logo img {
        width: 100px !important;
        height: 100px !important;
        object-fit: contain;
    }

    .header-buttons {
        display: flex;
        gap: 10px;
        align-items: center;
    }

    /* 메시지 버튼 */
    .msg-btn {
        background: #5271FF;
        color: white;
        padding: 8px 20px;
        border-radius: 10px;
        border: none;
        cursor: pointer;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 6px;
        transition: 0.2s;
    }

    .msg-btn:hover {
        background: #3A50D8;
        transform: translateY(-2px);
    }

    /* 마이페이지 버튼 */
    .mypage-btn {
        background: #FF6600;
        color: white;
        padding: 8px 20px;
        border-radius: 10px;
        border: none;
        cursor: pointer;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 6px;
        transition: 0.2s;
    }

    .mypage-btn:hover {
        background: #e65c00;
        transform: translateY(-2px);
    }

    /* 로그아웃/로그인 버튼 */
    .log-btn {
        background: #f0f0f0;
        color: #333;
        border: 1px solid #ccc;
        padding: 8px 20px;
        border-radius: 10px;
        cursor: pointer;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 6px;
        transition: 0.2s;
    }

    .log-btn:hover {
        background: #e0e0e0;
    }
</style>

<!-- 헤더 HTML -->
<header>
    <div class="logo">
        <a href="<%= request.getContextPath() %>/index.jsp">
            <img src="<%= request.getContextPath() %>/resources/images/logo.png" alt="logo">
        </a>
    </div>

    <div class="header-buttons">
        <%
            Object loginUser = session.getAttribute("Authorization");

            if (loginUser != null) {
                String token = (String) loginUser;
                String role = AuthUtil.getRole(request); // JWT에서 역할 추출
        %>
            <!-- 로그인 상태 공통: 메시지 버튼 -->
            <button class="msg-btn"
                    onclick="location.href='<%= request.getContextPath() %>/chat/roomList'">
                메시지
            </button>

            <% if ("ADMIN".equals(role)) { %>
                <button class="log-btn"
                        onclick="location.href='<%= request.getContextPath() %>/admin'">
                    관리자
                </button>
            <% } else { %>
                <button class="mypage-btn"
                        onclick="location.href='<%= request.getContextPath() %>/views/user/mypage.jsp'">
                    마이페이지
                </button>
            <% } %>

            <!-- 로그아웃 -->
            <form action="<%= request.getContextPath() %>/user/logout" method="get" style="display:inline;">
                <button type="submit" class="log-btn">Log out</button>
            </form>

        <% } else { %>
            <!-- 비로그인 상태 -->
            <button class="log-btn"
                    onclick="location.href='<%= request.getContextPath() %>/views/user/login.jsp'">
                Log in
            </button>
        <% } %>
    </div>
</header>

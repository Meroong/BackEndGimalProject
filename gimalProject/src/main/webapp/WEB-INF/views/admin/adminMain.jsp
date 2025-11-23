<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 관리자 페이지</title>

    <!-- 메인 화면이 쓰는 home.css 그대로 사용 -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/home.css">
</head>
<body>
<div class="container">

    <%-- 메인과 동일한 헤더 --%>
    <header>
        <div class="logo">
            <img src="<%=request.getContextPath()%>/resources/images/logo.png" alt="logo">
            도란도란
        </div>

        <div class="header-buttons">
            <%
                Object loginUser = session.getAttribute("Authorization");
                if (loginUser != null) {
            %>
                <!-- 로그인 상태: 메시지 + 로그아웃 -->
                <button class="msg-btn"
                        onclick="location.href='<%=request.getContextPath()%>/views/chat/chat.jsp'">메시지</button>

                <form action="<%= request.getContextPath() %>/user/logout"
                      method="get" style="display:inline;">
                    <button type="submit" class="log-btn">Log out</button>
                </form>
            <%
                } else {
            %>
                <!-- 비로그인 상태: 로그인 버튼 -->
                <button class="log-btn"
                        onclick="location.href='<%=request.getContextPath()%>/views/user/login.jsp'">Log in</button>
            <%
                }
            %>
        </div>
    </header>

    <%-- 관리자 메인 컨텐츠 --%>
    <section class="main-box">
        <div class="box-title">관리자 메인</div>

        <p style="margin-bottom: 16px; color:#555;">
            도란도란 서비스 운영을 위한 관리자 전용 메뉴입니다.
        </p>

        <div class="grid-3">

            <div class="meeting-card" style="cursor:pointer;"
                 onclick="location.href='<%=request.getContextPath()%>/admin/users'">
                <div class="meeting-info">
                    <h3>회원 관리</h3>
                    <p>회원 목록 조회, 권한 확인</p>
                </div>
            </div>

            <div class="meeting-card"  style="cursor:pointer;"
                 onclick="location.href='<%=request.getContextPath()%>/admin/reports'">
                <div class="meeting-info">
                    <h3>신고 관리</h3>
                    <p>신고 내역 확인 및 처리</p>
                </div>
            </div>

            <div class="meeting-card" style="cursor:pointer;"
                 onclick="location.href='<%=request.getContextPath()%>/admin/stats'">
                <div class="meeting-info">
                    <h3>게시글·모임 통계</h3>
                    <p>서비스 이용 현황 조회</p>
                </div>
            </div>

            <div class="meeting-card" style="cursor:pointer;"
                 onclick="location.href='<%=request.getContextPath()%>/admin/notices'">
                <div class="meeting-info">
                    <h3>공지사항 관리</h3>
                    <p>공지 등록 / 수정 / 삭제</p>
                </div>
            </div>

        </div>
    </section>

</div>
</body>
</html>

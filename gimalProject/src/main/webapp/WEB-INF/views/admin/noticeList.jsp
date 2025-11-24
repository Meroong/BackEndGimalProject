<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.AdminNoticeDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 공지사항 관리</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/home.css">
</head>
<body>
<div class="container">

    <!-- 상단 헤더 -->
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

    <!-- 메인 영역 -->
    <section class="main-box">
        <div class="box-title">공지사항 관리</div>

        <p style="margin-bottom: 12px; color:#555;">
            도란도란 공지사항 목록입니다. (작성, 수정, 삭제 기능은 모두 관리자 전용입니다.)
        </p>

        <!-- 공지 작성 버튼 -->
        <div style="text-align:right; margin-bottom:10px;">
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin/notices/write'">
                공지 작성
            </button>
        </div>

        <table border="1" style="width:100%; border-collapse:collapse; text-align:center;">
            <thead>
            <tr style="background:#f3f4f6;">
                <th>번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>작성일</th>
                <th>조회수</th>
                <th>관리</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<AdminNoticeDTO> noticeList =
                        (List<AdminNoticeDTO>) request.getAttribute("noticeList");

                if (noticeList == null || noticeList.isEmpty()) {
            %>
                <tr>
                    <td colspan="6">등록된 공지사항이 없습니다.</td>
                </tr>
            <%
                } else {
                    for (AdminNoticeDTO n : noticeList) {
            %>
                <tr>
                    <td><%= n.getId() %></td>
                    <td><%= n.getTitle() %></td>
                    <td><%= n.getWriter() %></td>
                    <td><%= n.getCreatedAt() %></td>
                    <td><%= n.getHit() %></td>
                    <td>
                        <a href="<%=request.getContextPath()%>/admin/notices/edit?id=<%= n.getId() %>">
                            수정
                        </a>
                        |
                        <a href="<%=request.getContextPath()%>/admin/notices/delete?id=<%= n.getId() %>"
                           onclick="return confirm('정말 삭제하시겠습니까?');">
                            삭제
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

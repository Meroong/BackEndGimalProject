<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dto.ReportDTO" %>

<%
    ReportDTO r = (ReportDTO) request.getAttribute("report");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 신고 상세보기</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/home.css">
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
                    onclick="location.href='<%=request.getContextPath()%>/admin/reports'">
                신고 목록
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">신고 상세정보</div>

        <table border="1" style="width:100%; border-collapse:collapse;">
            <tr><th>ID</th><td><%= r.getId() %></td></tr>
            <tr><th>신고자 ID(reporterId)</th><td><%= r.getReporterId() %></td></tr>
            <tr><th>대상자 ID(targetUserId)</th><td><%= r.getTargetUserId() %></td></tr>
            <tr><th>타입(targetType)</th><td><%= r.getTargetType() %></td></tr>
            <tr><th>사유(reason)</th><td><%= r.getReason() %></td></tr>
            <tr><th>상태(status)</th><td><%= r.getStatus() %></td></tr>
            <tr><th>등록일시(createdAt)</th><td><%= r.getCreatedAt() %></td></tr>
        </table>

        <br>

        <button class="log-btn"
                onclick="location.href='<%=request.getContextPath()%>/admin/reports'">
            목록으로
        </button>
    </section>

</div>
</body>
</html>

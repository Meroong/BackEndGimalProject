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
        <button class="log-btn" onclick="location.href='<%=request.getContextPath()%>/'">홈으로</button>
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
            <tr><th>신고자 ID</th><td><%= r.getReporterId() %></td></tr>
            <tr><th>대상자 ID</th><td><%= r.getTargetUserId() %></td></tr>
            <tr><th>대상 타입</th><td><%= r.getTargetType() %></td></tr>
            <tr><th>사유</th><td><%= r.getReason() %></td></tr>
            <tr><th>상태</th><td><%= r.getStatus() %></td></tr>
            <tr><th>등록일</th><td><%= r.getCreatedAt() %></td></tr>
        </table>

        <br>

        <%-- 상태가 PENDING 일 때만 처리 버튼 보임 --%>
        <% if ("PENDING".equals(r.getStatus())) { %>
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin/reports/resolve?id=<%= r.getId() %>'">
                신고 처리 완료
            </button>
            <br><br>
        <% } %>

        <button class="log-btn"
                onclick="location.href='<%=request.getContextPath()%>/admin/reports'">
            목록으로
        </button>
    </section>

</div>

</body>
</html>

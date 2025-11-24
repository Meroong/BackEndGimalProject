<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dto.AdminStatsDTO" %>

<%
    AdminStatsDTO stats = (AdminStatsDTO) request.getAttribute("stats");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 관리자 통계</title>
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
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">관리자 통계</div>

        <table border="1" style="width:100%; border-collapse:collapse; text-align:center;">
            <tr style="background:#f3f4f6;">
                <th>지표</th>
                <th>값</th>
            </tr>
            <tr>
                <td>전체 회원 수</td>
                <td><%= (stats != null) ? stats.getTotalUsers() : 0 %></td>
            </tr>
            <tr>
                <td>전체 신고 수</td>
                <td><%= (stats != null) ? stats.getTotalReports() : 0 %></td>
            </tr>
        </table>

        <p style="margin-top:16px; color:#555;">
            * 통계 값은 AdminStatsDAO.getStats() 결과를 기반으로 합니다.
        </p>
    </section>

</div>

</body>
</html>

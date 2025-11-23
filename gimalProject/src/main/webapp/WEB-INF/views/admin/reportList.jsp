<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.ReportDTO" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 신고 관리</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/home.css">
</head>
<body>
<div class="container">

    <%-- 상단 헤더 (간단 버전) --%>
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
        <div class="box-title">신고 관리</div>

        <p style="margin-bottom: 12px; color:#555;">
            유저들이 등록한 신고 내역을 조회하고, 처리 상태를 변경하는 페이지입니다.
            (지금은 조회만, 나중에 처리 기능 추가 예정)
        </p>

        <table border="1" style="width:100%; border-collapse:collapse; text-align:center;">
            <thead>
            <tr style="background:#f3f4f6;">
                <th>신고 ID</th>
                <th>신고자 ID</th>
                <th>대상자 ID</th>
                <th>대상 유형</th>
                <th>사유</th>
                <th>상태</th>
                <th>신고일</th>
            </tr>
            </thead>
            <tbody>
            <%
                // 나중에 서블릿에서 "reportList"라는 이름으로 List<ReportDTO>를 넣어줄 겁니다.
                List<ReportDTO> reportList = (List<ReportDTO>) request.getAttribute("reportList");

                if (reportList == null || reportList.isEmpty()) {
            %>
                <tr>
                    <td colspan="7">등록된 신고가 없습니다.</td>
                </tr>
            <%
                } else {
                    for (ReportDTO r : reportList) {
            %>
                <tr>
                    <td><%= r.getId() %></td>
                    <td><%= r.getReporterId() %></td>
                    <td><%= r.getTargetUserId() %></td>
                    <td><%= r.getTargetType() %></td>
                    <td><%= r.getReason() %></td>
                    <td><%= r.getStatus() %></td>
                    <td><%= r.getCreatedAt() %></td>
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

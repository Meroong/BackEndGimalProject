<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

        <table border="1" style="width:100%; border-collapse:collapse; text-align:center;">
            <thead>
            <tr style="background:#f3f4f6;">
                <th>ID</th>
                <th>신고자</th>
                <th>대상자</th>
                <th>타입</th>
                <th>사유</th>
                <th>상태</th>
                <th>등록일시</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<ReportDTO> reportList =
                        (List<ReportDTO>) request.getAttribute("reportList");

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

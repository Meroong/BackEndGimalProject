<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.ReportDTO" %>

<%
    List<ReportDTO> reportList = (List<ReportDTO>) request.getAttribute("reportList");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 신고 목록</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/admin.css">
    <style>
        /* 목록에서 상태를 한눈에 보기 좋게 */
        .status-badge {
            display:inline-block;
            padding:2px 8px;
            border-radius:999px;
            font-size:12px;
            font-weight:600;
        }
        .status-pending {
            background:#fee2e2;
            color:#b91c1c;
        }
        .status-resolved {
            background:#e5e7eb;
            color:#4b5563;
        }
        .row-resolved {
            opacity:0.6;
        }
    </style>
</head>
<body>

<div class="container">

    <header>
    
        <h1>신고 관리</h1>

        <div class="header-buttons">
        <button class="log-btn" onclick="location.href='<%=request.getContextPath()%>/'">홈으로</button>
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin'">
                관리자 메인
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">신고 목록</div>

        <table class="admin-table">
            <thead>
            <tr style="background:#f3f4f6;">
                <th>ID</th>
                <th>신고자 ID</th>
                <th>대상 유저 ID</th>
                <th>대상 타입</th>
                <th>사유</th>
                <th>상태</th>
                <th>신고일</th>
                <th>상세보기</th>
            </tr>
            </thead>

            <tbody>
            <%
                if (reportList == null || reportList.isEmpty()) {
            %>
                <tr>
                    <td colspan="8">등록된 신고가 없습니다.</td>
                </tr>
            <%
                } else {
                    for (ReportDTO r : reportList) {
                        String status = r.getStatus();
                        boolean isPending = "PENDING".equals(status);
            %>
                <tr class="<%= isPending ? "" : "row-resolved" %>">
                    <td><%= r.getId() %></td>
                    <td><%= r.getReporterId() %></td>
                    <td><%= r.getTargetUserId() %></td>
                    <td><%= r.getTargetType() %></td>
                    <td><%= r.getReason() %></td>
                    <td>
                        <% if (isPending) { %>
                            <span class="status-badge status-pending">PENDING</span>
                        <% } else { %>
                            <span class="status-badge status-resolved">RESOLVED</span>
                        <% } %>
                    </td>
                    <td><%= r.getCreatedAt() %></td>
                    <td>
                        <a href="<%=request.getContextPath()%>/admin/reports/detail?id=<%= r.getId() %>">
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

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
    <style>
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 16px;
            margin-top: 16px;
        }
        .stat-card {
            padding: 20px;
            border-radius: 16px;
            box-shadow: 0 8px 18px rgba(0,0,0,0.06);
            background: #ffffff;
        }
        .stat-title {
            font-size: 14px;
            color: #6b7280;
            margin-bottom: 8px;
        }
        .stat-value {
            font-size: 28px;
            font-weight: 700;
            color: #111827;
        }
        .stat-desc {
            margin-top: 4px;
            font-size: 12px;
            color: #9ca3af;
        }
    </style>
</head>
<body>

<div class="container">

    <header>
        <h1>관리자 통계</h1>

        <div class="header-buttons">
        <button class="log-btn" onclick="location.href='<%=request.getContextPath()%>/'">홈으로</button>
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin'">
                관리자 메인
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">서비스 현황</div>

        <%
            if (stats == null) {
        %>
            <p>통계 정보를 불러올 수 없습니다.</p>
        <%
            } else {
        %>

        <div class="stats-grid">

            <!-- 총 회원 수 -->
            <div class="stat-card">
                <div class="stat-title">총 회원 수</div>
                <div class="stat-value"><%= stats.getTotalUsers() %></div>
                <div class="stat-desc">
                    현재 도란도란에 가입한 전체 회원 수입니다.
                </div>
            </div>

            <!-- 총 신고 건수 -->
            <div class="stat-card">
                <div class="stat-title">총 신고 건수</div>
                <div class="stat-value"><%= stats.getTotalReports() %></div>
                <div class="stat-desc">
                    누적 신고 내역 수입니다. 신고 관리 메뉴에서 상세 확인이 가능합니다.
                </div>
            </div>

        </div>

        <br>

        <button class="log-btn"
                onclick="location.href='<%=request.getContextPath()%>/admin/reports'">
            신고 관리 바로가기
        </button>

        <%
            }
        %>

    </section>

</div>

</body>
</html>

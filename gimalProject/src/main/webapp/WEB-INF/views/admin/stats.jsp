<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>관리자 통계</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: sans-serif; padding: 20px; }
        h2 { margin-bottom: 20px; }
        .stats-wrap { display: flex; gap: 20px; }
        .stat-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 16px 24px;
            min-width: 180px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        .stat-label { color: #666; font-size: 14px; margin-bottom: 8px; }
        .stat-value { font-size: 24px; font-weight: bold; }
    </style>
</head>
<body>

<h2>관리자 통계</h2>

<div class="stats-wrap">
    <div class="stat-card">
        <div class="stat-label">전체 회원 수</div>
        <div class="stat-value">${stats.totalUsers}</div>
    </div>

    <div class="stat-card">
        <div class="stat-label">전체 신고 수</div>
        <div class="stat-value">${stats.totalReports}</div>
    </div>
</div>

</body>
</html>

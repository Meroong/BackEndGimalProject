<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>신고 상세 정보</title>

    <!-- 관리자 전용 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">

    <!-- 이 페이지 전용 간단 스타일 -->
    <style>
        .report-content-box {
            background: #f9fafb;
            padding: 20px;
            border-radius: 12px;
            min-height: 80px;
            line-height: 1.5;
        }

        .report-action-row {
            margin-top: 16px;
            display: flex;
            justify-content: flex-end;
            gap: 8px;
        }
    </style>
</head>

<body class="admin-body">

<div class="admin-container">

    <header>
        <h1>신고 상세 정보</h1>

        <div class="header-buttons">
            <button class="log-btn"
                    onclick="location.href='${pageContext.request.contextPath}/'">
                홈으로
            </button>
            <button class="log-btn"
                    onclick="location.href='${pageContext.request.contextPath}/admin/reports'">
                목록으로
            </button>
        </div>
    </header>

    <!-- 기본 정보 -->
    <div class="main-box">
        <div class="box-title">기본 정보</div>

        <table>
            <tbody>
            <tr>
                <th>신고 ID</th>
                <td>${report.id}</td>
            </tr>
            <tr>
                <th>신고자 ID</th>
                <td>${report.reporterId}</td>
            </tr>
            <tr>
                <th>대상자 ID</th>
                <td>${report.targetUserId}</td>
            </tr>
            <tr>
                <th>타입</th>
                <td>${report.targetType}</td>
            </tr>
            <tr>
                <th>상태</th>
                <td>
                    <c:choose>
                        <c:when test="${report.status == 'PENDING'}">
                            <span class="badge badge-pending">대기</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-resolved">처리 완료</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- 신고 내용 + 버튼 -->
    <div class="main-box mt-24">
        <div class="box-title">신고 내용</div>

        <div class="report-content-box">
            <c:out value="${report.reason}" />
        </div>

        <!-- 여기서 바로 상태 변경 버튼 노출 -->
        <div class="report-action-row">

            <!-- 상태가 RESOLVED일 때만 '대기' 버튼 -->
            <c:if test="${report.status == 'RESOLVED'}">
                <button class="log-btn"
                        style="background:#6c757d;"
                        onclick="location.href='${pageContext.request.contextPath}/admin/report/resolve?id=${report.id}'">
                    대기
                </button>
            </c:if>

            <!-- 상태가 PENDING일 때만 '처리 완료' 버튼 -->
            <c:if test="${report.status == 'PENDING'}">
                <button class="log-btn"
                        style="background:#FF9800;"
                        onclick="location.href='${pageContext.request.contextPath}/admin/report/resolve?id=${report.id}'">
                    처리 완료
                </button>
            </c:if>

        </div>
    </div>

</div>
</body>
</html>

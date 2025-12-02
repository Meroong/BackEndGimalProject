<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>신고 상세 정보</title>

    <!-- 관리자 전용 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>

<body class="admin-body">

<div class="admin-container">

    <header>
        <h1>신고 상세 정보</h1>

        <button class="log-btn" onclick="location.href='${pageContext.request.contextPath}/admin/reports'">
            목록으로
        </button>
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
                    <!-- ✅ 상태는 뱃지로만 표시 (버튼 X) -->
                    <c:choose>
                        <c:when test="${report.status == 'PENDING'}">
                            <span class="badge badge-pending">PENDING</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-resolved">RESOLVED</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- 신고 내용 -->
    <div class="main-box mt-24">
        <div class="box-title">신고 내용</div>

        <div style="background:#f9fafb; padding:20px; border-radius:12px;">
            ${report.reason}
        </div>
    </div>

    <!-- 버튼 영역 -->
    <div style="margin-top:30px; text-align:right; display:flex; gap:10px; justify-content:flex-end;">

        <!-- 상태가 RESOLVED일 때만 다시 PENDING 버튼 보여주기 -->
        <c:if test="${report.status == 'RESOLVED'}">
            <button class="log-btn"
                    style="background:#6c757d;"
                    onclick="location.href='${pageContext.request.contextPath}/admin/report/resolve?id=${report.id}'">
                다시 PENDING으로 변경
            </button>
        </c:if>

        <!-- 상태가 PENDING일 때만 처리 완료 버튼 보여주기 -->
        <c:if test="${report.status == 'PENDING'}">
            <button class="log-btn"
                    style="background:#FF9800;"
                    onclick="location.href='${pageContext.request.contextPath}/admin/report/resolve?id=${report.id}'">
                처리 완료로 변경
            </button>
        </c:if>

        <button class="log-btn"
                onclick="location.href='${pageContext.request.contextPath}/admin/reports'">
            목록으로
        </button>
    </div>

</div>
</body>
</html>

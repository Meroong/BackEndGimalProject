<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>모임 관리 - 관리자</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">

    <style>
        .meeting-list-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
        }

        .filter-row {
            display: flex;
            gap: 8px;
            align-items: center;
            flex-wrap: wrap;
        }

        .filter-input {
            padding: 8px 12px;
            border-radius: 999px;
            border: 1px solid #d1d5db;
            font-size: 14px;
        }

        .filter-select {
            padding: 8px 12px;
            border-radius: 999px;
            border: 1px solid #d1d5db;
            font-size: 14px;
        }

        .status-pill {
            display: inline-block;
            padding: 3px 10px;
            border-radius: 999px;
            font-size: 11px;
            font-weight: 600;
        }
        .status-open { background:#dcfce7;color:#166534; }
        .status-closed { background:#fef3c7;color:#92400e; }
        .status-completed { background:#e5e7eb;color:#374151; }

        .meeting-table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    font-size: 15px;
}

.meeting-table thead tr {
    background: #f9fafb;
}

.meeting-table th {
    padding: 14px 12px;
    font-weight: 600;
    color: #6b7280;
    text-align: center;
    border-bottom: 1px solid #e5e7eb;
}

.meeting-table td {
    padding: 14px 12px;
    border-bottom: 1px solid #e5e7eb;
}

/* 컬럼별 정렬 */
.col-id      { width: 70px; text-align: center; }
.col-title   { text-align: left; }
.col-date    { width: 200px; text-align: center; }
.col-members { width: 150px; text-align: center; }
.col-status  { width: 120px; text-align: center; }
.col-manage  { width: 120px; text-align: center; }

/* hover 효과 */
.meeting-table tbody tr:hover {
    background: #f3f4f6;
}

/* 상태 Pill */
.status-pill {
    display: inline-block;
    padding: 4px 12px;
    border-radius: 999px;
    font-size: 12px;
    font-weight: 600;
}
.status-open { background:#dcfce7;color:#166534; }
.status-closed { background:#fef3c7;color:#92400e; }
.status-completed { background:#e5e7eb;color:#374151; }

        }
    </style>
</head>
<body class="admin-body">

<div class="admin-container">

    <header>
        <h1>모임 관리</h1>

        <div class="header-buttons">
        <button class="log-btn"
                    onclick="location.href='${pageContext.request.contextPath}/'">
                홈으로
            </button>
            <button class="log-btn"
                    onclick="location.href='${pageContext.request.contextPath}/admin'">
                관리자 메인
            </button>
        </div>
    </header>

    <section class="main-box">

        <!-- 상단 설명 + 필터 -->
        <div class="meeting-list-header">
            <div>
                <div class="box-title">모든 모임 목록</div>
                <p class="info-muted">
                    제목 검색과 상태 필터를 활용해 원하는 모임을 빠르게 찾아볼 수 있습니다.
                </p>
            </div>

            <!-- 검색 / 필터 폼 -->
            <form method="get"
                  action="${pageContext.request.contextPath}/admin/meeting/list"
                  class="filter-row">

                <input type="text" name="keyword"
                       class="filter-input"
                       placeholder="제목 검색"
                       value="${keyword}"/>

                <select name="status" class="filter-select">
                    <option value="ALL" <c:if test="${status == null || status == 'ALL'}">selected</c:if>>
                        전체 상태
                    </option>
                    <option value="OPEN" <c:if test="${status == 'OPEN'}">selected</c:if>>
                        OPEN (모집중)
                    </option>
                    <option value="CLOSED" <c:if test="${status == 'CLOSED'}">selected</c:if>>
                        CLOSED (마감)
                    </option>
                    <option value="COMPLETED" <c:if test="${status == 'COMPLETED'}">selected</c:if>>
                        COMPLETED (종료)
                    </option>
                </select>

                <button type="submit" class="log-btn">
                    검색
                </button>
            </form>
        </div>

        <!-- 목록 테이블 -->
      <table class="meeting-table">
    <thead>
    <tr>
        <th class="col-id">번호</th>
        <th class="col-title">제목</th>
        <th class="col-date">모임 일시</th>
        <th class="col-members">인원</th>
        <th class="col-status">상태</th>
        <th class="col-manage">관리</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="m" items="${meetingList}">
        <tr onclick="location.href='${pageContext.request.contextPath}/admin/meeting/detail?id=${m.id}'"
            style="cursor:pointer;">

            <td class="col-id">#${m.id}</td>

            <td class="col-title">
                <c:out value="${m.title}"/>
            </td>

            <td class="col-date">
                <fmt:formatDate value="${m.date}" pattern="yyyy-MM-dd HH:mm"/>
            </td>

            <td class="col-members">
                ${m.currentMembers} / ${m.maxMembers} 명
            </td>

            <td class="col-status">
                <c:choose>
                    <c:when test="${m.status == 'OPEN'}">
                        <span class="status-pill status-open">OPEN</span>
                    </c:when>
                    <c:when test="${m.status == 'CLOSED'}">
                        <span class="status-pill status-closed">CLOSED</span>
                    </c:when>
                    <c:when test="${m.status == 'COMPLETED'}">
                        <span class="status-pill status-completed">COMPLETED</span>
                    </c:when>
                </c:choose>
            </td>

            <td class="col-manage">
                <a class="link-btn"
                   href="${pageContext.request.contextPath}/admin/meeting/detail?id=${m.id}">
                    상세 보기
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>


    </section>

</div>

</body>
</html>

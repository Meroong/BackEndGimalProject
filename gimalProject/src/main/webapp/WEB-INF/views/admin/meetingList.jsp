<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>모임 관리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
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
        <div class="box-title">등록된 모임 목록</div>

        <!-- 🔍 검색 / 필터 -->
        <form method="get" class="meeting-filter-row">
            <input type="text" name="keyword" class="meeting-search-input"
                   placeholder="제목, 내용, 태그 검색"
                   value="${keyword != null ? keyword : ''}">

            <select name="status" class="meeting-status-select">
                <option value="ALL" ${status == null || status == 'ALL' ? 'selected' : ''}>전체 상태</option>
                <option value="OPEN" ${status == 'OPEN' ? 'selected' : ''}>OPEN (모집중)</option>
                <option value="CLOSED" ${status == 'CLOSED' ? 'selected' : ''}>CLOSED (마감)</option>
                <option value="COMPLETED" ${status == 'COMPLETED' ? 'selected' : ''}>COMPLETED (완료)</option>
            </select>

            <button type="submit" class="log-btn meeting-search-btn">검색</button>
        </form>

        <c:set var="meetingCount" value="${fn:length(meetingList)}" />

        <p class="meeting-result-text">
            검색 결과: <strong>${meetingCount}</strong>건
        </p>

        <c:if test="${empty meetingList}">
            <p>조건에 맞는 모임이 없습니다.</p>
        </c:if>

        <c:if test="${not empty meetingList}">
            <table class="admin-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>제목</th>
                    <th>일시</th>
                    <th>장소</th>
                    <th>인원</th>
                    <th>상태</th>
                    <th>등록일</th>
                    <th>관리</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="m" items="${meetingList}">
                    <tr>
                        <td>${m.id}</td>
                        <td class="text-left">
                            <a href="${pageContext.request.contextPath}/admin/meeting/detail?id=${m.id}">
                                ${m.title}
                            </a>
                        </td>
                        <td>${m.date}</td>
                        <td class="text-left">${m.location}</td>
                        <td>${m.currentMembers} / ${m.maxMembers}</td>
                        <td>
                            <c:choose>
                                <c:when test="${m.status == 'OPEN'}">
                                    <span class="badge-status badge-open">OPEN</span>
                                </c:when>
                                <c:when test="${m.status == 'CLOSED'}">
                                    <span class="badge-status badge-closed">CLOSED</span>
                                </c:when>
                                <c:when test="${m.status == 'COMPLETED'}">
                                    <span class="badge-status badge-completed">COMPLETED</span>
                                </c:when>
                                <c:otherwise>
                                    ${m.status}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${m.createdAt}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/meeting/detail?id=${m.id}"
                               class="detail-link">상세</a>
                            |
                            <a href="${pageContext.request.contextPath}/admin/meeting/delete?id=${m.id}"
                               onclick="return confirm('이 모임을 삭제하시겠습니까?');">
                                삭제
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

    </section>

</div>

</body>
</html>

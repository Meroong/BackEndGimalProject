<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>모임 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>
<body class="admin-body">

<div class="admin-container">

    <header>
        <h1>모임 상세</h1>

        <div class="header-buttons">
            <button class="log-btn"
                    onclick="location.href='${pageContext.request.contextPath}/admin/meeting/list'">
                목록으로
            </button>
            <button class="log-btn"
                    onclick="location.href='${pageContext.request.contextPath}/admin'">
                관리자 메인
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">모임 정보</div>

        <c:if test="${empty meeting}">
            <p>모임 정보를 불러올 수 없습니다.</p>
        </c:if>

        <c:if test="${not empty meeting}">
            <table class="admin-detail-table">
                <tr>
                    <th>ID</th>
                    <td>${meeting.id}</td>
                </tr>
                <tr>
                    <th>제목</th>
                    <td>${meeting.title}</td>
                </tr>
                <tr>
                    <th>소개</th>
                    <td>
                        <pre style="white-space:pre-wrap;">${meeting.content}</pre>
                    </td>
                </tr>
                <tr>
                    <th>모임 일시</th>
                    <td>${meeting.date}</td>
                </tr>
                <tr>
                    <th>장소</th>
                    <td>${meeting.location}</td>
                </tr>
                <tr>
                    <th>인원</th>
                    <td>${meeting.currentMembers} / ${meeting.maxMembers}</td>
                </tr>
                <tr>
                    <th>참가비</th>
                    <td>${meeting.cost}</td>
                </tr>
                <tr>
                    <th>태그</th>
                    <td>${meeting.tag}</td>
                </tr>
<tr>
    <th>상태</th>
    <td>
        <div class="status-row">
            <span class="current-status">현재 상태: <strong>${meeting.status}</strong></span>

            <select name="status">
                <option value="OPEN" ${meeting.status == 'OPEN' ? 'selected' : ''}>OPEN (모집중)</option>
                <option value="CLOSED" ${meeting.status == 'CLOSED' ? 'selected' : ''}>CLOSED (마감)</option>
                <option value="COMPLETED" ${meeting.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED (완료)</option>
            </select>

            <button type="submit" class="btn-primary">상태 변경</button>
        </div>
    </td>
</tr>


                <tr>
                    <th>등록일</th>
                    <td>${meeting.createdAt}</td>
                </tr>
            </table>

            <br>

          <div class="delete-align-wrapper">
    <a href="${pageContext.request.contextPath}/admin/meeting/delete?id=${meeting.id}"
       class="delete-btn">삭제</a>
</div>

        </c:if>

    </section>

</div>

</body>
</html>

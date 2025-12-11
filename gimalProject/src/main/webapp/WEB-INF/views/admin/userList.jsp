<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 회원 관리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>
<body class="admin-body">

<div class="admin-container">

    <!-- 상단 헤더 : 제목 + 오른쪽 버튼들 -->
    <header>
        <h1>관리자 회원 관리</h1>

        <div class="header-buttons">
            <a href="${pageContext.request.contextPath}/" class="log-btn">홈으로</a>
            <a href="${pageContext.request.contextPath}/admin" class="log-btn">관리자 메인</a>
        </div>
    </header>

    <!-- 메인 박스 -->
    <div class="main-box">

        <!-- 회원 목록 제목 -->
        <div class="notice-header-row">
            <h2 class="box-title">회원 목록</h2>
            <!-- 필요하면 오른쪽에 필터/검색 버튼 나중에 추가 가능 -->
        </div>

        <!-- 회원 목록 테이블 -->
        <table>
            <thead>
            <tr>
                <th>번호</th>
                <th>아이디</th>
                <th>닉네임</th>
                <th>이름</th>
                <th>권한 / 상태</th>
                <th>가입일</th>
                <th>상세</th>
            </tr>
            </thead>

            <tbody>
            <c:if test="${empty userList}">
                <tr>
                    <td colspan="7">등록된 회원이 없습니다.</td>
                </tr>
            </c:if>

            <c:forEach var="u" items="${userList}">
                <tr>
                    <td>${u.autoId}</td>
                    <td>${u.userId}</td>
                    <td>${u.nickname}</td>
                    <td>${u.userName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${u.role eq 'ADMIN'}">
                                <span class="badge badge-admin">ADMIN</span>
                            </c:when>
                            <c:when test="${u.role eq 'BLOCKED'}">
                                <span class="badge badge-pending">BLOCKED</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-user">USER</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${u.createdAt}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/users/detail?id=${u.autoId}"
                        >
                            상세보기
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div><!-- /.main-box -->

</div><!-- /.admin-container -->

</body>
</html>
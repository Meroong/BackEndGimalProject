<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 공지사항 관리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>
<body class="admin-body">

<div class="admin-container">

    <!-- 상단 헤더 : 제목 + 오른쪽 버튼들 -->
    <header>
        <h1>관리자 공지사항 관리</h1>

        <!-- 오른쪽 상단 버튼 그룹 (홈으로 / 관리자 메인) -->
        <div class="header-buttons">
            <a href="${pageContext.request.contextPath}/" class="log-btn">홈으로</a>
            <a href="${pageContext.request.contextPath}/admin" class="log-btn">관리자 메인</a>
        </div>
    </header>

    <!-- 공지사항 메인 박스 -->
    <div class="main-box">

        <!-- 제목 + 오른쪽 공지 작성 버튼 (한 줄 정렬) -->
        <div class="notice-header-row">
            <h2 class="box-title">공지사항 목록</h2>
            <a href="${pageContext.request.contextPath}/admin/notices/write" class="top-btn">
                공지 작성
            </a>
        </div>

        <!-- 공지 목록 테이블 -->
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>제목</th>
                <th>작성일</th>
                <th>수정</th>
                <th>삭제</th>
            </tr>
            </thead>

            <tbody>
            <c:if test="${empty noticeList}">
                <tr>
                    <td colspan="5">등록된 공지사항이 없습니다.</td>
                </tr>
            </c:if>

            <c:forEach var="n" items="${noticeList}">
                <tr>
                    <td>${n.id}</td>
                    <td>${n.title}</td>
                    <td>${n.createdAt}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/notices/edit?id=${n.id}">
                            수정
                        </a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/notices/delete?id=${n.id}"
                           onclick="return confirm('정말 삭제하시겠습니까?');">
                            삭제
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div> <!-- /.main-box -->

</div> <!-- /.admin-container -->

</body>
</html>
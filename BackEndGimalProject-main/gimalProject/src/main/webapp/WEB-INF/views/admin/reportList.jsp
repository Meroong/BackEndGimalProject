<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 신고 관리</title>
    <!-- 관리자 공통 CSS (경로는 기존 공지/회원이랑 동일하게) -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>
<body class="admin-body">

<div class="admin-container">

    <!-- 상단 헤더 -->
    <header>
        <h1>신고 관리</h1>
        <div class="header-buttons">
            <a href="${pageContext.request.contextPath}/" class="log-btn">홈으로</a>
            <a href="${pageContext.request.contextPath}/admin" class="log-btn">관리자 메인</a>
        </div>
    </header>

    <!-- 메인 박스 -->
    <div class="main-box">

        <h2 class="box-title">신고 목록</h2>
        <p>회원들이 접수한 신고 내역을 확인하고 상태를 변경할 수 있습니다.</p>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>신고자 ID</th>
                    <th>대상자 ID</th>
                    <th>사유</th>
                    <th>상태</th>
                    <th>상세</th>
                </tr>
            </thead>

            <tbody>
                <!-- 신고가 하나도 없을 때 -->
                <c:if test="${empty reportList}">
                    <tr>
                        <td colspan="6">등록된 신고가 없습니다.</td>
                    </tr>
                </c:if>

                <!-- 신고 목록 루프 -->
                <c:forEach var="report" items="${reportList}">
                    <tr>
                        <!-- 여기부터가 민섭님이 말한 부분 -->
                        <td>${report.id}</td>
                        <td>${report.reporterId}</td>
                        <td>${report.targetUserId}</td>
                        <td>${report.reason}</td>

                        <!-- 상태 배지 (필드명이 status 라고 가정) -->
                        <td>
                            <c:choose>
                                <c:when test="${report.status eq 'PENDING'}">
                                    <span class="badge badge-pending">대기</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-resolved">정지</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <!-- 상세보기 링크 (경로는 프로젝트에 맞게) -->
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/reports/detail?id=${report.id}">
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
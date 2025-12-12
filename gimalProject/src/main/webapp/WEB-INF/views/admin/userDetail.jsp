<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 회원 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>
<body class="admin-body">

<div class="admin-container">

    <header>
        <h1>회원 기본정보</h1>
        <div class="header-buttons">
            <a href="${pageContext.request.contextPath}/" class="log-btn">홈으로</a>
            <a href="${pageContext.request.contextPath}/admin" class="log-btn">관리자 메인</a>
        </div>
    </header>

    <div class="main-box">

        <div class="user-detail-header">
            <div>
                <h2 class="box-title">회원 상세 정보</h2>
                <p class="user-sub">
                    회원 PK(autoId) : <strong>${user.autoId}</strong>
                </p>
            </div>

            <div>
                <c:choose>
                    <c:when test="${user.role eq 'ADMIN'}">
                        <span class="badge badge-admin">ADMIN</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-user">USER</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="user-detail-layout">

            <!-- 왼쪽 영역 -->
            <section class="user-card user-main-card">
                <h3>기본 정보</h3>

                <div class="user-field-row">
                    <span class="field-label">아이디</span>
                    <span class="field-value">${user.userId}</span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">이름</span>
                    <span class="field-value">${user.userName}</span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">닉네임</span>
                    <span class="field-value">${user.nickname}</span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">권한</span>
                    <span class="field-value">
                        <c:choose>
                            <c:when test="${user.role eq 'ADMIN'}">ADMIN</c:when>
                            <c:otherwise>USER</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">신뢰도</span>
                    <span class="field-value">${user.trustScore}</span>
                </div>
            </section>

            <!-- 오른쪽 영역 -->
            <section class="user-card user-meta-card">
                <h3>추가 정보</h3>

                <div class="user-field-row">
                    <span class="field-label">주소 ID</span>
                    <span class="field-value">
                        <c:choose>
                            <c:when test="${empty user.addressId}">-</c:when>
                            <c:otherwise>${user.addressId}</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">상세주소</span>
                    <span class="field-value">
                        <c:choose>
                            <c:when test="${empty user.addressDetail}">-</c:when>
                            <c:otherwise>${user.addressDetail}</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <!-- 가입일 -->
                <div class="user-field-row">
                    <span class="field-label">가입일</span>
                    <span class="field-value">
                        <fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                    </span>
                </div>

                <!-- 수정일 -->
                <div class="user-field-row">
                    <span class="field-label">수정일</span>
                    <span class="field-value">
                        <c:choose>
                            <c:when test="${empty user.updatedAt}">-</c:when>
                            <c:otherwise>
                                <fmt:formatDate value="${user.updatedAt}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>

            </section>

        </div><!-- /.user-detail-layout -->

        <div class="user-actions">
            <button type="button"
                    class="log-btn btn-ghost"
                    onclick="location.href='${pageContext.request.contextPath}/admin/users'">
                목록으로
            </button>
        </div>

    </div><!-- /.main-box -->

</div><!-- /.admin-container -->

</body>
</html>

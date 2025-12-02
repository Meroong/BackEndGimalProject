<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // 컨트롤러에서 request.setAttribute("user", user); 로 넘겨준다고 가정
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 회원 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>
<body class="admin-body">

<div class="admin-container">

    <!-- 상단 헤더 : 제목 + 오른쪽 버튼 -->
    <header>
        <h1>회원 기본정보</h1>

        <div class="header-buttons">
            <a href="${pageContext.request.contextPath}/" class="log-btn">홈으로</a>
            <a href="${pageContext.request.contextPath}/admin" class="log-btn">관리자 메인</a>
        </div>
    </header>

    <div class="main-box">

        <!-- 상단 타이틀 + PK 표시 -->
        <div class="user-detail-header">
            <div>
                <h2 class="box-title">회원 상세 정보</h2>
                <p class="user-sub">
                    회원 PK(autoId) : <strong>${user.autoId}</strong>
                </p>
            </div>

            <!-- 역할/상태 배지 -->
            <div>
                <c:choose>
                    <c:when test="${user.role eq 'ADMIN'}">
                        <span class="badge badge-admin">ADMIN</span>
                    </c:when>
                    <c:when test="${user.role eq 'BLOCKED'}">
                        <span class="badge badge-pending">BLOCKED</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-user">USER</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- 좌우 카드 레이아웃 -->
        <div class="user-detail-layout">

            <!-- 왼쪽 : 기본 프로필 카드 -->
            <section class="user-card user-main-card">
                <h3>기본 정보</h3>

                <div class="user-field-row">
                    <span class="field-label">아이디(userId)</span>
                    <span class="field-value">${user.userId}</span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">이름(userName)</span>
                    <span class="field-value">${user.userName}</span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">닉네임(nickname)</span>
                    <span class="field-value">${user.nickname}</span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">권한(role)</span>
                    <span class="field-value">
                        <c:choose>
                            <c:when test="${user.role eq 'ADMIN'}">ADMIN</c:when>
                            <c:when test="${user.role eq 'BLOCKED'}">BLOCKED</c:when>
                            <c:otherwise>USER</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">신뢰도(trustScore)</span>
                    <span class="field-value">${user.trustScore}</span>
                </div>
            </section>

            <!-- 오른쪽 : 주소/날짜 카드 -->
            <section class="user-card user-meta-card">
                <h3>추가 정보</h3>

                <div class="user-field-row">
                    <span class="field-label">주소 ID(addressId)</span>
                    <span class="field-value">
                        <c:choose>
                            <c:when test="${empty user.addressId}">-</c:when>
                            <c:otherwise>${user.addressId}</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">상세주소(addressDetail)</span>
                    <span class="field-value">
                        <c:choose>
                            <c:when test="${empty user.addressDetail}">-</c:when>
                            <c:otherwise>${user.addressDetail}</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">가입일(createdAt)</span>
                    <span class="field-value">${user.createdAt}</span>
                </div>

                <div class="user-field-row">
                    <span class="field-label">수정일(updatedAt)</span>
                    <span class="field-value">
                        <c:choose>
                            <c:when test="${empty user.updatedAt}">-</c:when>
                            <c:otherwise>${user.updatedAt}</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </section>

        </div><!-- /.user-detail-layout -->

               <!-- 하단 버튼 영역 -->
        <div class="user-actions">

            <c:choose>
                <%-- 🔒 대상 회원이 ADMIN 이면 정지/탈퇴 버튼 숨기기 --%>
                <c:when test="${user.role eq 'ADMIN'}">

            
                    <!-- 목록으로만 보이게 -->
                    <button type="button"
                            class="log-btn btn-ghost"
                            onclick="location.href='${pageContext.request.contextPath}/admin/users'">
                        목록으로
                    </button>

                </c:when>

                <%-- 일반 USER / BLOCKED 계정일 때만 정지/탈퇴 버튼 노출 --%>
                <c:otherwise>

                    <c:choose>
                        <c:when test="${user.role eq 'BLOCKED'}">
                            <!-- 정지 해제 -->
                            <button type="button"
                                    class="log-btn btn-secondary"
                                    onclick="location.href='${pageContext.request.contextPath}/admin/users/unblock?id=${user.autoId}'">
                                정지 해제
                            </button>
                        </c:when>
                        <c:otherwise>
                            <!-- 계정 정지 -->
                            <button type="button"
                                    class="log-btn btn-warning"
                                    onclick="location.href='${pageContext.request.contextPath}/admin/users/block?id=${user.autoId}'">
                                계정 정지
                            </button>
                        </c:otherwise>
                    </c:choose>

                    <!-- 회원 탈퇴 -->
                    <button type="button"
                            class="log-btn btn-danger"
                            onclick="if(confirm('정말 이 회원을 탈퇴(삭제) 처리하시겠습니까?')) location.href='${pageContext.request.contextPath}/admin/users/delete?id=${user.autoId}'">
                        회원 탈퇴(삭제)
                    </button>

                    <!-- 목록으로 -->
                    <button type="button"
                            class="log-btn btn-ghost"
                            onclick="location.href='${pageContext.request.contextPath}/admin/users'">
                        목록으로
                    </button>

                </c:otherwise>
            </c:choose>

        </div>


    </div><!-- /.main-box -->

</div><!-- /.admin-container -->

</body>
</html>

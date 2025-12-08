<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 메인</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>
<body class="admin-body">

<div class="admin-container">

    <!-- 상단 헤더 -->
    <header>
        <h1>관리자 메인</h1>

        <div class="header-buttons">
            <button class="log-btn"
                    onclick="location.href='${pageContext.request.contextPath}/'">
                홈으로
            </button>
        </div>
    </header>

    <!-- 안내 텍스트 + 카드들 -->
    <div class="main-box">
        <div class="box-title">관리 기능 한눈에 보기</div>
        <p>도란도란 모임 서비스를 안전하고 효율적으로 운영하기 위한 관리자 전용 메뉴입니다.</p>

        <div class="admin-card-grid">

            <!-- 공지 관리 카드 -->
            <a href="${pageContext.request.contextPath}/admin/notice/list" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">공지 관리</div>
                        <div class="admin-card-sub">
                            모임 운영 관련 공지를 등록·수정·삭제할 수 있어요.
                        </div>
                    </div>
                    <div class="admin-card-icon">🔔</div>
                </div>

                <ul class="admin-card-meta-list">
                    <li>중요 공지나 서비스 점검 일정 등록</li>
                    <li>기간 지난 공지 정리하여 서비스 운영 최적화</li>
                </ul>
            </a>

            <!-- 회원 관리 카드 -->
            <a href="${pageContext.request.contextPath}/admin/users" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">회원 관리</div>
                        <div class="admin-card-sub">
                            회원 정보를 조회하고, 정지·탈퇴 처리가 가능해요.
                        </div>
                    </div>
                    <div class="admin-card-icon">👤</div>
                </div>

                <ul class="admin-card-meta-list">
                    <li>문제가 되는 회원 차단 (BLOCKED)</li>
                    <li>회원 상세 정보와 활동 내역 확인</li>
                </ul>
            </a>

            <!-- 신고 관리 카드 -->
            <a href="${pageContext.request.contextPath}/admin/reports" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">신고 관리</div>
                        <div class="admin-card-sub">
                            회원/모임 관련 신고를 확인하고 처리할 수 있어요.
                        </div>
                    </div>
                    <div class="admin-card-icon">⚠️</div>
                </div>

                <ul class="admin-card-meta-list">
                    <li>PENDING → RESOLVED 처리 토글 가능</li>
                    <li>신고 내용 검토 후 제재 여부 결정</li>
                </ul>
            </a>

            <!-- ✅ 모임 관리 카드 (새로 추가된 부분) -->
            <a href="${pageContext.request.contextPath}/admin/meeting/list" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">모임 관리</div>
                        <div class="admin-card-sub">
                            개설된 모임을 한눈에 보고 상태 변경·삭제를 할 수 있어요.
                        </div>
                    </div>
                    <div class="admin-card-icon">📅</div>
                </div>

                <ul class="admin-card-meta-list">
                    <li>전체 모임 목록 확인</li>
                    <li>문제 있는 모임 상태 변경 또는 삭제</li>
                </ul>
            </a>

            <!-- 서비스 통계 카드 -->
            <a href="${pageContext.request.contextPath}/admin/stats" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">서비스 통계</div>
                        <div class="admin-card-sub">
                            회원 증가, 모임 개설 수, 신고 현황을 그래프로 확인할 수 있어요.
                        </div>
                    </div>
                    <div class="admin-card-icon">📊</div>
                </div>

                <ul class="admin-card-meta-list">
                    <li>모임 서비스 운영 상태 한눈에 파악</li>
                    <li>신고 현황과 회원 활동 분석</li>
                </ul>
            </a>

        </div><!-- /.admin-card-grid -->
    </div><!-- /.main-box -->

</div><!-- /.admin-container -->

</body>
</html>

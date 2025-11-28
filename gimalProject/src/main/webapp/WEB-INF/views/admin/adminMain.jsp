<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 관리자 메인</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/admin.css">
</head>
<body class="admin-body">

<div class="admin-container">

    <header>
        <h1>관리자 메인</h1>

        <div class="header-buttons">
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/'">
                홈으로
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">관리 기능 한눈에 보기</div>
        <p>도란도란을 안전하고 편하게 운영하기 위한 관리자 전용 메뉴입니다.</p>

        <div class="admin-card-grid">

            <!-- 공지 관리 -->
            <a href="<%=request.getContextPath()%>/admin/notices" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">공지 관리</div>
                        <div class="admin-card-sub">
                            서비스 공지와 안내 문구를 등록·수정·삭제할 수 있어요.
                        </div>
                    </div>
                    <div class="admin-card-icon">📢</div>
                </div>
                <ul class="admin-card-meta-list">
                    <li>새 기능 오픈이나 점검 안내 등록</li>
                    <li>기간이 지난 공지는 정리해서 깔끔하게 관리</li>
                </ul>
            </a>

            <!-- 회원 관리 -->
            <a href="<%=request.getContextPath()%>/admin/users" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">회원 관리</div>
                        <div class="admin-card-sub">
                            가입된 회원 정보를 확인하고, 필요 시 정지·탈퇴 처리를 할 수 있어요.
                        </div>
                    </div>
                    <div class="admin-card-icon">👤</div>
                </div>
                <ul class="admin-card-meta-list">
                    <li>문제가 되는 회원은 BLOCKED 상태로 전환</li>
                    <li>신뢰도(trustScore)로 활동 이력을 한 번에 확인</li>
                </ul>
            </a>

            <!-- 신고 관리 -->
            <a href="<%=request.getContextPath()%>/admin/reports" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">신고 관리</div>
                        <div class="admin-card-sub">
                            이용자들이 보낸 신고를 확인하고 처리 상태를 변경할 수 있어요.
                        </div>
                    </div>
                    <div class="admin-card-icon">⚠️</div>
                </div>
                <ul class="admin-card-meta-list">
                    <li>PENDING 상태의 신고를 검토 후 RESOLVED로 처리</li>
                    <li>신고자 · 피신고자 정보를 보고 추가 조치 여부 결정</li>
                </ul>
            </a>

            <!-- 서비스 통계 -->
            <a href="<%=request.getContextPath()%>/admin/stats" class="admin-card">
                <div class="admin-card-header">
                    <div>
                        <div class="admin-card-title">서비스 통계</div>
                        <div class="admin-card-sub">
                            전체 회원 수와 신고 건수를 기준으로 서비스 현황을 확인할 수 있어요.
                        </div>
                    </div>
                    <div class="admin-card-icon">📊</div>
                </div>
                <ul class="admin-card-meta-list">
                    <li>가입자 규모와 신고 추이를 간단히 파악</li>
                    <li>운영 방향을 정할 때 참고 자료로 활용</li>
                </ul>
            </a>

        </div>

    </section>

</div>

</body>
</html>

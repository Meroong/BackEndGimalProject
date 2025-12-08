<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>우리 동네 드림 - 도란도란</title>
<!-- 부트스트랩 CSS (프로젝트에 맞춰 경로 수정 필요) -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
.dream-card {
transition: transform 0.2s;
border: none;
box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}
.dream-card:hover {
transform: translateY(-5px);
}
.card-img-top {
height: 200px;
object-fit: cover;
border-top-left-radius: 10px;
border-top-right-radius: 10px;
}
.status-badge {
position: absolute;
top: 10px;
left: 10px;
padding: 5px 10px;
border-radius: 20px;
font-size: 0.8rem;
font-weight: bold;
color: white;
}
.bg-available { background-color: #28a745; } /* 초록색: 나눔중 /
.bg-reserved { background-color: #ffc107; color: #333; } / 노란색: 예약중 /
.bg-completed { background-color: #6c757d; } / 회색: 완료 */

    .floating-btn {
        position: fixed;
        bottom: 30px;
        right: 30px;
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background-color: #FF6F61; /* 도란도란 포인트 컬러 */
        color: white;
        font-size: 30px;
        border: none;
        box-shadow: 0 4px 10px rgba(0,0,0,0.3);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
        text-decoration: none;
    }
    .floating-btn:hover { background-color: #e65b50; color: white; }
</style>


</head>
<body class="bg-light">

<!-- 헤더 포함 (가정) -->
<jsp:include page="../../include//header.jsp" />

<div class="container py-5">
    <div class="row mb-4">
        <div class="col-12 text-center">
            <h2 class="fw-bold text-dark">🎁 우리 동네 무료 나눔</h2>
            <p class="text-muted">이웃과 따뜻한 마음을 나누세요</p>
        </div>
    </div>

    <!-- 필터 영역 (지역 등) -->
    <div class="d-flex justify-content-between mb-3">
        <span class="fw-bold fs-5">📍 ${sessionScope.userAddress.dongName} 근처의 드림</span>
        <div class="dropdown">
            <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                최신순
            </button>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="#">최신순</a></li>
                <li><a class="dropdown-item" href="#">인기순</a></li>
            </ul>
        </div>
    </div>

    <!-- 드림 아이템 리스트 -->
    <div class="row row-cols-1 row-cols-md-3 row-cols-lg-4 g-4">
        
        <%-- 서버에서 넘겨준 dreamList가 비어있을 경우 --%>
        <c:if test="${empty dreamList}">
            <div class="col-12 text-center py-5">
                <p class="text-muted">아직 등록된 드림이 없어요. 첫 번째 나눔을 실천해보세요!</p>
            </div>
        </c:if>

        <%-- 리스트 반복 출력 --%>
        <c:forEach var="item" items="${dreamList}">
            <div class="col">
                <div class="card dream-card h-100" onclick="location.href='/item/detail?id=${item.itemId}'" style="cursor: pointer;">
                    <!-- 상태 뱃지 -->
                    <div class="status-badge 
                        ${item.status == 'AVAILABLE' ? 'bg-available' : 
                          item.status == 'RESERVED' ? 'bg-reserved' : 'bg-completed'}">
                        ${item.status == 'AVAILABLE' ? '나눔중' : 
                          item.status == 'RESERVED' ? '예약중' : '나눔완료'}
                    </div>

                    <!-- 썸네일 이미지 (없으면 기본 이미지) -->
                    <img src="${item.thumbnail != null ? item.thumbnail : '/images/no-image.png'}" class="card-img-top" alt="${item.title}">
                    
                    <div class="card-body">
                        <h5 class="card-title text-truncate">${item.title}</h5>
                        <div class="d-flex justify-content-between align-items-center mt-2">
                            <span class="badge bg-danger">무료</span>
                            <small class="text-muted">Wait: ${item.timeAgo}</small> <!-- time_ago -> timeAgo 수정 -->
                        </div>
                        <p class="card-text text-muted small mt-2 text-truncate">${item.sellerNickname}</p> <!-- seller_nickname -> sellerNickname 수정 -->
                        <p class="card-text text-secondary small">📍 ${item.dongName}</p> <!-- dong_name -> dongName 수정 -->
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<!-- 글쓰기 버튼 (로그인 시에만 노출) -->
<c:if test="${not empty sessionScope.user}">
    <a href="/item/write?type=DREAM" class="floating-btn" title="드림 등록하기">
        +
    </a>
</c:if>

<!-- 푸터 포함 -->
<jsp:include page="../../include//footer.jsp" />


</body>
</html>
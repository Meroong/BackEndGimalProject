<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>드림 등록하기 - 도란도란</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../../include/header.jsp" />

    <div class="container mt-5" style="max-width: 800px;">
        <h3 class="mb-4 fw-bold">🎁 무료 나눔 등록</h3>
        
        <form action="/item/register" method="post" enctype="multipart/form-data">
            <!-- (핵심) 거래 타입을 DREAM으로 고정 -->
            <input type="hidden" name="tradeType" value="DREAM">
            <!-- (핵심) 가격을 0으로 고정 -->
            <input type="hidden" name="price" value="0">
            <!-- 작성자 ID (세션에서 처리하겠지만 폼에 필요하다면) -->
            <input type="hidden" name="sellerId" value="${sessionScope.user.autoId}">

            <!-- 제목 -->
            <div class="mb-3">
                <label for="title" class="form-label fw-bold">제목</label>
                <input type="text" class="form-control" id="title" name="title" placeholder="나눔할 물품 제목을 입력해주세요" required>
            </div>

            <!-- 카테고리 -->
            <div class="mb-3">
                <label for="category" class="form-label fw-bold">카테고리</label>
                <select class="form-select" id="category" name="categoryId" required>
                    <option value="" selected disabled>카테고리 선택</option>
                    <option value="1">육아용품</option>
                    <option value="2">가구/인테리어</option>
                    <option value="3">생활/가전</option>
                    <option value="4">도서/티켓</option>
                    <option value="5">기타</option>
                </select>
            </div>

            <!-- 사진 업로드 -->
            <div class="mb-3">
                <label for="images" class="form-label fw-bold">사진 첨부</label>
                <input type="file" class="form-control" id="images" name="file" multiple accept="image/*">
                <div class="form-text">나눔할 물건의 상태를 잘 보여주는 사진을 올려주세요.</div>
            </div>

            <!-- 내용 -->
            <div class="mb-4">
                <label for="content" class="form-label fw-bold">자세한 설명</label>
                <textarea class="form-control" id="content" name="content" rows="6" placeholder="나눔하는 이유, 물건의 상태, 직거래 장소 등을 적어주세요." required></textarea>
            </div>

            <!-- 버튼 -->
            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                <button type="button" class="btn btn-secondary me-md-2" onclick="history.back()">취소</button>
                <button type="submit" class="btn btn-primary" style="background-color: #FF6F61; border-color: #FF6F61;">나눔 등록 완료</button>
            </div>
        </form>
    </div>

    <jsp:include page="../../include//footer.jsp" />
</body>
</html>
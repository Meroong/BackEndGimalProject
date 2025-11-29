<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    // 예: 세션에 저장된 현재 프로필 이미지 URL
    String profileUrl = (String) session.getAttribute("profileUrl");

    if (profileUrl == null) {
        profileUrl = "resources/images/default_profile.png";  // 기본 이미지
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>프로필 이미지 테스트</title>
<style>
    .profile-box {
        width: 200px;
        margin-bottom: 20px;
    }
    .profile-box img {
        width: 200px;
        height: 200px;
        border-radius: 50%;
        object-fit: cover;
        border: 2px solid #aaa;
    }
    button {
        padding: 8px 12px;
        margin-top: 10px;
        cursor: pointer;
    }
</style>
</head>
<body>

<h2>프로필 이미지 테스트 페이지</h2>

<div class="profile-box">
    <img src="<%=profileUrl%>" alt="프로필 이미지">
</div>

<!-- 이미지 변경 / 업로드 서블렛 3.0 지원사항-->
<form action="uploadProfile" method="post" enctype="multipart/form-data">

	<!-- input type="file"을 사용해서 파일선택 버튼이 자동으로 생김 -->
	
    <input type="file" name="img" accept="image/*" required>
    <button type="submit">이미지 등록/수정</button>
</form>

<!-- 이미지 삭제 -->
<form action="deleteProfile" method="post">
    <button type="submit" style="background:#ff4e4e; color:white;">
        프로필 이미지 삭제
    </button>
</form>

</body>
</html>

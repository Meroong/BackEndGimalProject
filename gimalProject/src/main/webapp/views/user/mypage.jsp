<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO"%>
<%@ page import="dto.UserAddressDTO"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지 - 도란도란</title>
    <style>
        /* ===================== 전체 스타일 ===================== */
        body {
            margin: 0;
            padding: 0;
            background: #F5F6FA;
            font-family: 'Pretendard', sans-serif;
            color: #222;
        }
        .container { width: 1400px; margin: 0 auto; padding: 20px 40px; }
        header { display: flex; justify-content: space-between; align-items: flex-start; padding-top: 10px; }
        .logo { display: flex; align-items: center; gap: 10px; font-size: 28px; font-weight: 800; color: #FF7C40; }
        .logo img { width: 100px; height: 100px; object-fit: contain; }
        .header-buttons { display: flex; gap: 10px; align-items: center; }
        .log-btn, .mypage-btn, .update-btn, .delete-btn {
            padding: 8px 20px; border-radius: 10px; border: none; cursor: pointer;
            font-weight: 600; display: flex; align-items: center; gap: 6px; transition: 0.2s;
        }
        .log-btn { background: #f0f0f0; color: #333; border: 1px solid #ccc; }
        .log-btn:hover { background: #e0e0e0; }
        .mypage-btn, .update-btn { background: #FF6600; color: white; }
        .mypage-btn:hover, .update-btn:hover { background: #e65c00; }
        .delete-btn { background: #FF4444; color: white; }
        .delete-btn:hover { background: #cc3333; }
        .main-box { margin-top: 50px; background: white; padding: 45px; border-radius: 24px; box-shadow: 0 6px 24px rgba(0,0,0,0.06); }
        .box-title { font-size: 22px; font-weight: 700; margin-bottom: 25px; }
        .grid-3 { display: grid; grid-template-columns: 1.2fr 1.8fr 1fr; gap: 32px; }
        .center-card, .map-card, .weather-card {
            background: #FCFBFE; border-radius: 20px; padding: 30px; box-shadow: 0 4px 16px rgba(0,0,0,0.05);
        }
        .center-title { font-size: 20px; font-weight: 700; margin-bottom: 12px; }
        .center-desc { color: #666; font-size: 16px; margin-bottom: 20px; }
        input[type="text"], input[type="password"] {
            width: 100%; padding: 12px 15px; margin: 5px 0 15px 0; border-radius: 12px; border: 1px solid #DDD; font-size: 15px; font-weight: 500;
        }
        label { font-weight: 600; font-size: 14px; }
        form button { width: 100%; padding: 12px 0; border-radius: 12px; font-size: 15px; }
        h2, h3 { margin-bottom: 15px; }
    </style>
    <script>
		const POPUP_KEY = "<%= "devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc=" %>"; // 팝업용 키
		const RETURN_URL = "http://localhost:8080<%= request.getContextPath() %>/views/util/addressPopupReturn.jsp";
		function openJusoPopup() {
		    window.open(
		        "https://business.juso.go.kr/addrlink/addrLinkUrl.do?confmKey=" + POPUP_KEY 
		        + "&returnUrl=" + encodeURIComponent(RETURN_URL)
		        + "&resultType=4",
		        "jusoPopup",
		        "width=570,height=420,scrollbars=yes,resizable=yes"
		    );
		}
	</script>
	<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
</head>
<body>
<div class="container">

<%-- 헤더 --%>
<header>
    <div class="logo">
        <img src="<%= request.getContextPath() %>/resources/images/logo.png" alt="logo">
        도란도란
    </div>
    <div class="header-buttons">
        <form action="<%= request.getContextPath() %>/user/logout" method="get">
            <button type="submit" class="log-btn">Log out</button>
        </form>
    </div>
</header>

<%-- 로그인 체크 --%>
<%
    UserDTO user = (UserDTO) session.getAttribute("userInfo");
    UserAddressDTO addr = (UserAddressDTO) session.getAttribute("addressInfo");
    if (user != null) {
%>

<section class="main-box">
    <div class="box-title">마이페이지</div>
    <div class="grid-3">

        <%-- 왼쪽: 회원 정보 수정 --%>
        <div class="center-card">
            <div class="center-title">내 정보</div>
            <div class="center-desc">회원님의 정보를 확인하고 수정할 수 있습니다.</div>

            <form id="updateForm" action="<%= request.getContextPath() %>/user/update" method="post">
                <input type="hidden" name="autoId" value="<%= user.getAutoId() %>">

                <label>이름</label>
                <input type="text" value="<%= user.getUserName() != null ? user.getUserName() : "" %>" disabled>

                <label>닉네임</label>
                <input type="text" name="newNickname" value="<%= user.getNickname() != null ? user.getNickname() : "" %>">

                <label>비밀번호</label>
                <input type="password" name="newPassword" placeholder="변경할 비밀번호">

                <%-- 주소 영역 --%>
                <label>주소</label>
                <button type="button" class="update-btn" onclick="openJusoPopup()" style="width:auto; margin-bottom:10px;">주소 검색</button>

				<!-- 도로명주소 -->
				<label>도로명주소</label>
				<input type="text" id="roadAddress" disabled value="<%= (addr != null && addr.getRoadAddress() != null) ? addr.getRoadAddress() : "" %>" />
				
				<input type="hidden" id="roadAddressValue" name="roadAddress" value="<%= (addr != null && addr.getRoadAddress() != null) ? addr.getRoadAddress() : "" %>" />
				
				
				<!-- 지번주소 disabled라 팝업에서 .textContent가 아니라 .value 사용해야함-->
				<label>지번주소</label>
				<input type="text" id="jibunAddress" disabled value="<%= (addr != null && addr.getJibunAddress() != null) ? addr.getJibunAddress() : "" %>" />
				
				<input type="hidden" id="jibunAddressValue" name="jibunAddress" value="<%= (addr != null && addr.getJibunAddress() != null) ? addr.getJibunAddress() : "" %>" />
				
				<!-- 상세주소 (유저 입력 가능) -->	
				<label>상세주소</label>
				<input type="text" id="addrDetail" value="<%= (addr != null && addr.getAddrDetail() != null) ? addr.getAddrDetail() : "" %>" />

				<input type="hidden" id="addrDetailValue" name="addrDetail" value="<%= (addr != null && addr.getAddrDetail() != null) ? addr.getAddrDetail() : "" %>" />
				
				<input type="hidden" id="latitude" name="latitude">
				<input type="hidden" id="longitude" name="longitude">
                
                
                <button type="submit" class="update-btn">정보 수정</button>
            </form>
          	<form action="<%= request.getContextPath() %>/user/delete" method="post" onsubmit="return confirm('정말 탈퇴하시겠습니까?');">
			    <input type="hidden" name="autoId" value="<%= user.getAutoId() %>">
			    <button type="submit" class="delete-btn">회원 탈퇴</button>
			</form>
		</div>

        <%-- 가운데: 예비 영역 --%>
        <div class="map-card" style="display:flex; justify-content:center; align-items:center; color:#666;">
            추가 통계 또는 최근 활동 영역
        </div>

        <%-- 오른쪽: 예비 영역 --%>
        <div class="weather-card" style="display:flex; justify-content:center; align-items:center; color:#666;">
            알림 및 추천 활동 영역
        </div>

    </div>
</section>

<% } else { %>
<div class="main-box" style="text-align:center;">
    <p>로그인이 필요합니다. <a href="<%= request.getContextPath() %>/views/user/login.jsp">로그인 페이지로 이동</a></p>
</div>
<% } %>

</div>
<script>
    // 카카오 Geocoder 객체 생성
    var geocoder = new kakao.maps.services.Geocoder();

    // form submit 가로채기
    document.addEventListener("DOMContentLoaded", function () {
        document.getElementById("updateForm").addEventListener("submit", function(e) {
            e.preventDefault();

            let roadAddr = document.getElementById("roadAddressValue").value;
            let jibunAddr = document.getElementById("jibunAddressValue").value;

            let finalAddress = roadAddr || jibunAddr;

            if (!finalAddress) {
                alert("주소가 없습니다. 주소 검색을 먼저 해주세요.");
                return;
            }

            // 주소 → 좌표 변환
            geocoder.addressSearch(finalAddress, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {

                    let lat = result[0].y;
                    let lng = result[0].x;

                    document.getElementById("latitude").value = lat;
                    document.getElementById("longitude").value = lng;

                    console.log("위도:", lat, "경도:", lng);

                    // 실제 제출
                    e.target.submit();

                } else {
                    alert("주소 → 좌표 변환 실패: " + status);
                }
            });
        });
    });
</script>

</body>
</html>

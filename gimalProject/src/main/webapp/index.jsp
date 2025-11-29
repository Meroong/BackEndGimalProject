<%@page import="dto.UserAddressDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="util.AuthUtil"%>
<%@ page import="dto.UserAddressDTO"%>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 우리 동네 유아·애견 커넥트</title>
    <link rel="stylesheet" href="home.css">
</head>
<body>
<div class="container">

    <%-- 헤더 --%>
	<header>
	    <div class="logo">
	        <img src="resources/images/logo.png" alt="logo">
	        도란도란
	    </div>

<div class="header-buttons">
    <%
        Object loginUser = session.getAttribute("Authorization");

        if (loginUser != null) {

            // JWT 토큰
            String token = (String) loginUser;

            // JWT에서 role 추출 (사용중인 메서드명에 맞게 수정하세요)
            String role = AuthUtil.getRole(request);
    %>

        <!-- 로그인 상태 공통: 메시지 -->
        <button class="msg-btn"
                onclick="location.href='<%= request.getContextPath() %>/chat/roomList'">
            메시지
        </button>

        <%-- ADMIN: 관리자 버튼 --%>
        <% if ("ADMIN".equals(role)) { %>

            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin'">
                관리자
            </button>

        <% } else { %>

        <%-- USER: 마이페이지 버튼 --%>
            <button class="mypage-btn"
                    onclick="location.href='<%= request.getContextPath() %>/views/user/mypage.jsp'">
                마이페이지
            </button>

        <% } %>

        <!-- 로그아웃 -->
        <form action="<%= request.getContextPath() %>/user/logout" method="get" style="display:inline;">
            <button type="submit" class="log-btn">Log out</button>
        </form>

    <% } else { %>

        <!-- 비로그인 상태 -->
        <button class="log-btn"
                onclick="location.href='views/user/login.jsp'">
            Log in
        </button>

    <% } %>
</div>


	</header>


    <%-- 검색 영역 --%>
    <section class="search-section">
        <div class="search-row">
            <select>
                <option>구로동</option>
                <option>가리봉동</option>
                <option>고척동</option>
            </select>

            <select>
                <option>모임</option>
                <option>교환</option>
                <option>드림</option>
            </select>

            <input type="text" placeholder="검색어를 입력해주세요">
            <button class="search-btn">검색</button>
        </div>

        <div class="title-main">날씨가 쌀쌀하니 겉옷 꼭 챙기세요!</div>
        <div class="title-sub">오늘의 추천활동은 실내 모임이에요 😊</div>
    </section>

    <%-- 메인 추천 영역 --%>
    <section class="main-box">
        <div class="box-title">우리 동네 기반 맞춤 추천</div>

        <div class="grid-3">
			<div class="map-card" id="map" style="width:100%; height:400px;"></div>
			
			<!-- 비로그인 시 좌표를 넣어줌 -->
			<%
			UserAddressDTO addressInfo = (UserAddressDTO) session.getAttribute("addressInfo");
			
			double defaultLat = 37.501;
			double defaultLng = 126.884;
			
			double lat = (addressInfo != null && addressInfo.getLatitude() != null)
			             ? addressInfo.getLatitude()
			             : defaultLat;
			
			double lng = (addressInfo != null && addressInfo.getLongitude() != null)
			             ? addressInfo.getLongitude()
			             : defaultLng;
			%>
			<script>
			    const userLat = <%= lat %>;
			    const userLng = <%= lng %>;
			</script>
			<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
			<script>
			    window.onload = function() {
			        if (!window.kakao) {
			            alert("카카오 지도 SDK 로드 실패");
			            return;
			        }
			
			        // 지도 생성 //
			        var container = document.getElementById('map');
			        var options = {
			            center: new kakao.maps.LatLng(userLat, userLng), // 기본 중심 좌표: 구로동 근처
			            level: 3
			        };
			        var map = new kakao.maps.Map(container, options);
			
			        // 예시: 마커 추가
			        var markerPosition  = new kakao.maps.LatLng(userLat, userLng); 
			        var marker = new kakao.maps.Marker({
			            position: markerPosition
			        });
			        marker.setMap(map);
			
			        console.log("카카오 지도 로드 완료");
			        
			    }
			</script>

            <%-- 가운데: 오늘의 인기 모임 --%>
            <div class="center-card">
                <div class="center-title">오늘의 인기 모임 🔥</div>
                <div class="center-desc">지금 우리 동네에서 가장 활발한 모임을 소개해드릴게요!</div>

                <div class="popular-grid">

                    <div class="popular-card">
                        <img src="resources/images/kidsPlay.jpg" alt="pop1">
                        <div class="pop-info">
                            <h3>주말 실내 키즈 플레이라운지</h3>
                            <p>구로 · 5명 참여중</p>
                        </div>
                    </div>

                    <div class="popular-card">
                        <img src="resources/images/dogWalking.jpg" alt="pop2">
                        <div class="pop-info">
                            <h3>강아지 소형견 산책 모임</h3>
                            <p>고척 · 3명 참여중</p>
                        </div>
                    </div>

                    <div class="popular-card">
                        <img src="resources/images/baby.jpg" alt="pop3">
                        <div class="pop-info">
                            <h3>첫 육아 부모 대화방</h3>
                            <p>가리봉 · 12명 활성</p>
                        </div>
                    </div>

                    <div class="popular-card">
                        <img src="resources/images/doggroup.jpg" alt="pop4">
                        <div class="pop-info">
                            <h3>초보 펫돌보미 공유 모임</h3>
                            <p>구로 · 7명 참여중</p>
                        </div>
                    </div>

                </div>
            </div>

            <%-- 날씨 + 활동 카드 --%>
            <div>
                <div class="weather-card">
                    <div class="weather-title">현재 구로동 날씨</div>
                    <div class="weather-temp">14.5°C</div>
                    <div class="weather-status">맑음 • 미세먼지 좋음</div>
                </div>

                <div class="activities">
                    <div class="activity-card">
                        <img src="resources/images/meeting.jpg" alt="meet">
                        <span>모임</span>
                    </div>
                    <div class="activity-card">
                        <img src="resources/images/trade.jpg" alt="friend">
                        <span>교환</span>
                    </div>
                    <div class="activity-card">
                        <img src="resources/images/giving.jpg" alt="chat">
                        <span>드림</span>
                    </div>
                </div>
            </div>

        </div>
    </section>

</div>
<button class="log-btn"
        onclick="location.href='<%=request.getContextPath()%>/admin/stats'">
    통계 보기
</button>

</body>
</html>

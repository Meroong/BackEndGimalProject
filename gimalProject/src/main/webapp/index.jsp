<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.UserAddressDTO"%>
<%@ page import="dto.WeatherDTO"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 우리 동네 유아·애견 커넥트</title>
    <link rel="stylesheet" href="home.css">
    <style>
        /* 날씨 카드 배경 적용 */
        .weather-card {
            padding: 15px;
            border-radius: 10px;
            color: white;
            text-shadow: 1px 1px 2px black;
            background-size: cover;
            background-position: center;
        }
    </style>
</head>
<body>
<div class="container">

    <%-- 헤더 include --%>
    <jsp:include page="/include/header.jsp" />

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

            <%-- 지도 카드 --%>
            <div class="map-card" id="map" style="width:100%; height:400px;"></div>

            <%
                // HomeController에서 전달된 변수
                WeatherDTO weather = (WeatherDTO) request.getAttribute("weather");
                String bgImage = (String) request.getAttribute("bgImage");

                double lat = request.getAttribute("lat") != null ? (double) request.getAttribute("lat") : 37.501;
                double lng = request.getAttribute("lng") != null ? (double) request.getAttribute("lng") : 126.884;

                String dongName = "우리 동네"; // 기본값
                UserAddressDTO addressInfo = (UserAddressDTO) session.getAttribute("addressInfo");
                if(addressInfo != null && addressInfo.getRoadAddress() != null) {
                    String[] parts = addressInfo.getRoadAddress().split(" ");
                    dongName = parts[parts.length - 1];
                }

                // 날씨 표시용
                String temp = "정보 없음";
                String status = "정보 없음";
                if(weather != null) {
                    temp = String.format("%.1f°C", weather.getTemperature());

                    String dustInfo;
                    if(weather.getPm10() <= 30) dustInfo = "좋음";
                    else if(weather.getPm10() <= 80) dustInfo = "보통";
                    else if(weather.getPm10() <= 150) dustInfo = "나쁨";
                    else dustInfo = "매우 나쁨";

                    status = weather.getWeather() + " • 미세먼지 " + dustInfo;
                }
            %>

            <script>
                const userLat = <%= lat %>;
                const userLng = <%= lng %>;
            </script>
            <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
            <script>
                window.onload = function() {
                    if (!window.kakao) { alert("카카오 지도 SDK 로드 실패"); return; }
                    var container = document.getElementById('map');
                    var options = { center: new kakao.maps.LatLng(userLat, userLng), level: 3 };
                    var map = new kakao.maps.Map(container, options);
                    var marker = new kakao.maps.Marker({ position: new kakao.maps.LatLng(userLat, userLng) });
                    marker.setMap(map);
                }
            </script>

            <%-- 가운데: 인기 모임 --%>
            <div class="center-card">
                <div class="center-title">오늘의 인기 모임 🔥</div>
                <div class="center-desc">지금 <%= dongName %>에서 가장 활발한 모임을 소개해드릴게요!</div>
            </div>

            <%-- 오른쪽: 날씨 + 활동 카드 --%>
            <div>
                <div class="weather-card" style="background-image: url('<%= bgImage %>');">
                    <div class="weather-title">현재 <%= dongName %> 날씨</div>
                    <div class="weather-temp"><%= temp %></div>
                    <div class="weather-status"><%= status %></div>
                </div>

                <div class="activities">
                    <a href="<%= request.getContextPath() %>/meeting/list" class="activity-card" style="text-decoration:none; color:inherit;">
                        <img src="resources/images/meeting.jpg" alt="meet">
                        <span>모임</span>
                    </a>

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
</body>
</html>

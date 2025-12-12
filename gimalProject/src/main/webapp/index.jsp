<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="dto.UserAddressDTO"%>
<%@ page import="dto.WeatherDTO"%>
<%@ page import="dto.MeetingInfoDTO"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>도란도란 - 우리 동네 유아·애견 커넥트</title>
    <link rel="stylesheet" href="home.css">

    <style>
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

    <%-- 헤더 --%>
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

    <%-- 메인 영역 --%>
    <section class="main-box">
        <div class="box-title">우리 동네 기반 맞춤 추천</div>

        <div class="grid-3">

            <%-- 지도 영역 --%>
            <div class="map-card" id="map" style="width:100%; height:400px;"></div>

            <%
                WeatherDTO weather = (WeatherDTO) request.getAttribute("weather");
                String bgImage = (String) request.getAttribute("bgImage");

                double lat = request.getAttribute("lat") != null ? (double) request.getAttribute("lat") : 37.501;
                double lng = request.getAttribute("lng") != null ? (double) request.getAttribute("lng") : 126.884;

                List<MeetingInfoDTO> meetings =
                    (List<MeetingInfoDTO>) request.getAttribute("meetings");

                String dongName = "우리 동네";
                UserAddressDTO addressInfo = (UserAddressDTO) session.getAttribute("addressInfo");
                if (addressInfo != null && addressInfo.getRoadAddress() != null) {
                    String[] parts = addressInfo.getRoadAddress().split(" ");
                    dongName = parts[parts.length - 1];
                }

                String temp = "정보 없음";
                String status = "정보 없음";
                if (weather != null) {
                    temp = String.format("%.1f°C", weather.getTemperature());

                    String dustInfo;
                    if (weather.getPm10() <= 30) dustInfo = "좋음";
                    else if (weather.getPm10() <= 80) dustInfo = "보통";
                    else if (weather.getPm10() <= 150) dustInfo = "나쁨";
                    else dustInfo = "매우 나쁨";

                    status = weather.getWeather() + " • 미세먼지 " + dustInfo;
                }
            %>

            <%-- 지도 데이터 JS 전달 --%>
            <script>
                const userLat = <%= lat %>;
                const userLng = <%= lng %>;

                const meetings = [];
                <%
                if (meetings != null) {
                    for (MeetingInfoDTO m : meetings) {
                        if (m.getLatitude() != null && m.getLongitude() != null) {
                %>
                meetings.push({
                    id: <%= m.getMeetingId() %>,
                    title: "<%= m.getTitle() %>",
                    lat: <%= m.getLatitude() %>,
                    lng: <%= m.getLongitude() %>
                });
                <%
                        }
                    }
                }
                %>
            </script>

            <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>

       <script>
		window.onload = function () {
		    if (!window.kakao || !kakao.maps) {
		        console.error("카카오 지도 SDK 로드 실패");
		        return;
		    }
		
		    const container = document.getElementById('map');
		
		    const map = new kakao.maps.Map(container, {
		        center: new kakao.maps.LatLng(userLat, userLng),
		        level: 4
		    });
		
		    // 내 위치 마커
		    new kakao.maps.Marker({
		        position: new kakao.maps.LatLng(userLat, userLng),
		        map: map
		    });
		
		    console.log("meetings:", meetings);
		
		    meetings.forEach(m => {
		        if (!m.lat || !m.lng) return;
		
		        const position = new kakao.maps.LatLng(m.lat, m.lng);
		
		        const marker = new kakao.maps.Marker({
		            position,
		            map
		        });
		
		        const infoWindow = new kakao.maps.InfoWindow({
		            content: `
		              <div style="
		                padding:6px 10px;
		                font-size:13px;
		                border-radius:6px;
		                background:white;
		                box-shadow:0 2px 6px rgba(0,0,0,0.3);
		              ">
		                ${m.title}
		              </div>
		            `
		        });
		
		        kakao.maps.event.addListener(marker, 'mouseover', () => {
		            infoWindow.open(map, marker);
		        });
		
		        kakao.maps.event.addListener(marker, 'mouseout', () => {
		            infoWindow.close();
		        });
		
		        kakao.maps.event.addListener(marker, 'click', () => {
		            location.href =
		              "<%= request.getContextPath() %>/meeting/info?meetingId=" + m.id;
		        });
		    });
		};
		</script>

            <%-- 가운데 카드 --%>
            <div class="center-card">
                <div class="center-title">오늘의 인기 모임 🔥</div>
                <div class="center-desc">
                    지금 <%= dongName %>에서 가장 활발한 모임을 소개해드릴게요!
                </div>
            </div>

            <%-- 오른쪽 카드 --%>
            <div>
                <div class="weather-card" style="background-image:url('<%= bgImage %>');">
                    <div class="weather-title">현재 <%= dongName %> 날씨</div>
                    <div class="weather-temp"><%= temp %></div>
                    <div class="weather-status"><%= status %></div>
                </div>

                <div class="activities">
                    <a href="<%= request.getContextPath() %>/meeting/list"
                       class="activity-card" style="text-decoration:none;color:inherit;">
                        <img src="resources/images/meeting.jpg" alt="meet">
                        <span>모임</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/dream/list.do"
                       class="activity-card" style="text-decoration:none;color:inherit;">
                        <img src="resources/images/giving.jpg" alt="dream">
                        <span>드림</span>
                    </a>
                </div>
            </div>

        </div>
    </section>
</div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.MeetingInfoDTO"%>
<%@ page import="dto.FileResourceDTO"%>
<%@ page import="java.util.List"%>

<%
	Object loginUser = session.getAttribute("userInfo");
    boolean isLogin = (loginUser != null);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>모임 상세 - 도란도란</title>
<link rel="stylesheet" href="home.css">
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b"></script>

<script>
function setMainImage(src) {
    var mainImg = document.getElementById("mainImage");
    if(mainImg) mainImg.src = src;
}

function initMap(lat, lng) {
    var container = document.getElementById('map');
    var options = { center: new kakao.maps.LatLng(lat, lng), level: 3 };
    var map = new kakao.maps.Map(container, options);
    var marker = new kakao.maps.Marker({ position: new kakao.maps.LatLng(lat, lng) });
    marker.setMap(map);
}

// 로그인 필요 시 실행되는 함수
function needLoginForJoin() {
    var currentUrl = encodeURIComponent(window.location.href);
    if (confirm("참여하시려면 로그인이 필요합니다.\n로그인 하시겠습니까?")) {
    	<%
	        String redirectUrl = request.getContextPath() 
	                            + "/meeting/info?meetingId=" 
	                            + request.getParameter("meetingId");
	
	        session.setAttribute("redirectAfterLogin", redirectUrl);
	    %>
    	location.href = "<%= request.getContextPath() %>/views/user/login.jsp";
    }
}
</script>

<style>
/* 검색창 */
.search-section { text-align:center; margin:20px 0; }
.search-row { display:flex; justify-content:center; gap:12px; }
.search-row input { padding:10px 14px; border-radius:8px; border:1px solid #DDD; width:300px; }
.search-row select { padding:10px 14px; border-radius:8px; border:1px solid #DDD; }
.search-btn { padding:10px 20px; border:none; background:#FF7C40; color:#fff; border-radius:8px; cursor:pointer; }

/* 카드 디자인 */
.card { display:flex; gap:20px; background:#FCFBFE; padding:20px; border-radius:16px; box-shadow:0 4px 16px rgba(0,0,0,0.05); margin-bottom:30px; min-height:320px; }
.card img { width:300px; height:300px; object-fit:cover; border-radius:12px; background:#f0f0f0; }
.card-content { flex:1; display:flex; flex-direction:column; justify-content:space-between; }
.card-content h2 { font-size:22px; font-weight:700; color:#FF7C40; margin:0; }
.info { font-size:16px; color:#555; line-height:1.5; }
.badge { display:inline-block; background:#FF7C40; color:#fff; padding:4px 8px; border-radius:8px; font-size:14px; margin-right:6px; }
.hashtags { font-size:14px; color:#888; margin-top:6px; }

/* 버튼 */
.btn { padding:10px 18px; background:#FF7C40; color:#fff; border:none; border-radius:10px; cursor:pointer; font-size:16px; font-weight:600; transition:0.2s; text-align:center; }
.btn:hover { background:#e66a2f; transform:translateY(-2px); }

/* 썸네일 */
.thumbnail-grid { display:flex; gap:10px; margin-top:10px; }
.thumbnail-grid img { width:60px; height:60px; object-fit:cover; border-radius:6px; border:2px solid #ccc; cursor:pointer; }
.thumbnail-grid img:hover { border-color:#FF7C40; }

/* 모집인원 배지 */
.recruit-badge { position:absolute; top:10px; left:10px; background:#FF7C40; color:#fff; padding:6px 10px; border-radius:12px; font-weight:600; font-size:14px; }

/* 지도 */
#map { width:100%; height:400px; border-radius:16px; margin-top:20px; }
</style>
</head>

<body>
<div class="container">

    <jsp:include page="/include/header.jsp" />

    <!-- 검색창 -->
    <section class="search-section">
        <div class="search-row">
            <select>
                <option>동네 선택</option>
                <option>구로동</option>
                <option>가리봉동</option>
                <option>고척동</option>
            </select>
            <input type="text" placeholder="검색어를 입력해주세요">
            <button class="search-btn">검색</button>
        </div>
    </section>

<%
    MeetingInfoDTO m = (MeetingInfoDTO) request.getAttribute("meetingInfo");
    Boolean isCreator = (Boolean) request.getAttribute("isCreator");
    Boolean isParticipant = (Boolean) request.getAttribute("isParticipant"); 
%>

<% if(m == null) { %>
    <p>존재하지 않는 모임입니다.</p>
<% } else { %>

<div style="position:relative;">
    <div class="recruit-badge">모집인원 <%= m.getCurrentMembers() %>/<%= m.getMaxMembers() %></div>

    <div class="card">
	<div>
	    <!-- 대표 이미지 -->
	    <img id="mainImage"
	         src="<%= (m.getImages() != null && !m.getImages().isEmpty())
	                ? m.getImages().get(0).getFileUrl()
	                : "/resources/images/default.png"
	         %>"
	         style="width:300px; height:300px; object-fit:cover; border-radius:12px; background:#f0f0f0;" />
	
	    <!-- 썸네일 -->
	    <% if (m.getImages() != null && m.getImages().size() > 1) { %>
	        <div class="thumbnail-grid">
	            <% for (FileResourceDTO img : m.getImages()) {
	                   if (img != null && img.getFileUrl() != null && !img.getFileUrl().trim().isEmpty()) { %>
	
	                <img src="<%= img.getFileUrl() %>"
	                     onclick="setMainImage('<%= img.getFileUrl() %>')"
	                     style="cursor:pointer;" />
	
	            <% }} %>
	        </div>
	    <% } %>
	</div>

        <div class="card-content">
            <div>
                <h2><%= m.getTitle() %></h2>
                <p class="info"><strong>설명:</strong> <%= m.getContent() %></p>
                <p class="info"><strong>날짜:</strong> <%= m.getDate() %></p>
                <p class="info">
                    <span class="badge">최대 <%= m.getMaxMembers() %>명</span>
                    <span class="badge">현재 <%= m.getCurrentMembers() %>명</span>
                    <span class="badge">참가비: <%= m.getCost() %>원</span>
                </p>
                <p class="info"><strong>주소:</strong> <%= m.getRoadAddress() %> / <%= m.getAddrDetail() %></p>

                <p class="hashtags">
                <% if(m.getTag()!=null){ 
                       for(String tag : m.getTag().split("#")){ 
                           if(!tag.trim().isEmpty()){ %>
                    #<%= tag %>
                <% }}} %>
                </p>
            </div>

            <div style="margin-top:10px;">

                <%-- 로그인하지 않은 경우 → 참여 버튼을 누르면 로그인 후 복귀 --%>
                <% if (!isLogin) { %>
                    <button class="btn" onclick="needLoginForJoin()">참여하기</button>
                <% } else { %>

                    <%-- 로그인된 상태에서: 참여/나가기/수정 표시 --%>

                    <% if(!Boolean.TRUE.equals(isParticipant) && !Boolean.TRUE.equals(isCreator)) { %>
                        <form action="<%= request.getContextPath() %>/meeting/join" method="post" style="display:inline;">
                            <input type="hidden" name="meetingId" value="<%= m.getMeetingId() %>">
                            <button class="btn">참여하기</button>
                        </form>
                    <% } %>

                    <% if(Boolean.TRUE.equals(isParticipant) && !Boolean.TRUE.equals(isCreator)) { %>
                        <form action="<%= request.getContextPath() %>/meeting/quit" method="post" style="display:inline; margin-left:10px;">
                            <input type="hidden" name="meetingId" value="<%= m.getMeetingId() %>">
                            <button class="btn" style="background:#555;">모임 나가기</button>
                        </form>
                    <% } %>

                    <% if(Boolean.TRUE.equals(isCreator)) { %>
                        <a class="btn" style="background:#5271FF; margin-left:10px;"
                           href="<%= request.getContextPath() %>/meeting/edit?meetingId=<%= m.getMeetingId() %>">
                           모임 수정
                        </a>
                    <% } %>

                <% } %>

            </div>

        </div>
    </div>
</div>

<div id="map"></div>
<script>
    <% if(m.getLatitude()!=null && m.getLongitude()!=null){ %>
        initMap(<%= m.getLatitude() %>, <%= m.getLongitude() %>);
    <% } %>
</script>

<a href="<%= request.getContextPath() %>/meeting/list" 
   style="display:inline-block; margin-top:20px; color:#5271FF; font-weight:600;">
   목록으로 돌아가기
</a>

<% } %>
</div>
</body>
</html>

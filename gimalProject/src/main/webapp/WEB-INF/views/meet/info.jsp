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
const IS_LOGIN = <%= isLogin ? "true" : "false" %>;
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
function goLoginWithRedirect() {
  const redirectUrl = encodeURIComponent(
    "<%= request.getContextPath() %>" + location.pathname + location.search
  );
  location.href = "<%= request.getContextPath() %>/page/login?redirect=" + redirectUrl;
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
	
	.report-btn {
	    position: absolute;
	    top: 20px;
	    right: 20px;
	    background: #FF4E4E;
	    color: white;
	    padding: 8px 14px;
	    border-radius: 10px;
	    font-size: 14px;
	    font-weight: 600;
	    border: none;
	    cursor: pointer;
	    transition: 0.2s;
	}
	
	.report-btn:hover {
	    background: #d93c3c;
	    transform: translateY(-2px);
	}
	
	.card-wrapper {
	    position: relative;
	}
	/* 태그 표시용 (상세페이지) */
	.tag-view-area {
	    display: flex;
	    flex-wrap: wrap;
	    gap: 8px;
	    margin-top: 6px;
	}
	
	.tag-view {
	    padding: 6px 12px;
	    border-radius: 20px;
	    border: 1px solid #FF7C40;
	    background: #FFF4EE;
	    color: #FF7C40;
	    font-size: 13px;
	    font-weight: 500;
	}
		header {
    position: relative;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
}

/* 가운데 네비게이션 */
.header-nav {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    gap: 24px;
}

.header-nav button {
    background: none;
    border: none;
    font-size: 16px;
    font-weight: 700;
    color: #333;
    cursor: pointer;
    padding: 6px 10px;
    border-radius: 8px;
    transition: 0.2s;
}

.header-nav button:hover {
    background: #FFF1E8;
    color: #FF6600;
}
header {
    position: relative;
    display: flex;
    align-items: center;
    height: 72px;              /* 🔥 기준 높이 */
    padding: 0 20px;
}
.logo {
    height: 100%;
    display: flex;
    align-items: center;
}

.logo img {
    height: 42px;              /* 🔥 header 안에서 적당한 크기 */
    width: auto;
    object-fit: contain;
}
.header-nav {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    height: 100%;
    display: flex;
    align-items: center;
    gap: 24px;
}

.header-nav button {
    height: 40px;
    line-height: 40px;        /* 🔥 수직 정렬 핵심 */
    padding: 0 14px;
}
.header-buttons {
    height: 100%;
    display: flex;
    align-items: center;
    gap: 10px;
}

.header-buttons button {
    height: 40px;
    line-height: 40px;
    padding: 0 18px;
}
.header-spacer {
    height: 80px;
}
.header-spacer1 {
    height: 50px;
}
<div class="header-spacer"></div>
</style>
</head>

<body>
	<jsp:include page="/WEB-INF/views/include/header.jsp" />	
	<div class="header-spacer1"></div>
    <jsp:include page="/WEB-INF/views/include/searchBar.jsp">
	    <jsp:param name="mode" value="home"/>
	</jsp:include>
	<div class="header-spacer"></div>
<div class="container">


<%
    MeetingInfoDTO m = (MeetingInfoDTO) request.getAttribute("meetingInfo");
    Boolean isCreator = (Boolean) request.getAttribute("isCreator");
    Boolean isParticipant = (Boolean) request.getAttribute("isParticipant"); 
%>

<% if(m == null) { %>
    <p>존재하지 않는 모임입니다.</p>
<% } else { %>

<div style="position:relative;">

	<div class="card-wrapper">

    <!-- 신고 버튼 (오른쪽 상단 배치) -->
	<%
	boolean hasReported = (Boolean) request.getAttribute("hasReported");
	boolean isCreatorBool = (Boolean.TRUE.equals(isCreator)); 
	%>
	
	<!-- 1. 게시자는 모임삭제 버튼 -->
	<% if (isCreatorBool) { %>
	
	    <form action="<%= request.getContextPath() %>/meeting/delete" method="post" style="display:inline;">
	        <input type="hidden" name="meetingId" value="<%= m.getMeetingId() %>">
	        <button type="submit" class="report-btn" style="background:#444;">모임 삭제</button>
	    </form>
	
	<!-- 2. 이미 신고한 유저 -->
	<% } else if (hasReported) { %>
	
	    <div class="report-btn" style="background:#ccc; cursor:default;">
	        신고 완료
	    </div>
	
	<!-- 3. 신고 가능 -->
	<% } else { %>
	
		<button type="button" class="report-btn"
		        onclick="handleReportClick(
		            'MEETING',
		            '<%= m.getCreatorId() %>',
		            '<%= m.getMeetingId() %>'
		        )">
		    신고하기
		</button>

	
	<% } %>
	
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

			<div class="tag-view-area">
			<%
			    if (m.getTag() != null && !m.getTag().isBlank()) {
			        for (String tag : m.getTag().split(",")) {
			            String t = tag.trim();
			            if (!t.isEmpty()) {
			%>
			    <span class="tag-view">#<%= t %></span>
			<%
			            }
			        }
			    }
			%>
			</div>
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

                    <% if(Boolean.TRUE.equals(isParticipant) && !Boolean.TRUE.equals(isCreator)) { System.out.println(m.getStatus()); %>
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
                        
					    <% if ("OPEN".equals(m.getStatus())) { %>
					        <!-- 모집 마감 -->
					        <form action="<%= request.getContextPath() %>/meeting/status" method="post" style="display:inline;">
					            <input type="hidden" name="meetingId" value="<%= m.getMeetingId() %>">
					            <input type="hidden" name="status" value="CLOSED">
					            <button type="submit" class="btn" style="background:#555;">
					                모집 마감
					            </button>
					        </form>
					
					    <% } else if ("CLOSED".equals(m.getStatus())) { %>
					        <!-- 모집 재개 -->
					        <form action="<%= request.getContextPath() %>/meeting/status" method="post" style="display:inline;">
					            <input type="hidden" name="meetingId" value="<%= m.getMeetingId() %>">
					            <input type="hidden" name="status" value="OPEN">
					            <button type="submit" class="btn" style="background:#2ecc71;">
					                모집 재개
					            </button>
					        </form>
					    <% } %>
                    <% } %>

                <% } %>

            </div>

        </div>
    </div>
</div><button type="button" class="report-btn"
        onclick="openReportModal(
            'MEETING',
            '<%= m.getCreatorId() %>',
            '<%= m.getMeetingId() %>'
        )">
    신고하기
</button>

<div id="map"></div>
<script>
    <% if(m.getLatitude()!=null && m.getLongitude()!=null){ %>
        initMap(<%= m.getLatitude() %>, <%= m.getLongitude() %>);
    <% } %>
</script>
<script>
function handleReportClick(type, targetUserId, targetId) {

    if (!IS_LOGIN) {
        goLoginWithRedirect();
        return;
    }

    // reportModal.jsp 에서 사용하는 hidden input 세팅
    document.getElementById("reportType").value = type;
    document.getElementById("targetUserId").value = targetUserId;
    document.getElementById("targetId").value = targetId;

    // 모달 열기
    document.getElementById("reportModal").style.display = "block";
}
</script>

<% } %>
</div>
<jsp:include page="/WEB-INF/views/include/reportModal.jsp" />
</body>
</html>

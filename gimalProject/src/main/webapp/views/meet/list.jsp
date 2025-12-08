<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dto.MeetingDTO" %>

<%
    // 로그인 여부 체크
    Object loginUser = session.getAttribute("loginUser");
    boolean isLogin = (loginUser != null);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>동네 모임</title>

    <!-- 메인 스타일 가져오기 -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/home.css">

    <style>
        body {
            background: #F5F6FA;
            font-family: 'Pretendard';
        }

        .page-wrapper {
            width: 1400px;
            margin: 0 auto;
            padding: 20px 0;
        }

        .layout {
            display: flex;
            gap: 30px;
        }

        /* ---------- 좌측 사이드바 ---------- */
        .sidebar {
            width: 260px;
            background: white;
            padding: 25px 20px;
            border-radius: 24px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.06);
            height: auto;
        }

        .side-title {
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 12px;
        }

        .filter-group {
            margin-bottom: 28px;
        }

        .filter-group ul li {
            list-style: none;
            margin: 8px 0;
            cursor: pointer;
        }

        /* ---------- 우측 콘텐츠 ---------- */
        .content {
            flex: 1;
        }

        .content-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .search-box input {
            width: 350px;
            height: 42px;
            padding: 0 15px;
            border-radius: 12px;
            border: 1px solid #ccc;
        }

        .write-btn {
            background: #FF7C40;
            color: white;
            padding: 10px 18px;
            border-radius: 12px;
            font-size: 14px;
            font-weight: 700;
            text-decoration: none;
            transition: 0.2s;
        }

        .write-btn:hover {
            background: #e46d33;
        }

        /* ---------- 모임 리스트 카드 ---------- */
        .meeting-card {
            background: white;
            padding: 22px 25px;
            margin-bottom: 18px;
            border-radius: 20px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.07);
            cursor: pointer;
            transition: 0.2s;
        }
        .meeting-card:hover {
            transform: translateY(-4px);
        }

        .meeting-title {
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 6px;
        }
        .meeting-content {
            font-size: 14px;
            color: #666;
            margin-bottom: 10px;
        }
        .meeting-meta {
            font-size: 12px;
            color: #888;
        }

        .status-btn {
            float: right;
            font-size: 13px;
            padding: 5px 12px;
            border-radius: 8px;
            color: white;
            background: #FF7C40;
        }
    </style>

	<script>
	function needLogin() {
	    var currentUrl = window.location.href;
	
	    if(confirm('로그인이 필요합니다.')) {
	        // JSP에서 세션에 저장
	        <%-- JS → JSP 변수 전달 --%>
	        <% session.setAttribute("redirectAfterLogin", request.getContextPath().toString()+"/meeting/list"); %>
	
	        location.href = "<%= request.getContextPath() %>/views/user/login.jsp";
	    }
	}
	</script>

</head>
<body>

<div class="page-wrapper">

    <!-- 공통 헤더 -->
    <jsp:include page="/include/header.jsp" />

    <div class="layout">

        <!-- ---------- 좌측 필터 사이드바 ---------- -->
        <div class="sidebar">
            <div class="filter-group">
                <div class="side-title">카테고리</div>
                <ul>
                    <li>전체</li>
                    <li>산책</li>
                    <li>헬스</li>
                    <li>애견</li>
                </ul>
            </div>

            <div class="filter-group">
                <div class="side-title">모임 인원</div>
                <ul>
                    <li>1명 이상</li>
                    <li>최대 10명</li>
                </ul>
            </div>

            <div class="filter-group">
                <div class="side-title">필터</div>
                <input type="date" style="width:100%; padding:8px; border-radius:12px; border:1px solid #ddd;">
            </div>
        </div>

        <!-- ---------- 우측 콘텐츠 ---------- -->
        <div class="content">

            <div class="content-header">
                <!-- 검색 -->
                <div class="search-box">
                    <input type="text" placeholder="Search Product Here">
                </div>

                <!-- 모임 생성 버튼 (로그인 여부에 따라 다르게) -->
                <% if (isLogin) { %>
                    <a href="<%= request.getContextPath() %>/views/meet/meetForm.jsp" class="write-btn">모임 생성 ✏️</a>
                <% } else { %>
                    <a href="#" class="write-btn" onclick="needLogin()">모임 생성 ✏️</a>
                <% } %>
            </div>

            <!-- ====== 모임 리스트 출력 ====== -->
            <%
                List<MeetingDTO> list = (List<MeetingDTO>) request.getAttribute("meetingList");
                if (list == null || list.isEmpty()) {
            %>
                <p style="color:#555; font-size:16px;">등록된 모임이 없습니다.</p>

            <% } else {
                   for (MeetingDTO m : list) { %>

            <div class="meeting-card"
                 onclick="location.href='<%= request.getContextPath() %>/meeting/info?meetingId=<%= m.getMeetingId() %>'">

                <div class="meeting-title"><%= m.getTitle() %></div>
                <div class="meeting-content"><%= m.getContent() %></div>

                <div class="meeting-meta">
                    <%= m.getDate() %>
                    <span class="status-btn">모집인원</span>
                </div>

            </div>

            <% } } %>

        </div>

    </div>

</div>

</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dto.MeetingInfoDTO" %>


<%
    Object loginUser = session.getAttribute("userInfo");
    boolean isLogin = (loginUser != null);

    String selectedCategory = (String) request.getAttribute("selectedCategory");
    String selectedDateFrom = (String) request.getAttribute("selectedDateFrom");
    String selectedDateTo   = (String) request.getAttribute("selectedDateTo");
    String keyword          = (String) request.getAttribute("keyword");
    String selectedStatus   = (String) request.getAttribute("selectedStatus");
    String selectedWeather  = (String) request.getAttribute("selectedWeather");

    if (selectedCategory == null || selectedCategory.isBlank()) selectedCategory = "전체";
    if (selectedDateFrom == null) selectedDateFrom = "";
    if (selectedDateTo   == null) selectedDateTo   = "";
    if (keyword == null) keyword = "";
    if (selectedStatus == null || selectedStatus.isBlank()) selectedStatus = "ALL";
    if (selectedWeather == null || selectedWeather.isBlank()) selectedWeather = "ALL";

    // ✅ 태그 카테고리 목록
    String[] tagCategories = {
        "운동","육아","산책","조깅","러닝",
        "반려견","반려묘","카페","스터디",
        "독서","취미","여행","사진","게임"
    };
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>동네 모임</title>

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
        .filter-group ul {
            padding-left: 0;
            margin: 0;
        }
        .filter-group ul li {
            list-style: none;
            margin: 8px 0;
        }
        .filter-label {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            color: #444;
            cursor: pointer;
        }
        .filter-label input[type="radio"] {
            accent-color: #FF7C40;
        }

        .content {
            flex: 1;
        }
		.content-header {
		    display: flex;
		    justify-content: space-between;
		    align-items: center;
		    margin-bottom: 20px;
		     margin-left: 330px;
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

		/* 카드 */
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
		
		/* 제목 */
		.meeting-title {
		    font-size: 18px;
		    font-weight: 700;
		    margin-bottom: 6px;
		    color: #222;
		}
		
		/* 내용 요약 */
		.meeting-content {
		    font-size: 14px;
		    color: #666;
		    margin-bottom: 10px;
		}
		
		/* ========= 날짜 + 동네 (1줄) ========= */
		.meeting-meta-top {
		    font-size: 13px;
		    color: #555;
		    margin-bottom: 6px;
		    display: flex;
		    align-items: center;
		    gap: 6px;
		}
		
		/* ========= 조회수 + 몇분전 + 모집인원 버튼 (2줄) ========= */
		.meeting-meta-bottom {
		    font-size: 12px;
		    color: #777;
		    display: flex;
		    align-items: center;
		    gap: 6px;
		}
		
		/* 조회수 */
		.view-wrapper {
		    display: flex;
		    align-items: center;
		}
		
		.view-icon {
		    width: 16px;
		    height: 16px;
		    opacity: 0.7;
		    margin-right: 4px;
		}
		
		.view-count {
		    color: #666;
		    font-size: 12px;
		}
		
		/* 몇분전 */
		.time-ago {
		    color: #777;
		}
		
		/* 오른쪽 배치되는 버튼 */
		.status-btn {
		    margin-left: auto;
		    font-size: 13px;
		    padding: 5px 12px;
		    border-radius: 8px;
		    color: white;
		    background: #FF7C40;
		    font-weight: 600;
		}
				/* 모집 상태 뱃지 */
		.status-badge {
		    padding: 4px 10px;
		    border-radius: 8px;
		    font-size: 12px;
		    font-weight: 700;
		    color: #fff;
		}
		
		.status-badge.open {
		    background: #FF7C40;
		}
		
		.status-badge.closed {
		    background: #999;
		}
		
		/* 참여 버튼 */
		.join-btn {
		    background: #FF7C40;
		    border: none;
		    color: #fff;
		    padding: 6px 14px;
		    border-radius: 8px;
		    font-size: 13px;
		    font-weight: 700;
		    cursor: pointer;
		}
		
		.join-btn:hover {
		    background: #e46d33;
		}
		
		/* 인원 표시 */
		.member-count {
		    font-size: 12px;
		    color: #555;
		    font-weight: 600;
		}
        .filter-submit {
            margin-bottom: 16px;
            text-align: right;
        }
        .filter-submit button {
            background: #FF7C40;
            color: #fff;
            padding: 8px 16px;
            border-radius: 10px;
            border: none;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
        }
        .filter-submit button:hover {
            background: #e46d33;
        }

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
	    if(confirm('로그인이 필요합니다.')) {
	        <% session.setAttribute("redirectAfterLogin", request.getContextPath().toString()+"/meeting/list"); %>
	        location.href = "<%= request.getContextPath() %>/views/user/login.jsp";
	    }
	}
	</script>
<script>
    function autoSubmitFilter() {
        document.getElementById('filterForm').submit();
    }
</script>

</head>
<body>

<div class="page-wrapper">

    <jsp:include page="/include/header.jsp" />
    
                    <div class="content-header">
                    <!-- 검색어 -->
                    <div class="search-box">
                        <input type="text"
                               name="keyword"
                               placeholder="검색어를 입력하세요"
                               value="<%= keyword %>">
                    </div>

                    <!-- 모임 생성 버튼 -->
                    <% if (isLogin) { %>
                        <a href="<%= request.getContextPath() %>/views/meet/meetForm.jsp" class="write-btn">모임 생성 ✏️</a>
                    <% } else { %>
                        <a href="#" class="write-btn" onclick="needLogin()">모임 생성 ✏️</a>
                    <% } %>
                </div>

    <!-- 전체 필터 + 검색을 하나의 GET 폼으로 -->
    <form id="filterForm" method="get" action="<%= request.getContextPath() %>/meeting/list">
        <div class="layout">

            <!-- 좌측 필터 사이드바 -->
            <div class="sidebar">
				<!-- 카테고리 (태그 기반) -->
				<div class="filter-group">
				<div class="side-title">카테고리</div>
				<ul>
				<li>
				<label class="filter-label">
				<input type="radio" name="category" value="전체"
				       onchange="autoSubmitFilter()"
				       <%= "전체".equals(selectedCategory)?"checked":"" %>>
				전체
				</label>
				</li>
				
				<% for(String tag : tagCategories) { %>
				<li>
				<label class="filter-label">
				<input type="radio" name="category" value="<%= tag %>"
				       onchange="autoSubmitFilter()"
				       <%= tag.equals(selectedCategory)?"checked":"" %>>
				<%= tag %>
				</label>
				</li>
				<% } %>
				</ul>
				</div>

                <!-- 모집 상태 필터 (모집중/마감/종료) -->
                <div class="filter-group">
                    <div class="side-title">모집 상태</div>
                    <ul>
                        <li>
                            <label class="filter-label">
                                <input type="radio" name="status" value="ALL"
     							  onchange="autoSubmitFilter()"
     							  <%= "ALL".equals(selectedStatus) ? "checked" : "" %>>

                                전체
                            </label>
                        </li>
                        <li>
                            <label class="filter-label">
                                <input type="radio" name="status" value="OPEN"
                                onchange="autoSubmitFilter()"
                                <%= "OPEN".equals(selectedStatus) ? "checked" : "" %>>
                                모집중
                            </label>
                        </li>
                        <li>
                            <label class="filter-label">
                                <input type="radio" name="status" value="CLOSED"
                                		onchange="autoSubmitFilter()"
                                       <%= "CLOSED".equals(selectedStatus) ? "checked" : "" %>>
                                모집 마감
                            </label>
                        </li>
                        
                </div>

                <!-- 날짜 필터 -->
                <div class="filter-group">
                    <div class="filter-group">
    <div class="side-title">모임 날짜</div>
    		<div style="display:flex; align-items:center; gap:6px;">
       			 <input type="date"
          			    name="dateFrom"
           	  		    value="<%= selectedDateFrom %>"
           	  		    onchange="autoSubmitFilter()"
               		    style="flex:1; padding:8px; border-radius:12px; border:1px solid #ddd;">
	
        <span style="font-size:14px; color:#666;">~</span>

        <input type="date"
               name="dateTo"
               value="<%= selectedDateTo %>"
               onchange="autoSubmitFilter()"
               style="flex:1; padding:8px; border-radius:12px; border:1px solid #ddd;">
    </div>
    <!-- <div style="margin-top:6px; font-size:11px; color:#888;">
        시작일 또는 종료일만 선택해도 필터됩니다.
    </div> 해도 되고 안해도 되고 -->
</div>

                </div>

                <!-- 날씨 필터 -->
                <div class="filter-group">
                    <div class="side-title">날씨</div>
                    <select name="weather"
                    		onchange="autoSubmitFilter()"
                            style="width:100%; padding:8px; border-radius:12px; border:1px solid #ddd;">
                        <option value="ALL" <%= "ALL".equals(selectedWeather) ? "selected" : "" %>>전체</option>
                        <option value="맑음" <%= "맑음".equals(selectedWeather) ? "selected" : "" %>>맑음</option>
                        <option value="흐림" <%= "흐림".equals(selectedWeather) ? "selected" : "" %>>흐림</option>
                        <option value="비" <%= "비".equals(selectedWeather) ? "selected" : "" %>>비</option>
                        <option value="이슬비" <%= "이슬비".equals(selectedWeather) ? "selected" : "" %>>이슬비</option>
                        <option value="천둥번개" <%= "천둥번개".equals(selectedWeather) ? "selected" : "" %>>천둥번개</option>
                        <option value="눈" <%= "눈".equals(selectedWeather) ? "selected" : "" %>>눈</option>
                        <option value="기타" <%= "기타".equals(selectedWeather) ? "selected" : "" %>>기타</option>
                    </select>
                </div>
            </div>
            
            <!-- 우측 콘텐츠 -->
            <div class="content">
              
                <!-- ====== 모임 리스트 출력 ====== -->
			<%
			    List<MeetingInfoDTO> list = (List<MeetingInfoDTO>) request.getAttribute("meetingList");
			    if (list == null || list.isEmpty()) {
			%>
			    <p style="color:#555; font-size:16px;">등록된 모임이 없습니다.</p>
			
			<% } else {
			       for (MeetingInfoDTO m : list) { %>
			
			<div class="meeting-card"
			     onclick="location.href='<%= request.getContextPath() %>/meeting/info?meetingId=<%= m.getMeetingId() %>'">
			
			    <div class="meeting-title"><%= m.getTitle() %></div>
			    <div class="meeting-content"><%= m.getContent() %></div>
			
			    <div class="meeting-meta-top">
				    <span class="meet-date"><%= m.getDateStr() %></span>
				    <span class="meet-dong"><%= m.getDongName() %></span>
				</div>
				
				<div class="meeting-meta-bottom">

				    <!-- 조회수 -->
				    <span class="view-wrapper">
				        <img src="https://cdn-icons-png.flaticon.com/512/709/709612.png" class="view-icon">
				        <span class="view-count"><%= m.getViewCount() %></span>
				    </span>
				
				    <!-- 몇분전 -->
				    <span class="time-ago"><%= m.getTimeAgo() %></span>
				
				    <!-- 모집 인원 -->
				    <span class="member-count">
				        <%= m.getCurrentMembers() %> / <%= m.getMaxMembers() %>명
				    </span>
				
				    <!-- 상태 뱃지 -->
				    <% if ("OPEN".equals(m.getStatus())) { %>
				        <span class="status-badge open">모집중</span>
				    <% } else { %>
				        <span class="status-badge closed">마감</span>
				    <% } %>
				
				    <!-- 참여 버튼 (🔥 조건 엄격) -->
				    <% if (
				            isLogin
				            && "OPEN".equals(m.getStatus())
				            && !m.isCreator()      /* 👉 DTO에 creator 여부 boolean 있으면 최고 */
				            && !m.isParticipant()  /* 👉 참여 여부 */
				        ) { %>
				
				        <form action="<%= request.getContextPath() %>/meeting/join"
				              method="post"
				              style="margin-left:auto;">
				            <input type="hidden" name="meetingId" value="<%= m.getMeetingId() %>">
				            <button type="submit" class="join-btn">참여하기</button>
				        </form>
				
				    <% } %>
				
				</div>


			
			</div>
			
			<% } } %>

            </div>
        </div>
    </form>

</div>

</body>
<script>
window.onpageshow = function(event) {
    if (event.persisted) {
        // 캐시에서 복원된 경우 → 강제 새로고침
        location.reload();
    }
};
</script>
</html>

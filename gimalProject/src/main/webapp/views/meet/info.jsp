<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dto.MeetingInfoDTO" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>모임 상세</title>
</head>
<body>

<h1>📌 모임 상세 정보</h1>

<%
    MeetingInfoDTO m = (MeetingInfoDTO) request.getAttribute("meetingInfo");
%>

<% if (m == null) { %>
    <p>존재하지 않는 모임입니다.</p>
<% } else { %>

<h2><%= m.getTitle() %></h2>
<p><strong>설명:</strong> <%= m.getContent() %></p>
<p><strong>날짜:</strong> <%= m.getDate() %></p>

<h3>모집 정보</h3>
<ul>
    <li>최대 인원: <%= m.getMaxMembers() %></li>
    <li>현재 인원: <%= m.getCurrentMembers() %></li>
    <li>참가비: <%= m.getCost() %> 원</li>
    <li>태그: <%= m.getTag() %></li>
    <li>상태: <%= m.getStatus() %></li>
</ul>

<h3>주소</h3>
<ul>
    <li>도로명 주소: <%= m.getRoadAddress() %></li>
    <li>지번 주소: <%= m.getJibunAddress() %></li>
    <li>상세 주소: <%= m.getAddrDetail() %></li>
    <li>위도: <%= m.getLatitude() %></li>
    <li>경도: <%= m.getLongitude() %></li>
</ul>

<h3>날씨</h3>
<p><%= m.getWeather() %></p>

<h3>이미지들</h3>

<%
    List<String> imgs = m.getImages();
    if (imgs == null || imgs.isEmpty()) {
%>
    <p>등록된 이미지가 없습니다.</p>
<%
    } else {
        for (String url : imgs) {
%>
            <img src="<%= url %>" width="200" style="margin-right:10px;" />
<%
        }
    }
%>

<% } %>

<br><br>
<a href="<%= request.getContextPath() %>/meet/list">목록으로 돌아가기</a>

</body>
</html>

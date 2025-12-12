<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- include/sidebar.jsp (fragment) -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/common.css">

<aside class="panel panel--padded sticky" aria-label="사이드 메뉴">
  <h2 class="sidebar__title">빠른 메뉴</h2>

  <div class="sidebar__section">
    <p class="sidebar__section-title">둘러보기</p>
    <div class="menu">
      <a class="menu__item" href="<%= request.getContextPath() %>/index.jsp">홈</a>
      <a class="menu__item" href="<%= request.getContextPath() %>/meeting/list">모임</a>
      <a class="menu__item" href="<%= request.getContextPath() %>/dream/list.do">드림</a>
      <a class="menu__item" href="<%= request.getContextPath() %>/chat/roomList">메시지</a>
    </div>
  </div>

  <div class="sidebar__section">
    <p class="sidebar__section-title">도움말</p>
    <div class="menu">
      <a class="menu__item" href="<%= request.getContextPath() %>/error.jsp">오류 페이지</a>
    </div>
  </div>
</aside>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="util.AuthUtil"%>

<!-- NOTE:
  - common.css를 아직 모든 페이지 head에서 로드하지 않아서, include/header.jsp에서 한 번 더 로드합니다.
  - 전체 JSP에 공통 적용이 끝나면(모든 페이지 head에 common.css 추가 후) 아래 link는 제거해도 됩니다.
-->
<link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/common.css">

<header class="app-header">
  <div class="container app-header__inner">

    <!-- Brand -->
    <a class="app-brand" href="<%= request.getContextPath() %>/index.jsp" aria-label="홈으로 이동">
      <img class="app-brand__logo" src="<%= request.getContextPath() %>/resources/images/logo.png" alt="도란도란 로고">
      <span class="app-brand__name">도란도란</span>
    </a>

    <!-- Primary Nav (필요 시 항목 추가 가능) -->
    <nav class="app-nav" aria-label="주요 메뉴">
      <a class="app-nav__link" href="<%= request.getContextPath() %>/index.jsp">홈</a>
      <a class="app-nav__link" href="<%= request.getContextPath() %>/meeting/list">모임</a>
      <a class="app-nav__link" href="<%= request.getContextPath() %>/dream/list.do">드림</a>
    </nav>

    <!-- Actions -->
    <div class="app-actions">
      <%
        Object loginUser = session.getAttribute("Authorization");
        if (loginUser != null) {
          String role = AuthUtil.getRole(request);
      %>

        <button class="btn btn--primary msg-btn"
                type="button"
                onclick="location.href='<%= request.getContextPath() %>/chat/roomList'">
          메시지
        </button>

        <% if ("ADMIN".equals(role)) { %>
          <button class="btn btn--outline log-btn"
                  type="button"
                  onclick="location.href='<%= request.getContextPath() %>/admin'">
            관리자
          </button>
        <% } else { %>
          <button class="btn btn--dark mypage-btn"
                  type="button"
                  onclick="location.href='<%= request.getContextPath() %>/views/user/mypage.jsp'">
            마이페이지
          </button>
        <% } %>

        <form action="<%= request.getContextPath() %>/user/logout" method="get" style="display:inline;">
          <button type="submit" class="btn btn--outline log-btn">Log out</button>
        </form>

      <% } else { %>

        <button class="btn btn--outline log-btn"
                type="button"
                onclick="location.href='<%= request.getContextPath() %>/views/user/login.jsp'">
          Log in
        </button>

      <% } %>
    </div>

  </div>
</header>

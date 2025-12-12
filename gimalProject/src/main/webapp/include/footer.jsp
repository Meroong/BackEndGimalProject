<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- include/footer.jsp (fragment) -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/common.css">

<footer class="app-footer">
  <div class="container app-footer__inner">
    <div>
      <span style="font-weight:700; color:#374151;">도란도란</span>
      <span style="margin-left:6px;">· 우리 동네 유아·애견 커넥트</span>
      <span style="margin-left:8px; color:#9ca3af;">© <span id="footerYear"></span></span>
    </div>

    <div class="app-footer__links">
      <a class="app-nav__link" href="<%= request.getContextPath() %>/index.jsp">홈</a>
      <a class="app-nav__link" href="<%= request.getContextPath() %>/meeting/list">모임</a>
      <a class="app-nav__link" href="<%= request.getContextPath() %>/dream/list.do">드림</a>
    </div>
  </div>
</footer>

<script>
  (function() {
    var el = document.getElementById("footerYear");
    if (el) el.textContent = new Date().getFullYear();
  })();
</script>

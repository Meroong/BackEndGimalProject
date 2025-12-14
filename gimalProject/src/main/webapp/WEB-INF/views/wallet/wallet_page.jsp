<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String roomId = request.getParameter("roomId");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>포인트 지갑</title>

    <!-- ⭐ wallet 전용 CSS -->
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/resources/css/wallet.css">
</head>
<body>

<div class="wallet-page">

    <!-- 뒤로가기 -->
    <div style="margin-bottom:12px;">
        <button onclick="goBack()" style="
            border:none;
            background:none;
            font-size:16px;
            cursor:pointer;
        ">← 뒤로가기</button>
    </div>

    <div class="wallet-card">
        <jsp:include page="/WEB-INF/views/wallet/wallet_section.jsp" />
    </div>

</div>

<script>
function goBack() {
    const params = new URLSearchParams(window.location.search);
    const roomId = params.get("roomId");

    if (roomId) {
        location.href =
          "${pageContext.request.contextPath}/chat/room/" + roomId;
    } else {
        location.href =
          "${pageContext.request.contextPath}/chat/roomList";
    }
}
</script>

</body>
</html>

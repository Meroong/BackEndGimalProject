<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>채팅방 목록</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .chat-room { border: 1px solid #ccc; padding: 10px; margin-bottom: 10px; }
        .chat-room a { text-decoration: none; font-weight: bold; }
    </style>
</head>
<body>

<h2>채팅방 목록</h2>

<c:if test="${not empty message}">
    <p style="color:red;">${message}</p>
</c:if>

<!-- 채팅방 리스트 -->
<c:forEach var="room" items="${chatList}">
    <div class="chat-room">
        <a href="${pageContext.request.contextPath}/chat/room/${room.roomId}">
            ${room.roomType} 방 #${room.roomId}
        </a>

<%--         <form method="post" 
              action="${pageContext.request.contextPath}/chat/roomDelete/${room.roomId}"
              style="display:inline;">
            <button type="submit">삭제</button>
        </form> --%>
        
        <form method="post" 
              action="${pageContext.request.contextPath}/chat/roomQuit/${room.roomId}"
              style="display:inline;">
            <button type="submit">방 나오기</button>
        </form>
    </div>
</c:forEach>

</body>
</html>

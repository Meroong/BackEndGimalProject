<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>채팅방 #${selectedRoomId}</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .chat-messages { border: 1px solid #ccc; padding: 10px; height: 300px; overflow-y: scroll; margin-bottom: 10px; }
    </style>
</head>
<body>

<h2>채팅방 #${selectedRoomId}</h2>

<!-- 채팅 메시지 출력 -->
<div class="chat-messages">
    <c:if test="${empty messages}">
        <p>채팅이 없습니다.</p>
    </c:if>

    <c:forEach var="msg" items="${messages}">
        <p><b>${msg.senderId}</b> : ${msg.content}</p>
    </c:forEach>
</div>

<!-- 메시지 입력 -->
<form method="post" action="${pageContext.request.contextPath}/chat/sendChat">
    <input type="hidden" name="roomId" value="${selectedRoomId}"/>
    <input type="text" name="content" placeholder="메시지 입력" size="50"/>
    <button type="submit">전송</button>
</form>

<!-- 목록으로 돌아가기 -->
<p><a href="${pageContext.request.contextPath}/chat/roomList">채팅방 목록으로 돌아가기</a></p>

</body>
</html>

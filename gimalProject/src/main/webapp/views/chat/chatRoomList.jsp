<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>채팅방 목록</title>
    <style>
        @charset "UTF-8";

        body {
            margin: 0;
            padding: 0;
            background: #F5F6FA;
            font-family: 'Pretendard', sans-serif;
            color: #222;
        }

        .container {
            width: 1000px;
            max-width: 95%;
            margin: 30px auto;
            padding: 20px;
        }

        h2 {
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 25px;
            color: #FF7C40;
        }

        .chat-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 20px;
        }

        .chat-card {
            background: #fff;
            border-radius: 16px;
            padding: 20px;
            box-shadow: 0 5px 18px rgba(0,0,0,0.07);
            transition: 0.2s;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .chat-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 22px rgba(0,0,0,0.12);
        }

        .chat-title {
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 15px;
            color: #5271FF;
        }

        .chat-actions {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }

        .chat-actions a,
        .chat-actions button {
            padding: 8px 16px;
            border-radius: 10px;
            border: none;
            font-weight: 600;
            cursor: pointer;
            transition: 0.15s;
        }

        .chat-actions a {
            text-decoration: none;
            background: #FF7C40;
            color: white;
        }

        .chat-actions a:hover {
            background: #ff6720;
        }

        .chat-actions button {
            background: #222;
            color: white;
        }

        .chat-actions button:hover {
            background: #444;
        }

        .message {
            color: red;
            margin-bottom: 15px;
            font-weight: 600;
        }

    </style>
</head>
<body>

<div class="container">
    <h2>채팅방 목록</h2>

    <c:if test="${not empty message}">
        <p class="message">${message}</p>
    </c:if>

    <div class="chat-grid">
        <c:forEach var="room" items="${chatList}">
            <div class="chat-card">
                <div class="chat-title">
                    ${room.roomType} 방 #${room.roomId}
                </div>

                <div class="chat-actions">
                    <a href="${pageContext.request.contextPath}/chat/room/${room.roomId}">입장</a>
                    <form method="post" 
                          action="${pageContext.request.contextPath}/chat/roomQuit/${room.roomId}">
                        <button type="submit">방 나오기</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>

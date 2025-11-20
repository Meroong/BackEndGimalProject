<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>채팅방 #${selectedRoomId}</title>
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
            width: 800px;
            max-width: 95%;
            margin: 30px auto;
            padding: 20px;
        }

        h2 {
            font-size: 26px;
            font-weight: 700;
            margin-bottom: 20px;
            color: #FF7C40;
        }

        .chat-box {
            background: #fff;
            border-radius: 16px;
            padding: 20px;
            box-shadow: 0 5px 18px rgba(0,0,0,0.07);
            display: flex;
            flex-direction: column;
            height: 500px;
        }

        .chat-messages {
            flex: 1;
            border: 1px solid #DDD;
            border-radius: 12px;
            padding: 15px;
            margin-bottom: 15px;
            overflow-y: auto;
            background: #FCFBFE;
        }

        .chat-messages p {
            margin: 5px 0;
        }

        .chat-messages b {
            color: #5271FF;
        }

        .chat-form {
            display: flex;
            gap: 10px;
        }

        .chat-form input[type="text"] {
            flex: 1;
            padding: 12px 16px;
            border-radius: 12px;
            border: 1px solid #DDD;
            font-size: 15px;
            outline: none;
            transition: 0.15s;
        }

        .chat-form input[type="text"]:focus {
            border-color: #FF7C40;
        }

        .chat-form button {
            padding: 12px 20px;
            border-radius: 12px;
            border: none;
            background: #FF7C40;
            color: white;
            font-weight: 700;
            cursor: pointer;
            transition: 0.15s;
        }

        .chat-form button:hover {
            background: #ff6720;
        }

        .back-link {
            margin-top: 15px;
            display: inline-block;
            color: #5271FF;
            font-weight: 600;
            text-decoration: none;
            transition: 0.15s;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>채팅방 #${selectedRoomId}</h2>

    <div class="chat-box">
        <div class="chat-messages">
            <c:if test="${empty messages}">
                <p>채팅이 없습니다.</p>
            </c:if>

            <c:forEach var="msg" items="${messages}">
                <p><b>${msg.senderId}</b> : ${msg.content}</p>
            </c:forEach>
        </div>

        <form class="chat-form" method="post" action="${pageContext.request.contextPath}/chat/sendChat">
            <input type="hidden" name="roomId" value="${selectedRoomId}"/>
            <input type="text" name="content" placeholder="메시지 입력"/>
            <button type="submit">전송</button>
        </form>
    </div>

    <a class="back-link" href="${pageContext.request.contextPath}/chat/roomList">채팅방 목록으로 돌아가기</a>
</div>

</body>
</html>

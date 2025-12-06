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
            display: flex;
            justify-content: space-between;
            align-items: center;
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

        /* 햄버거 버튼 */
        .menu-container {
            position: relative;
            display: inline-block;
        }

        .menu-btn {
            background-color: #FF7C40;
            border: none;
            border-radius: 50%;
            width: 36px;
            height: 36px;
            color: white;
            font-weight: bold;
            cursor: pointer;
            font-size: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .menu-content {
            display: none;
            position: absolute;
            right: 0;
            background-color: white;
            min-width: 180px;
            box-shadow: 0px 8px 16px rgba(0,0,0,0.2);
            border-radius: 12px;
            z-index: 1;
            padding: 10px;
        }

        .menu-container:hover .menu-content {
            display: block;
        }

        .menu-content button {
            width: 100%;
            background: #FF7C40;
            border: none;
            color: white;
            padding: 8px 12px;
            margin: 5px 0;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
        }

        .menu-content button:hover {
            background: #ff6720;
        }

        .participant-list {
            margin-top: 10px;
        }

        .participant-list li {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 5px;
        }

        .participant-list button {
            padding: 4px 8px;
            font-size: 12px;
        }

    </style>
</head>
<body>

<div class="container">
    <h2>
        채팅방 #${selectedRoomId}

        <c:if test="${isHost}">
            <div class="menu-container">
                <button class="menu-btn">＋</button>
                <div class="menu-content">
                    <c:if test="${roomInfo.roomType eq 'GROUP'}">
                        <button type="button" onclick="endMeeting()">모임 종료</button>
                    </c:if>
                    <c:if test="${not empty participants}">
                        <p>참가자 관리</p>
                        <ul class="participant-list">
                            <c:forEach var="p" items="${participants}">
                                <li>
                                    ${p.userName}
                                    <button type="button" onclick="kickParticipant(${p.userId})">강퇴</button>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
            </div>
        </c:if>

        <c:if test="${not isHost}">
            <div class="menu-container">
                <button class="menu-btn">＋</button>
                <div class="menu-content">
                    <button type="button" onclick="leaveRoom()">채팅방 나가기</button>
                </div>
            </div>
        </c:if>
    </h2>

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

<script>
    function kickParticipant(userId) {
        if(confirm("정말 강퇴하시겠습니까?")) {
            // AJAX 호출로 서버에 강퇴 요청
            fetch('${pageContext.request.contextPath}/meeting/kick?userId=' + userId + '&roomId=' + ${selectedRoomId})
                .then(res => location.reload());
        }
    }

    function endMeeting() {
        if(confirm("모임을 종료하시겠습니까?")) {
            fetch('${pageContext.request.contextPath}/meeting/end?roomId=' + ${selectedRoomId})
                .then(res => location.href='${pageContext.request.contextPath}/chat/roomList');
        }
    }

    function leaveRoom() {
        if(confirm("채팅방을 나가시겠습니까?")) {
            fetch('${pageContext.request.contextPath}/chat/leave?roomId=' + ${selectedRoomId})
                .then(res => location.href='${pageContext.request.contextPath}/chat/roomList');
        }
    }
</script>

</body>
</html>

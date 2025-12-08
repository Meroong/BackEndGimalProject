<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>채팅방 #${selectedRoomId}</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background: #F5F6FA;
            font-family: 'Pretendard', sans-serif;
        }

        /* 팝업 채팅창 컨테이너 */
        .container {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 400px;
            height: 600px;
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 5px 18px rgba(0,0,0,0.15);
            display: flex;
            flex-direction: column;
            overflow: hidden;
            z-index: 1000;
        }

        /* 헤더 */
        h2 {
            font-size: 18px;
            color: #FF7C40;
            padding: 12px 16px;
            margin: 0;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: center;
            align-items: center;
            position: relative;
        }

        /* 뒤로가기 버튼 */
        .back-btn {
            position: absolute;
            left: 16px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 18px;
            color: #FF7C40;
            text-decoration: none;
        }

        /* 모임원 관리 버튼 */
        .member-btn {
            position: absolute;
            right: 16px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 18px;
            background: none;
            border: none;
            color: #FF7C40;
            cursor: pointer;
        }

        .chat-box {
            flex: 1;
            display: flex;
            flex-direction: column;
            padding: 12px;
            overflow: hidden;
        }

        .chat-messages {
            flex: 1 1 auto;
            overflow-y: auto;
            padding: 8px;
            background: #FCFBFE;
            border-radius: 12px;
            margin-bottom: 8px;
        }

        .msg {
            display: flex;
            align-items: flex-end;
            margin-bottom: 10px;
            gap: 8px;
            width: 100%;
        }

        .left-msg {
            justify-content: flex-start;
            text-align: left;
        }

        .right-msg {
            justify-content: flex-end;
            text-align: right;
        }

        .msg img {
            width: 28px;
            height: 28px;
            border-radius: 50%;
        }

        .bubble {
            max-width: 70%;
            padding: 8px 12px;
            border-radius: 16px;
            display: inline-block;
            word-wrap: break-word;
        }

        .left-bubble {
            background: #fff;
            border: 1px solid #DDD;
        }

        .right-bubble {
            background: #FF7C40;
            color: #fff;
        }

        .name-tag {
            font-size: 11px;
            margin-bottom: 2px;
            color: #555;
        }

        /* 메시지 입력 영역 */
        .chat-form {
            display: flex;
            gap: 6px;
            padding: 8px 0;
        }

        .chat-form input {
            flex: 1;
            padding: 8px;
            border-radius: 12px;
            border: 1px solid #DDD;
        }

        .chat-form button {
            padding: 8px 12px;
            background: #FF7C40;
            border: none;
            border-radius: 12px;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        /* 모달 */
        .modal {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: #fff;
            border-radius: 12px;
            padding: 16px;
            box-shadow: 0 5px 18px rgba(0,0,0,0.2);
            z-index: 2000;
            width: 300px;
        }

        .modal h3 {
            margin-top: 0;
        }

        .modal ul {
            list-style: none;
            padding: 0;
            max-height: 200px;
            overflow-y: auto;
        }

        .modal li {
            margin-bottom: 8px;
        }

        .modal form {
            display: inline;
        }
    </style>
</head>
<body>

<div class="container">

    <h2>
        <a href="${pageContext.request.contextPath}/chat/roomList" class="back-btn">←</a>
        채팅방 #${selectedRoomId}

        <!-- 호스트만 보이도록 + 버튼 -->
        <c:if test="${isHost}">
            <button class="member-btn" onclick="document.getElementById('memberModal').style.display='block';">＋</button>
        </c:if>
    </h2>

    <div class="chat-box">
        <!-- 메시지 영역 -->
        <div class="chat-messages" id="chatMessages">
            <c:url var="defaultProfile" value="/resources/images/default.jpg"/>
            <c:forEach var="msg" items="${messages}">
                <c:choose>
                    <c:when test="${msg.senderId == loginUserId}">
                        <div class="msg right-msg">
                            <div class="bubble right-bubble">${msg.content}</div>
                            <img src="${msg.senderProfile != null ? msg.senderProfile : defaultProfile}">
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="msg left-msg">
                            <img src="${msg.senderProfile != null ? msg.senderProfile : defaultProfile}">
                            <div>
                                <div class="name-tag">${msg.senderNickname}</div>
                                <div class="bubble left-bubble">${msg.content}</div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

        <!-- 메시지 입력 -->
        <form class="chat-form" method="post" action="${pageContext.request.contextPath}/chat/sendChat">
            <input type="hidden" name="roomId" value="${selectedRoomId}"/>
            <input type="text" name="content" placeholder="메시지 입력"/>
            <button type="submit">전송</button>
        </form>

    </div>
</div>

<!-- 모임원 관리 모달 -->
<c:if test="${isHost}">
<div class="modal" id="memberModal">
    <h3>모임원 관리</h3>
    <ul>
        <c:forEach var="user" items="${participantUsers}">
            <li>
                ${user.nickname}
                <c:choose>
                    <c:when test="${user.inChat}">
                        <!-- 강퇴 -->
                        <form method="post" action="${pageContext.request.contextPath}/chat/kick">
                            <input type="hidden" name="roomId" value="${selectedRoomId}"/>
                            <input type="hidden" name="meetId" value="${roomInfo.meetingId}"/>
                            <input type="hidden" name="targetUserId" value="${user.participantId}"/>
                            <button type="submit">강퇴</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <!-- 초대 -->
                        <form method="post" action="${pageContext.request.contextPath}/chat/invite">
                            <input type="hidden" name="roomId" value="${selectedRoomId}"/>
                            <input type="hidden" name="meetId" value="${roomInfo.meetingId}"/>
                            <input type="hidden" name="receiverId" value="${user.participantId}"/>
                            <button type="submit">초대</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </li>
        </c:forEach>
    </ul>
    <button onclick="document.getElementById('memberModal').style.display='none';">닫기</button>
</div>
</c:if>

<script>
    // 스크롤 항상 아래로
    var chatMessages = document.getElementById('chatMessages');
    chatMessages.scrollTop = chatMessages.scrollHeight;
</script>

</body>
</html>

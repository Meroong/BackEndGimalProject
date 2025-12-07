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
            width: 400px;   /* 팝업 크기 조정 */
            height: 600px;  /* 팝업 크기 조정 */
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
            justify-content: center; /* 채팅방 이름 중앙 */
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

		.chat-box {
		    flex: 1;
		    display: flex;
		    flex-direction: column;
		    padding: 12px;
		    overflow: hidden; /* 추가: 내부 스크롤 영역만 사용 */
		}
		
		.chat-messages {
		    flex: 1 1 auto;  /* 기존 flex:1 대신 */
		    overflow-y: auto;
		    padding: 8px;
		    background: #FCFBFE;
		    border-radius: 12px;
		    margin-bottom: 8px; /* 입력창과 간격 */
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

        /* 메시지 입력 영역 항상 하단 고정 */
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

        /* 최소한 스크롤바 */
        .chat-messages::-webkit-scrollbar {
            width: 6px;
        }
        .chat-messages::-webkit-scrollbar-thumb {
            background: rgba(0,0,0,0.2);
            border-radius: 3px;
        }
    </style>
</head>
<body>

<div class="container">

    <h2>
        <a href="${pageContext.request.contextPath}/chat/roomList" class="back-btn">←</a>
        채팅방 #${selectedRoomId}
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
        <form class="chat-form" method="post" action="${pageContext.request.contextPath}/chat/sendChat" id="chatForm">
            <input type="hidden" name="roomId" value="${selectedRoomId}"/>
            <input type="text" name="content" placeholder="메시지 입력"/>
            <button type="submit">전송</button>
        </form>

    </div>

</div>

<script>
    // 페이지 로드 시 스크롤 항상 아래로
    const chatMessages = document.getElementById('chatMessages');
    chatMessages.scrollTop = chatMessages.scrollHeight;

    // 메시지 전송 후 스크롤 아래로
    const chatForm = document.getElementById('chatForm');
    chatForm.addEventListener('submit', function() {
        setTimeout(() => {
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }, 50); // 폼 제출 후 렌더링까지 약간 지연
    });
</script>

</body>
</html>

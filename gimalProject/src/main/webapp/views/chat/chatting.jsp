<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>채팅방 #${selectedRoomId}</title>
<<<<<<< HEAD

    <!-- ✅ 기존 CSS 그대로 유지 -->
    <style>
        body { margin: 0; padding: 0; background: #F5F6FA; font-family: 'Pretendard', sans-serif; }
        .container { position: fixed; bottom: 20px; right: 20px; width: 400px; height: 600px; background: #fff; border-radius: 16px; box-shadow: 0 5px 18px rgba(0,0,0,0.15); display: flex; flex-direction: column; overflow: hidden; z-index: 1000; }
        h2 { font-size: 18px; color: #FF7C40; padding: 12px 16px; margin: 0; border-bottom: 1px solid #eee; display: flex; justify-content: center; align-items: center; position: relative; }
        .back-btn { position: absolute; left: 16px; top: 50%; transform: translateY(-50%); font-size: 18px; color: #FF7C40; text-decoration: none; }
        .member-btn { position: absolute; right: 16px; top: 50%; transform: translateY(-50%); font-size: 18px; background: none; border: none; color: #FF7C40; cursor: pointer; }
        .pay-toggle-btn { position: absolute; right: 50px; top: 50%; transform: translateY(-50%); font-size: 13px; background: #2E86DE; border: none; color: white; padding: 4px 8px; border-radius: 8px; cursor: pointer; }
        .pay-box { display: none; padding: 10px; text-align: center; border-bottom: 1px solid #eee; background: #F8F9FA; }
        .chat-box { flex: 1; display: flex; flex-direction: column; padding: 12px; overflow: hidden; }
        .chat-messages { flex: 1 1 auto; overflow-y: auto; padding: 8px; background: #FCFBFE; border-radius: 12px; margin-bottom: 8px; }
        .msg { display: flex; align-items: flex-end; margin-bottom: 10px; gap: 8px; width: 100%; }
        .left-msg { justify-content: flex-start; }
        .right-msg { justify-content: flex-end; }
        .msg img { width: 28px; height: 28px; border-radius: 50%; }
        .bubble { max-width: 70%; padding: 8px 12px; border-radius: 16px; display: inline-block; }
        .left-bubble { background: #fff; border: 1px solid #DDD; }
        .right-bubble { background: #FF7C40; color: #fff; }
        .chat-form { display: flex; gap: 6px; padding: 8px 0; }
        .chat-form input { flex: 1; padding: 8px; border-radius: 12px; border: 1px solid #DDD; }
        .chat-form button { padding: 8px 12px; background: #FF7C40; border: none; border-radius: 12px; color: white; font-weight: bold; cursor: pointer; }
        .modal { display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; border-radius: 12px; padding: 16px; box-shadow: 0 5px 18px rgba(0,0,0,0.2); z-index: 2000; width: 300px; }

        /* ✅ 추가된 호스트 메뉴 */
        .host-menu {
            display: none;
            position: absolute;
            top: 60px;
            right: 16px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 6px 16px rgba(0,0,0,0.2);
            padding: 8px;
            z-index: 3000;
        }

        .host-menu button {
            width: 100%;
            padding: 8px 12px;
            margin: 4px 0;
            border: none;
            border-radius: 8px;
            background: #FF7C40;

            color: white;
            font-weight: bold;
            cursor: pointer;
        }
        .host-menu button:hover {
            background: #e96a2f;
        }
    </style>
</head>


<body>

<div class="container">
<h2>
    <a href="${pageContext.request.contextPath}/chat/roomList" class="back-btn">←</a>
    채팅방 #${selectedRoomId}

    <!--  회비 버튼 유지 -->
    <c:if test="${meetingCost != null && meetingCost > 0 && !hasPaid}">
        <button class="pay-toggle-btn" onclick="togglePayBox()">회비</button>
    </c:if>

    <!-- 플러스 버튼 (호스트 전용 메뉴 호출) -->
    <c:if test="${isHost}">
        <button class="member-btn" onclick="toggleHostMenu()">＋</button>
    </c:if>
</h2>

<!-- 호스트 전용 메뉴 -->
<div id="hostMenu" class="host-menu">
    <button onclick="openMemberModal()">👥 모임원 관리</button>
    <button onclick="openVoteModal()">📊 투표 만들기</button>
</div>

<!-- 회비 박스 -->
<c:if test="${meetingCost != null && meetingCost > 0 && !hasPaid}">
    <div class="pay-box" id="payBox">
        <form method="post" action="${pageContext.request.contextPath}/wallet/pay">
            <input type="hidden" name="meetingId" value="${roomInfo.meetingId}">
            <input type="hidden" name="roomId" value="${selectedRoomId}">
            <input type="hidden" name="amount" value="${meetingCost}">
            <button type="submit">회비 ${meetingCost}원 결제</button>
        </form>
    </div>
</c:if>

<!-- 채팅 영역 -->
<div class="chat-box">
    <div class="chat-messages" id="chatMessages">

        <!-- 투표 표시 -->
        <c:forEach var="vote" items="${voteList}">
            <div style="border:1px solid #ddd; padding:10px; border-radius:10px; margin-bottom:10px;">
                <b>${vote.title}</b><br>
                <small>마감: ${vote.endTime}</small>

                <c:if test="${!vote.closed}">
                    <form method="post" action="${pageContext.request.contextPath}/vote/submit">
                        <input type="hidden" name="voteId" value="${vote.voteId}">
                        <c:forEach var="opt" items="${vote.options}">
                            <button name="optionId" value="${opt.optionId}">
                                ${opt.text} (${opt.count})
                            </button><br>
                        </c:forEach>
                    </form>
                </c:if>

                <c:if test="${vote.closed}">
                    <div>✅ 투표 마감</div>
                </c:if>
            </div>
        </c:forEach>

        <!-- 기존 메시지 -->
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
                        <div class="bubble left-bubble">${msg.content}</div>
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
<div class="modal" id="memberModal">
    <h3>모임원 관리</h3>
    <ul>
        <c:forEach var="user" items="${participantUsers}">
            <li>${user.nickname}</li>
        </c:forEach>
    </ul>
    <button onclick="closeMemberModal()">닫기</button>
</div>

<!-- 투표 생성 모달 -->
<div class="modal" id="voteModal">
    <h3>투표 만들기</h3>

    <form method="post" action="${pageContext.request.contextPath}/vote/create">
        <input type="hidden" name="roomId" value="${selectedRoomId}">
        <input type="text" name="title" placeholder="투표 제목" required><br><br>
        <input type="text" name="opt1" placeholder="항목 1" required><br><br>
        <input type="text" name="opt2" placeholder="항목 2" required><br><br>
        <label>마감 시간</label>
        <input type="datetime-local" name="endTime"><br><br>
        <button type="submit">투표 생성</button>
    </form>

    <button onclick="closeVoteModal()">닫기</button>
</div>

<script>
function togglePayBox() {
    var box = document.getElementById("payBox");
    box.style.display = box.style.display === "block" ? "none" : "block";
}

function toggleHostMenu() {
    var menu = document.getElementById("hostMenu");
    menu.style.display = (menu.style.display === "block") ? "none" : "block";
}

function openVoteModal() {
    document.getElementById("hostMenu").style.display = "none";
    document.getElementById("voteModal").style.display = "block";
}

function closeVoteModal() {
    document.getElementById("voteModal").style.display = "none";
}

function openMemberModal() {
    document.getElementById("hostMenu").style.display = "none";
    document.getElementById("memberModal").style.display = "block";
}

function closeMemberModal() {
    document.getElementById("memberModal").style.display = "none";
}

</script>

</body>
</html>

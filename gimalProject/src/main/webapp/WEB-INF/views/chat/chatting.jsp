<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>채팅방 #${selectedRoomId}</title>

<style>
/* =====================
   기본 레이아웃
===================== */
body {
    margin: 0;
    background: #F5F6FA;
    font-family: 'Pretendard', sans-serif;
}
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

/* 메뉴들은 container 기준으로 */
.host-menu,
.pay-box {
    position: absolute;
}
/* =====================
   헤더
===================== */
.chat-header {
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-bottom: 1px solid #eee;
    position: relative;
    font-weight: bold;
    color: #FF7C40;
}
.chat-header a,
.chat-header button {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    background: none;
    border: none;
    cursor: pointer;
    color: #FF7C40;
}
.back-btn { left: 16px; font-size: 18px; }
.member-btn { right: 16px; font-size: 20px; }
.pay-toggle-btn {
    right: 50px;
    font-size: 12px;
    background: #2E86DE;
    color: #fff;
    padding: 4px 8px;
    border-radius: 8px;
}

/* =====================
   채팅 영역
===================== */
.chat-box {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 12px;
    padding-bottom: 8px;
}
.chat-messages {
    flex: 1;
    overflow-y: auto;
    padding: 8px;
    background: #FCFBFE;
    border-radius: 12px;
}


/* 메시지 공통 */
.msg {
    display: flex;
    gap: 8px;
    margin-bottom: 10px;
}
.left-msg { justify-content: flex-start; }
.right-msg { justify-content: flex-end; }

.msg img.profile {
    width: 28px;
    height: 28px;
    border-radius: 50%;
}

/* 말풍선 */
.bubble {
    max-width: 70%;
    padding: 8px 12px;
    border-radius: 16px;
    word-break: break-word;
}
.left-bubble {
    background: #fff;
    border: 1px solid #ddd;
}
.right-bubble {
    background: #FF7C40;
    color: #fff;
}

/* =====================
   이미지 메시지 (카카오/당근 스타일)
===================== */
.image-bubble {
    padding: 4px;
    border-radius: 12px;
}
.chat-image {
    max-width: 180px;     /* 카카오/당근 평균 */
    max-height: 230px;
    border-radius: 12px;
    object-fit: cover;
    display: block;
}

/* =====================
   입력 영역
===================== */
.chat-form {
    display: flex;
    gap: 6px;
    padding-top: 8px;
}
.chat-form input[type=text] {
    flex: 1;
    padding: 8px;
    border-radius: 12px;
    border: 1px solid #ddd;
}
.chat-form button {
    padding: 8px 12px;
    border-radius: 12px;
    background: #FF7C40;
    color: white;
    border: none;
    font-weight: bold;
    cursor: pointer;
        position: relative;
    z-index: 20;   /* 전송 버튼 클릭 우선 */
}


/* =====================
   호스트 메뉴
===================== */
.host-menu {
    display: none;
    position: absolute;
    top: 60px;
    right: 16px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 6px 16px rgba(0,0,0,0.2);
    padding: 8px;
    z-index: 3000;
}
.host-menu button {
    width: 100%;
    padding: 8px;
    margin: 4px 0;
    border-radius: 8px;
    background: #FF7C40;
    color: white;
    border: none;
    cursor: pointer;
}

/* =====================
   모달
===================== */
.modal {
    display: none;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 300px;
    background: #fff;
    border-radius: 12px;
    padding: 16px;
    box-shadow: 0 5px 18px rgba(0,0,0,0.2);
    z-index: 4000;
}
/* =====================
   이미지 미리보기 (오른쪽 아래)
===================== */
.image-preview-box {
    display: none;
    position: absolute;   /* ← 위치 고정 */
    right: 92px;
    bottom: 36px;
    width: 64px;
    height: 64px;
    z-index:10;
    border-radius: 10px;
    overflow: visible;    /* ❗ 중요 */
    background: #eee;
    box-shadow: 0 4px 12px rgba(0,0,0,0.25);
}
.image-preview-inner {
    width: 100%;
    height: 100%;
    position: relative;
}

.image-preview-inner img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center;   /* 🔥 이 줄 */
    transform: scale(1.08);
}
/* ❌ 제거 버튼 */
.image-preview-remove {
    position: absolute;
    top: -3px;
    right: -3px;
    width: 14px;
    height: 14px;
    border-radius: 50%;
    border: none;
    background: rgba(0,0,0,0.65);
    color: #fff;
    font-size: 9px;
    line-height: 14px;
    text-align: center;
    cursor: pointer;
}
.chat-input-wrapper {
    position: relative;   /* ✅ absolute 기준 */
}
.pay-box {
    display: none;          /* ⭐ 처음엔 무조건 숨김 */
    position: absolute;
    top: 52px;
    right: 12px;
    width: 260px;
    padding: 12px;
    background: #F9FAFB;
    border-radius: 12px;
    box-shadow: 0 6px 16px rgba(0,0,0,0.2);
    font-size: 14px;
    z-index: 5000;
}

</style>
</head>

<body>

<div class="container">

<!-- 헤더 -->
<div class="chat-header">
    <a href="${pageContext.request.contextPath}/chat/roomList" class="back-btn">←</a>
    채팅방 #${selectedRoomId}

    <c:if test="${meetingCost != null && meetingCost > 0 && !hasPaid}">
        <button class="pay-toggle-btn" onclick="togglePayBox()">회비</button>
    </c:if>

    <button class="member-btn" onclick="toggleHostMenu()">＋</button>
</div>
<!-- 회비 결제 박스 -->
<div id="payBox" class="pay-box">

    <!-- ✅ 메인 결제 버튼 -->
    <button class="pay-btn"
            onclick="handlePay(
                ${meetingCost},
                ${sessionScope.walletBalance}
            )">
        회비 ${meetingCost}원 결제하기
    </button>

    <!-- ✅ 내 포인트 표시 -->
    <div style="margin-top:8px; font-size:13px; color:#555;">
        현재 내 포인트: <b>${sessionScope.walletBalance} P</b>
    </div>

</div>

<!-- 호스트 메뉴 -->
<div id="hostMenu" class="host-menu">
    <form method="post"
          action="${pageContext.request.contextPath}/chat/roomQuit/${selectedRoomId}"
          onsubmit="return confirmExitChat();">
        <button type="submit">🚪 방 나가기</button>
    </form>

    <c:if test="${roomInfo.roomType eq 'GROUP' && isHost}">
        <button onclick="openMemberModal()">👥 모임원 관리</button>
        <button onclick="openVoteModal()">📊 투표 만들기</button>

        <form method="post"
              action="${pageContext.request.contextPath}/meeting/delete"
              onsubmit="return confirm('모임이 삭제되면 채팅방도 삭제됩니다.');">
            <input type="hidden" name="meetingId" value="${roomInfo.meetingId}">
            <button type="submit">🗑 모임 삭제</button>
        </form>
    </c:if>
</div>

<!-- 채팅 메시지 -->
<div class="chat-messages">

<c:url var="defaultProfile" value="/resources/images/default.jpg"/>

<c:forEach var="msg" items="${messages}">
<c:choose>

<c:when test="${msg.senderId == loginUserId}">
<div class="msg right-msg">
    <c:choose>
        <c:when test="${msg.messageType eq 'IMAGE'}">
            <div class="bubble image-bubble right-bubble">
                <img src="${pageContext.request.contextPath}${msg.imageUrl}" class="chat-image">
            </div>
        </c:when>
        <c:otherwise>
            <div class="bubble right-bubble">${msg.content}</div>
        </c:otherwise>
    </c:choose>
    <img class="profile" src="${msg.senderProfile != null ? msg.senderProfile : defaultProfile}">
</div>
</c:when>

<c:otherwise>
<div class="msg left-msg">
    <img class="profile" src="${msg.senderProfile != null ? msg.senderProfile : defaultProfile}">
    <c:choose>
        <c:when test="${msg.messageType eq 'IMAGE'}">
            <div class="bubble image-bubble left-bubble">
                <img src="${pageContext.request.contextPath}${msg.imageUrl}" class="chat-image">
            </div>
        </c:when>
        <c:otherwise>
            <div class="bubble left-bubble">${msg.content}</div>
        </c:otherwise>
    </c:choose>
</div>
</c:otherwise>

</c:choose>
</c:forEach>

</div>
<!-- 입력 -->
<div class="chat-input-wrapper">

	<form class="chat-form"
      method="post"
      action="${pageContext.request.contextPath}/chat/sendChat"
      enctype="multipart/form-data"
      >

    <!-- 반드시 필요 -->
    <input type="hidden" name="roomId" value="${selectedRoomId}">

        <label for="imageInput">📷</label>
        <input type="file" id="imageInput" name="image" accept="image/*"
               style="display:none" onchange="previewImage(this)"/>

        <input type="text" name="content" placeholder="메시지 입력"/>
        <button type="submit">전송</button>
    </form>

    <!-- ✅ form 밖, wrapper 기준 -->
    <div id="imagePreviewBox" class="image-preview-box">
        <div class="image-preview-inner">
            <img id="imagePreview">
            <button type="button"
                    class="image-preview-remove"
                    onclick="clearImagePreview()">✕</button>
        </div>
    </div>

</div>


</div>

<!-- 모달 -->
<div id="memberModal" class="modal">
    <h3>모임원 관리</h3>
    <c:forEach var="u" items="${participantUsers}">
        <div>${u.nickname}</div>
    </c:forEach>
    <button onclick="closeMemberModal()">닫기</button>
</div>

<div id="voteModal" class="modal">
    <h3>투표 만들기</h3>
    <form method="post" action="${pageContext.request.contextPath}/vote/create">
        <input type="hidden" name="roomId" value="${selectedRoomId}">
        <input type="text" name="title" placeholder="제목" required><br><br>
        <input type="text" name="opt1" placeholder="항목1" required><br><br>
        <input type="text" name="opt2" placeholder="항목2" required><br><br>
        <input type="datetime-local" name="endTime"><br><br>
        <button type="submit">생성</button>
    </form>
    <button onclick="closeVoteModal()">닫기</button>
</div>

<script>
function toggleHostMenu(){
    const m = document.getElementById("hostMenu");
    m.style.display = m.style.display === "block" ? "none" : "block";
}
function openMemberModal(){ hostMenu.style.display="none"; memberModal.style.display="block"; }
function closeMemberModal(){ memberModal.style.display="none"; }
function openVoteModal(){ hostMenu.style.display="none"; voteModal.style.display="block"; }
function closeVoteModal(){ voteModal.style.display="none"; }
function confirmExitChat(){
    return confirm("${roomInfo.roomType eq 'GROUP'}" === "true"
        ? "채팅방을 나가면 모임에서도 나가게 됩니다. 계속할까요?"
        : "채팅방을 나갈까요?");
}

function previewImage(input) {
    if (!input.files || !input.files[0]) return;

    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = function(e) {
        document.getElementById("imagePreview").src = e.target.result;
        document.getElementById("imagePreviewBox").style.display = "block";
    };

    reader.readAsDataURL(file);
}

function clearImagePreview() {
    document.getElementById("imageInput").value = "";
    document.getElementById("imagePreview").src = "";
    document.getElementById("imagePreviewBox").style.display = "none";
}
</script>
<script>
function handlePay(meetingCost, myPoint) {

	if (myPoint < meetingCost) {
	    alert("포인트가 부족합니다. 충전 페이지로 이동합니다.");
	    location.href =
	        "${pageContext.request.contextPath}/page/wallet?returnUrl=/chat/room/${selectedRoomId}";
	    return;
	}

    if (!confirm("회비 " + meetingCost + "원을 결제하시겠습니까?")) {
        return;
    }

    const form = document.createElement("form");
    form.method = "post";
    form.action = "${pageContext.request.contextPath}/wallet/pay";

    // meetingId
    const meetInput = document.createElement("input");
    meetInput.type = "hidden";
    meetInput.name = "meetingId";
    meetInput.value = "${roomInfo.meetingId}";
    form.appendChild(meetInput);

    // roomId
    const roomInput = document.createElement("input");
    roomInput.type = "hidden";
    roomInput.name = "roomId";
    roomInput.value = "${selectedRoomId}";
    form.appendChild(roomInput);

    // amount
    const amountInput = document.createElement("input");
    amountInput.type = "hidden";
    amountInput.name = "amount";
    amountInput.value = meetingCost;
    form.appendChild(amountInput);

    // ⭐ returnUrl (핵심)
    const returnInput = document.createElement("input");
    returnInput.type = "hidden";
    returnInput.name = "returnUrl";
    returnInput.value = "/chat/room/${selectedRoomId}";
    form.appendChild(returnInput);

    document.body.appendChild(form);
    form.submit();
}

function togglePayBox() {
    const box = document.getElementById("payBox");
    if (!box) return;

    box.style.display =
        box.style.display === "block" ? "none" : "block";
}
document.addEventListener("click", function (e) {
    const box = document.getElementById("payBox");
    const btn = document.querySelector(".pay-toggle-btn");

    if (!box || !btn) return;

    if (!box.contains(e.target) && !btn.contains(e.target)) {
        box.style.display = "none";
    }
});

window.addEventListener("load", () => {
    scrollToBottom();  // 페이지 로딩 후 스크롤을 맨 아래로 내립니다.
});
function scrollToBottom() {
    const box = document.querySelector('.chat-messages');  // id가 아니라 class를 선택
    if (box) {
        box.scrollTop = box.scrollHeight;  // 메시지가 추가될 때 자동으로 맨 아래로 스크롤
    }
}
// 예시: 메시지가 전송될 때마다 호출
document.querySelector('.chat-form').addEventListener('submit', function() {
    scrollToBottom();  // 새 메시지 전송 후 최하단으로 이동
});
</script>

</body>
</html>

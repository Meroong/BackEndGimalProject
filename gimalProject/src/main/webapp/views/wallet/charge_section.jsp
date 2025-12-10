<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="map-card">

    <div class="center-title">포인트 & 회비 지갑</div>
    <div class="center-desc">모임 회비 결제에 사용할 포인트를 관리할 수 있습니다.</div>

    <div class="wallet-title-row">
        <span class="label">현재 보유 포인트</span>
        <span class="balance">${walletBalance} P</span>
    </div>

    <div class="wallet-sub">
        포인트는 모임 회비 결제에 사용되며, 추후 회비 정산 기능과 연동될 예정입니다.
    </div>

    <div class="wallet-charge-box">
        <h4>포인트 충전</h4>
        <small>테스트용 모의 카드 정보를 입력하면 포인트가 충전됩니다.</small>

        <form method="post" action="${pageContext.request.contextPath}/wallet/charge">
            
            <c:if test="${not empty param.roomId}">
                <input type="hidden" name="redirectRoomId" value="${param.roomId}">
            </c:if>

            <label>카드번호</label>
            <input type="text" name="cardNumber">

            <label>CVC</label>
            <input type="text" name="cvc">

            <label>비밀번호 앞 2자리</label>
            <input type="password" name="cardPw">

            <label>충전 금액</label>
            <input type="number" name="amount">

            <button type="submit">충전하기</button>
        </form>

        <div class="wallet-hint">
            ※ 실제 결제는 일어나지 않고,<br>
            테스트용 mock 카드 데이터로만 포인트가 충전됩니다.
        </div>
    </div>

</div>

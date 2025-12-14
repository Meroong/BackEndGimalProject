<%@ page contentType="text/html; charset=UTF-8" %>

<div class="wallet-charge-box">
    <h4>포인트 충전</h4>
    <small>테스트용 모의 카드 정보를 입력하면 포인트가 충전됩니다.</small>

    <form method="post" action="${pageContext.request.contextPath}/wallet/charge">
        <label>카드번호</label>
        <input type="text" name="cardNumber" placeholder="예: 1111-2222-3333-4444">

        <label>CVC</label>
        <input type="text" name="cvc" placeholder="3자리 숫자">

        <label>비밀번호 앞 2자리</label>
        <input type="password" name="cardPw" placeholder="예: 12">

        <label>충전 금액</label>
        <input type="number" name="amount" placeholder="예: 10000">

        <button type="submit">충전하기</button>
    </form>

    <div class="wallet-hint">
        ※ 실제 결제는 일어나지 않고,<br>
        테스트용 mock 카드 데이터로만 포인트가 충전됩니다.
    </div>
</div>

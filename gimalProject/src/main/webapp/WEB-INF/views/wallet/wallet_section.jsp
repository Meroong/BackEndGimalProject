<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="map-card">

    <!-- ✅ 포인트 보유 내역 -->
    <div class="wallet-title-row">
        <span class="label">현재 보유 포인트</span>
        <span class="balance">
            <c:choose>
                <c:when test="${not empty sessionScope.walletBalance}">
                    ${sessionScope.walletBalance} P
                </c:when>
                <c:otherwise>
                    0 P
                </c:otherwise>
            </c:choose>
        </span>
    </div>

    <!-- 🔽 충전 UI 유지 -->
    <jsp:include page="/WEB-INF/views/wallet/wallet_charge.jsp" />

</div>


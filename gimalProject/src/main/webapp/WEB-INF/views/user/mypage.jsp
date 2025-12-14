<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.UserDTO"%>
<%@ page import="dto.UserAddressDTO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지 - 도란도란</title>
    <style>
        body {
            margin:0;
            padding:0;
            background:#F5F6FA;
            font-family:'Pretendard',sans-serif;
            color:#222;
        }
        .container {
            width:1400px;
            max-width:100%;
            margin:0 auto;
            padding:20px 40px;
            box-sizing:border-box;
        }
        header {
            display:flex;
            justify-content:space-between;
            align-items:flex-start;
            padding-top:10px;
        }
        .logo {
            display:flex;
            align-items:center;
            gap:10px;
            font-size:28px;
            font-weight:800;
            color:#FF7C40;
        }
        .logo img {
            width:100px;
            height:100px;
            object-fit:contain;
        }
        .header-buttons {
            display:flex;
            gap:10px;
            align-items:center;
        }
        .log-btn, .mypage-btn, .update-btn, .delete-btn {
            padding:8px 20px;
            border-radius:10px;
            border:none;
            cursor:pointer;
            font-weight:600;
            display:flex;
            align-items:center;
            gap:6px;
            transition:0.2s;
        }
        .log-btn {
            background:#f0f0f0;
            color:#333;
            border:1px solid #ccc;
        }
        .log-btn:hover { background:#e0e0e0; }
        .mypage-btn, .update-btn {
            background:#FF6600;
            color:white;
        }
        .mypage-btn:hover, .update-btn:hover { background:#e65c00; }
        .delete-btn {
            background:#FF4444;
            color:white;
        }
        .delete-btn:hover { background:#cc3333; }

        .main-box {
            margin-top:50px;
            background:white;
            padding:45px;
            border-radius:24px;
            box-shadow:0 6px 24px rgba(0,0,0,0.06);
            box-sizing:border-box;
        }
        .box-title {
            font-size:22px;
            font-weight:700;
            margin-bottom:25px;
        }

        /* ★ 그리드 + 카드 레이아웃 */
        .grid-3 {
            display:grid;
            grid-template-columns: minmax(0,1.2fr) minmax(0,1.8fr) minmax(0,1fr);
            gap:32px;
        }
        .center-card, .map-card, .weather-card {
            background:#FCFBFE;
            border-radius:20px;
            padding:30px;
            box-shadow:0 4px 16px rgba(0,0,0,0.05);
            box-sizing:border-box;
            min-width:0; /* ★ grid 내부에서 넘치지 않게 */
        }

        .center-title {
            font-size:20px;
            font-weight:700;
            margin-bottom:12px;
        }
        .center-desc {
            color:#666;
            font-size:16px;
            margin-bottom:20px;
        }

        input[type="text"],
        input[type="password"],
        input[type="number"] {
            width:100%;
            padding:12px 15px;
            margin:5px 0 15px 0;
            border-radius:12px;
            border:1px solid #DDD;
            font-size:15px;
            font-weight:500;
            box-sizing:border-box; /* ★ padding 포함해서 100% 안 넘치게 */
        }
        label {
            font-weight:600;
            font-size:14px;
        }

        /* ================= 프로필 이미지 영역 ================= */
        .profile-box {
            width:150px;
            height:150px;
            margin-bottom:15px;
            border-radius:50%;
            overflow:hidden;
            border:2px solid #aaa;
        }
        .profile-box img {
            width:100%;
            height:100%;
            object-fit:cover;
        }

        .profile-actions {
            display:flex;
            gap:10px;
            margin-bottom:15px;
        }
        .profile-actions button {
            flex:1;
            padding:8px 10px;
            border-radius:8px;
            border:none;
            cursor:pointer;
            font-weight:600;
        }
        .upload-btn { background:#FF6600; color:white; }
        .delete-btn-small { background:#FF4444; color:white; }

        /* ====== 포인트/지갑 카드 스타일 (가운데 영역) ====== */
        .wallet-title-row {
            display:flex;
            justify-content:space-between;
            align-items:center;
            margin-bottom:10px;
        }
        .wallet-title-row span.label {
            font-size:16px;
            color:#666;
        }
        .wallet-title-row span.balance {
            font-size:22px;
            font-weight:700;
            color:#FF6600;
        }
        .wallet-sub {
            font-size:13px;
            color:#888;
            margin-bottom:20px;
        }
        .wallet-charge-box {
            margin-top:15px;
            padding:18px;
            border-radius:14px;
            background:#FFFFFF;
            border:1px solid #EEE;
            box-sizing:border-box;
        }
        .wallet-charge-box h4 {
            margin:0 0 10px 0;
            font-size:16px;
            font-weight:700;
        }
        .wallet-charge-box small {
            display:block;
            margin-bottom:10px;
            color:#999;
            font-size:12px;
        }
        /* ★ 폼을 세로로 정렬해서 가로로 안 튀어나가게 */
        .wallet-charge-box form {
            display:flex;
            flex-direction:column;
        }
        .wallet-charge-box button {
            width:100%;
            padding:10px 0;
            border-radius:10px;
            border:none;
            background:#FF6600;
            color:white;
            font-weight:700;
            cursor:pointer;
        }
        .wallet-charge-box button:hover {
            background:#e65c00;
        }
        .wallet-hint {
            margin-top:8px;
            font-size:12px;
            color:#AAA;
            line-height:1.4;
        }

        /* 오른쪽 카드(알림 영역) */
        .weather-card-list {
            font-size:14px;
            color:#555;
            line-height:1.6;
            padding-left:18px;
        }
        .weather-card-list li {
            margin-bottom:6px;
        }

        @media (max-width: 1100px) {
            .grid-3 {
                grid-template-columns:1fr;
            }
        }
    </style>

    <script>
        const POPUP_KEY = "<%= "devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc=" %>"; // 팝업용 키
        const RETURN_URL =
            "<%= request.getContextPath() %>/page/addressPopupReturn?mode=mypage";
        function openJusoPopup() {
            window.open(
                "https://business.juso.go.kr/addrlink/addrLinkUrl.do"
                + "?confmKey=devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc="
                + "&returnUrl=" + encodeURIComponent(
                    "http://localhost:8080/gimalProject/views/util/addressPopupReturn.jsp?mode=mypage"
                )
                + "&resultType=4",
                "jusoPopup",
                "width=570,height=420,scrollbars=yes,resizable=yes"
            );
        }
    </script>
    <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
</head>
<body>
<div class="container">

    <%-- 헤더 include --%>
    <jsp:include page="/WEB-INF/views/include/header.jsp" />

    <%
        UserDTO user = (UserDTO) session.getAttribute("userInfo");
        UserAddressDTO addr = (UserAddressDTO) session.getAttribute("addressInfo");

        // 세션에 저장된 포인트 값 가져옴
        Integer walletBalance = (Integer) session.getAttribute("walletBalance");
        if (walletBalance == null) walletBalance = 0;
    %>

    <%-- alert용 메시지 (MVC2 원칙대로 Controller에서만 세팅) --%>
	<c:if test="${not empty errorMessage}">
	    <script>alert("${errorMessage}");</script>
	    <c:remove var="errorMessage" scope="session"/>
	</c:if>
	
	<c:if test="${not empty successMessage}">
	    <script>alert("${successMessage}");</script>
	    <c:remove var="successMessage" scope="session"/>
	</c:if>

    <%
        if (user != null) {
            String profileUrl = (String) session.getAttribute("profileUrl");
            if(profileUrl == null || profileUrl.isEmpty()) {
                profileUrl = "/resources/images/default_profile.png";
            }
    %>

    <section class="main-box">
        <div class="box-title">마이페이지</div>
        <div class="grid-3">

            <%-- 왼쪽: 내정보 + 프로필 이미지 --%>
            <div class="center-card">
                <div class="center-title">내 정보</div>
                <div class="center-desc">회원님의 정보를 확인하고 수정할 수 있습니다.</div>

                <!-- 프로필 이미지 -->
                <div class="profile-box">
                    <img src="<%= request.getContextPath() + profileUrl %>" alt="프로필 이미지">
                </div>

                <!-- 이미지 업로드 / 삭제 -->
                <div class="profile-actions">
                    <form id="profileForm" action="<%= request.getContextPath() %>/image/profileUpload" method="post" enctype="multipart/form-data">
                        <input type="file" id="profileInput" name="img" accept="image/*" style="display:none;" required>
                        <button type="button" id="uploadBtn" class="upload-btn">이미지 등록/수정</button>
                    </form>

                    <form id="profileDeleteForm"
                          action="<%= request.getContextPath() %>/image/profileDelete"
                          method="post">
                        <button type="submit" class="delete-btn-small">프로필 이미지 삭제</button>
                    </form>
                </div>

                <script>
                    const uploadBtn = document.getElementById('uploadBtn');
                    const fileInput = document.getElementById('profileInput');
                    const form = document.getElementById('profileForm');

                    uploadBtn.addEventListener('click', () => fileInput.click());
                    fileInput.addEventListener('change', () => form.submit());
                </script>

                <%-- 회원 정보 수정 폼 --%>
                <form id="updateForm" action="<%= request.getContextPath() %>/user/update" method="post">
                    <input type="hidden" name="autoId" value="<%= user.getAutoId() %>">

                    <label>이름</label>
                    <input type="text" value="<%= user.getUserName() != null ? user.getUserName() : "" %>" disabled>

                    <label>닉네임</label>
                    <input type="text" name="newNickname" value="<%= user.getNickname() != null ? user.getNickname() : "" %>">

                    <label>비밀번호</label>
                    <input type="password" name="newPassword" placeholder="변경할 비밀번호">

                    <%-- 주소 영역 --%>
                    <label>주소</label>
                    <button type="button" class="update-btn" onclick="openJusoPopup()" style="width:auto; margin-bottom:10px;">주소 검색</button>

                    <label>도로명주소</label>
                    <input type="text" id="roadAddress" disabled
                           value="<%= (addr != null && addr.getRoadAddress() != null) ? addr.getRoadAddress() : "" %>" />
                    <input type="hidden" id="roadAddressValue" name="roadAddress"
                           value="<%= (addr != null && addr.getRoadAddress() != null) ? addr.getRoadAddress() : "" %>" />

                    <label>지번주소</label>
                    <input type="text" id="jibunAddress" disabled
                           value="<%= (addr != null && addr.getJibunAddress() != null) ? addr.getJibunAddress() : "" %>" />
                    <input type="hidden" id="jibunAddressValue" name="jibunAddress"
                           value="<%= (addr != null && addr.getJibunAddress() != null) ? addr.getJibunAddress() : "" %>" />

                    <label>상세주소</label>
                    <input type="text" id="addrDetail"
                           value="<%= (addr != null && addr.getAddrDetail() != null) ? addr.getAddrDetail() : "" %>" />
                    <input type="hidden" id="addrDetailValue" name="addrDetail"
                           value="<%= (addr != null && addr.getAddrDetail() != null) ? addr.getAddrDetail() : "" %>" />

                    <input type="hidden" id="latitude" name="latitude">
                    <input type="hidden" id="longitude" name="longitude">

                    <button type="submit" class="update-btn">정보 수정</button>
                </form>

                <form action="<%= request.getContextPath() %>/user/delete" method="post"
                      onsubmit="return confirm('정말 탈퇴하시겠습니까?');">
                    <input type="hidden" name="autoId" value="<%= user.getAutoId() %>">
                    <button type="submit" class="delete-btn">회원 탈퇴</button>
                </form>
            </div>

            <%-- 가운데: 포인트 / 지갑 / 충전 영역 --%>
            <div class="map-card">
	<div class="map-card">
    <jsp:include page="/WEB-INF/views/wallet/wallet_section.jsp">
	    <jsp:param name="returnUrl" value="/page/mypage" />
	</jsp:include>
	</div>

            <%-- 오른쪽: 알림 / 추천 활동 --%>
            <div class="weather-card">
                <div class="center-title">알림 & 추천</div>
                <ul class="weather-card-list">
                    <li>· 참여 중인 모임 회비 결제 알림 예정</li>
                    <li>· 최근 참여한 모임 기반 추천 모임 노출 예정</li>
                    <li>· 거래/리뷰 관련 알림도 이 영역에서 관리</li>
                </ul>
            </div>

        </div>
    </section>

    <%
        } else {
    %>
    <div class="main-box" style="text-align:center;">
        <p>로그인이 필요합니다.
            <a href="<%= request.getContextPath() %>/page/login">
			    로그인 페이지로 이동
			</a>
        </p>
    </div>
    <%
        }
    %>

</div>

<script>
    // 카카오 Geocoder 객체 생성
    var geocoder = new kakao.maps.services.Geocoder();

    // form submit 가로채기 (주소 → 좌표 변환 후 전송)
    document.addEventListener("DOMContentLoaded", function () {
        var updateForm = document.getElementById("updateForm");
        if (!updateForm) return;

        updateForm.addEventListener("submit", function(e) {
            e.preventDefault();

            let roadAddr = document.getElementById("roadAddressValue").value;
            let jibunAddr = document.getElementById("jibunAddressValue").value;
            let detailInput = document.getElementById("addrDetail");
            let detailHidden = document.getElementById("addrDetailValue");

            if (detailInput && detailHidden) {
                detailHidden.value = detailInput.value;
            }

            let finalAddress = roadAddr || jibunAddr;

            if (!finalAddress) {
                alert("주소가 없습니다. 주소 검색을 먼저 해주세요.");
                return;
            }

            geocoder.addressSearch(finalAddress, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    let lat = result[0].y;
                    let lng = result[0].x;

                    document.getElementById("latitude").value = lat;
                    document.getElementById("longitude").value = lng;

                    e.target.submit();
                } else {
                    alert("주소 → 좌표 변환 실패: " + status);
                }
            });
        });
    });
</script>

</body>
</html>

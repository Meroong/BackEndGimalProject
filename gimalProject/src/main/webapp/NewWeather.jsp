<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.UserAddressDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>모임 생성</title>

<script>
    const POPUP_KEY = "<%= "devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc=" %>";
    const RETURN_URL = "http://localhost:8080<%= request.getContextPath() %>/views/util/addressPopupReturn.jsp";

    function openJusoPopup() {
        window.open(
            "https://business.juso.go.kr/addrlink/addrLinkUrl.do?confmKey=" + POPUP_KEY 
            + "&returnUrl=" + encodeURIComponent(RETURN_URL)
            + "&resultType=4",
            "jusoPopup",
            "width=570,height=420,scrollbars=yes,resizable=yes"
        );
    }
</script>

<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
<% UserAddressDTO addr = (UserAddressDTO) session.getAttribute("addressInfo"); %>

</head>
<body>
<h2>모임 생성</h2>

<form id="insertForm" action="<%= request.getContextPath() %>/meeting/insert" method="post">

    <!-- 주소 영역 -->
    <label>주소</label>
    <button type="button" onclick="openJusoPopup()">주소 검색</button><br>

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
    <input type="text" id="addrDetail" name="addrDetail" 
        value="<%= (addr != null && addr.getAddrDetail() != null) ? addr.getAddrDetail() : "" %>" />

    <!-- 좌표 hidden -->
    <input type="hidden" id="latitude" name="latitude">
    <input type="hidden" id="longitude" name="longitude">
    
    <!-- 모임 정보 -->
    <label>모임 제목</label>
    <input type="text" name="title" value="디폴트 모임" required><br>
    
    <label>내용</label>
    <textarea name="content">디폴트 내용</textarea><br>
    
    <label>모임 날짜</label>
    <input type="date" name="date" value="<%= java.time.LocalDate.now() %>" required><br>
    
    <label>최대 인원</label>
    <input type="number" name="maxMembers" value="10"><br>
    
    <label>현재 인원</label>
    <input type="number" name="currentMembers" value="1"><br>
    
    <label>참가비</label>
    <input type="number" name="cost" value="0"><br>
    
    <label>태그</label>
    <input type="text" name="tag" value="디폴트"><br>
    
    <label>상태</label>
    <select name="status">
        <option value="OPEN" selected>OPEN</option>
        <option value="CLOSED">CLOSED</option>
        <option value="COMPLETED">COMPLETED</option>
    </select><br><br>

    <button type="submit">모임 생성</button>
</form>

<script>
    var geocoder = new kakao.maps.services.Geocoder();

    // submit 전에 주소 → 좌표 변환
    document.getElementById("insertForm").addEventListener("submit", function(e) {
        e.preventDefault();

        let roadAddr = document.getElementById("roadAddressValue").value;
        let jibunAddr = document.getElementById("jibunAddressValue").value;
        let finalAddress = roadAddr || jibunAddr;

        if (!finalAddress) {
            alert("주소가 없습니다. 주소 검색을 먼저 해주세요.");
            return;
        }

        geocoder.addressSearch(finalAddress, function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
                // 좌표 hidden 필드 세팅
                document.getElementById("latitude").value = result[0].y;
                document.getElementById("longitude").value = result[0].x;

                // 실제 폼 제출
                e.target.submit();
            } else {
                alert("주소 → 좌표 변환 실패: " + status);
            }
        });
    });
</script>

</body>
</html>

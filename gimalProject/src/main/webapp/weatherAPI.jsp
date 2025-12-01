<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.UserAddressDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
    <script>
		const POPUP_KEY = "<%= "devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc=" %>"; // 팝업용 키
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

		    <%-- 날씨 테스트 --%>
		    <form id="updateForm" action="<%= request.getContextPath() %>/meeting/update" method="post">
		        
		
		                        <%-- 주소 영역 --%>
                <label>주소</label>
                <button type="button" class="update-btn" onclick="openJusoPopup()" style="width:auto; margin-bottom:10px;">주소 검색</button>

				<!-- 도로명주소 -->
				<label>도로명주소</label>
				<input type="text" id="roadAddress" disabled value="<%= (addr != null && addr.getRoadAddress() != null) ? addr.getRoadAddress() : "" %>" />
				
				<input type="hidden" id="roadAddressValue" name="roadAddress" value="<%= (addr != null && addr.getRoadAddress() != null) ? addr.getRoadAddress() : "" %>" />
				
				
				<!-- 지번주소 disabled라 팝업에서 .textContent가 아니라 .value 사용해야함-->
				<label>지번주소</label>
				<input type="text" id="jibunAddress" disabled value="<%= (addr != null && addr.getJibunAddress() != null) ? addr.getJibunAddress() : "" %>" />
				
				<input type="hidden" id="jibunAddressValue" name="jibunAddress" value="<%= (addr != null && addr.getJibunAddress() != null) ? addr.getJibunAddress() : "" %>" />
				
				<!-- 상세주소 (유저 입력 가능) -->	
				<label>상세주소</label>
				<input type="text" id="addrDetail" value="<%= (addr != null && addr.getAddrDetail() != null) ? addr.getAddrDetail() : "" %>" />

				<input type="hidden" id="addrDetailValue" name="addrDetail" value="<%= (addr != null && addr.getAddrDetail() != null) ? addr.getAddrDetail() : "" %>" />
				
				<input type="hidden" id="latitude" name="latitude">
				<input type="hidden" id="longitude" name="longitude">
				
		        <button type="submit" class="update-btn">게시판 등록</button>
		    </form>
	
<script>
    // 카카오 Geocoder 객체 생성
    var geocoder = new kakao.maps.services.Geocoder();

    // form submit 가로채기
    document.addEventListener("DOMContentLoaded", function () {
        document.getElementById("updateForm").addEventListener("submit", function(e) {
            e.preventDefault();

            let roadAddr = document.getElementById("roadAddressValue").value;
            let jibunAddr = document.getElementById("jibunAddressValue").value;

            let finalAddress = roadAddr || jibunAddr;

            if (!finalAddress) {
                alert("주소가 없습니다. 주소 검색을 먼저 해주세요.");
                return;
            }

            // 주소 → 좌표 변환
            geocoder.addressSearch(finalAddress, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {

                    let lat = result[0].y;
                    let lng = result[0].x;

                    document.getElementById("latitude").value = lat;
                    document.getElementById("longitude").value = lng;

                    console.log("위도:", lat, "경도:", lng);

                    // 실제 제출
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
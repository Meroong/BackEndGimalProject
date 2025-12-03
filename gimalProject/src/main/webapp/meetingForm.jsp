<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.UserAddressDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>모임 생성</title>
<style>
    body {
        font-family: 'Arial', sans-serif;
        margin: 0;
        padding: 0;
        background: #f5f6fa;
    }
    .container {
        width: 600px;
        max-width: 95%;
        margin: 50px auto;
        background: #fff;
        padding: 30px;
        border-radius: 15px;
        box-shadow: 0 6px 20px rgba(0,0,0,0.1);
    }
    h2 {
        text-align: center;
        color: #FF7C40;
    }
    form {
        display: flex;
        flex-direction: column;
        gap: 15px;
    }
    label {
        font-weight: 600;
        margin-bottom: 5px;
    }
    input[type="text"], input[type="number"], input[type="date"], textarea, select {
        padding: 10px;
        border-radius: 8px;
        border: 1px solid #ddd;
        width: 100%;
        box-sizing: border-box;
    }
    textarea {
        resize: vertical;
        min-height: 80px;
    }
    button {
        background: #FF7C40;
        color: #fff;
        border: none;
        padding: 12px;
        border-radius: 10px;
        font-weight: 700;
        cursor: pointer;
        transition: 0.2s;
    }
    button:hover {
        background: #ff6720;
    }
    #imagePreview {
        display: none;
        max-width: 100%;
        margin-top: 10px;
        border-radius: 8px;
        border: 1px solid #ddd;
    }
</style>

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

    function previewImage(event) {
        const file = event.target.files[0];
        const preview = document.getElementById('imagePreview');
        if(file) {
            preview.src = URL.createObjectURL(file);
            preview.style.display = 'block';
        } else {
            preview.style.display = 'none';
            preview.src = '#';
        }
    }
</script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
</head>
<body>

<div class="container">
    <h2>모임 생성</h2>

    <form id="insertForm" action="<%= request.getContextPath() %>/meeting/insert" method="post" enctype="multipart/form-data">

        <!-- 이미지 업로드 -->
        <label>모임 이미지</label>
        <input type="file" id="imageFile" name="imageFile" accept="image/*" onchange="previewImage(event)">
        <img id="imagePreview" src="#" alt="선택한 이미지 미리보기">

        <!-- 주소 영역 -->
        <label>주소</label>
        <button type="button" onclick="openJusoPopup()">주소 검색</button>

        <label>도로명주소</label>
        <input type="text" id="roadAddress" disabled 
            value="<%= (session.getAttribute("addressInfo") != null && ((UserAddressDTO)session.getAttribute("addressInfo")).getRoadAddress() != null) ? ((UserAddressDTO)session.getAttribute("addressInfo")).getRoadAddress() : "" %>" />
        <input type="hidden" id="roadAddressValue" name="roadAddress" 
            value="<%= (session.getAttribute("addressInfo") != null && ((UserAddressDTO)session.getAttribute("addressInfo")).getRoadAddress() != null) ? ((UserAddressDTO)session.getAttribute("addressInfo")).getRoadAddress() : "" %>" />

        <label>지번주소</label>
        <input type="text" id="jibunAddress" disabled 
            value="<%= (session.getAttribute("addressInfo") != null && ((UserAddressDTO)session.getAttribute("addressInfo")).getJibunAddress() != null) ? ((UserAddressDTO)session.getAttribute("addressInfo")).getJibunAddress() : "" %>" />
        <input type="hidden" id="jibunAddressValue" name="jibunAddress" 
            value="<%= (session.getAttribute("addressInfo") != null && ((UserAddressDTO)session.getAttribute("addressInfo")).getJibunAddress() != null) ? ((UserAddressDTO)session.getAttribute("addressInfo")).getJibunAddress() : "" %>" />

        <label>상세주소</label>
        <input type="text" id="addrDetail" name="addrDetail" 
            value="<%= (session.getAttribute("addressInfo") != null && ((UserAddressDTO)session.getAttribute("addressInfo")).getAddrDetail() != null) ? ((UserAddressDTO)session.getAttribute("addressInfo")).getAddrDetail() : "" %>" />

        <!-- 좌표 hidden -->
        <input type="hidden" id="latitude" name="latitude">
        <input type="hidden" id="longitude" name="longitude">

        <!-- 모임 정보 -->
        <label>모임 제목</label>
        <input type="text" name="title" value="디폴트 모임" required>

        <label>내용</label>
        <textarea name="content">디폴트 내용</textarea>

        <label>모임 날짜</label>
        <input type="date" name="date" value="<%= java.time.LocalDate.now() %>" required>

        <label>최대 인원</label>
        <input type="number" name="maxMembers" value="10">

        <label>현재 인원</label>
        <input type="number" name="currentMembers" value="1">

        <label>참가비</label>
        <input type="number" name="cost" value="0">

        <label>태그</label>
        <input type="text" name="tag" value="디폴트">

        <label>상태</label>
        <select name="status">
            <option value="OPEN" selected>OPEN</option>
            <option value="CLOSED">CLOSED</option>
            <option value="COMPLETED">COMPLETED</option>
        </select>

        <button type="submit">모임 생성</button>
    </form>
</div>

<script>
    var geocoder = new kakao.maps.services.Geocoder();

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
                document.getElementById("latitude").value = result[0].y;
                document.getElementById("longitude").value = result[0].x;

                e.target.submit();
            } else {
                alert("주소 → 좌표 변환 실패: " + status);
            }
        });
    });
</script>

</body>
</html>

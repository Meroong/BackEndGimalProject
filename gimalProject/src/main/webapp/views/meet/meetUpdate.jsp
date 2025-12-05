<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.UserAddressDTO"%>
<%@ page import="dto.MeetingInfoDTO"%>
<%
    MeetingInfoDTO m = (MeetingInfoDTO) request.getAttribute("meetingInfo");
    UserAddressDTO addr = (UserAddressDTO) session.getAttribute("addressInfo");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>모임 수정</title>

<style>
    body { font-family: Arial, sans-serif; background: #f5f6fa; padding: 0; margin: 0; }
    .container {
        width: 650px; max-width: 95%; background: #fff; margin: 40px auto;
        padding: 30px; border-radius: 15px; box-shadow: 0 6px 20px rgba(0,0,0,0.1);
    }
    h2 { text-align: center; color: #FF7C40; }
    form { display: flex; flex-direction: column; gap: 15px; }
    input, textarea, select { padding: 10px; border-radius: 8px; border: 1px solid #ddd; width: 100%; box-sizing: border-box; }
    textarea { resize: vertical; min-height: 70px; }
    button { background: #FF7C40; border: none; padding: 12px; border-radius: 10px; color: #fff; font-weight: bold; cursor: pointer; }

    #mainImagePreview {
        width: 100%; height: 250px; object-fit: cover;
        border: 2px solid #ddd; border-radius: 10px; margin-bottom: 10px;
    }

    #thumbnailGrid {
        display: grid;
        grid-template-columns: repeat(5, 1fr);
        gap: 10px;
    }
    .thumb-box {
        width: 100%; padding-top: 100%; position: relative;
        border: 2px dashed #ccc; border-radius: 10px;
        display: flex; justify-content: center; align-items: center;
        font-size: 12px; color: #aaa;
        overflow: hidden;
    }
    .thumb-box img {
        position: absolute; top: 0; left: 0; width: 100%; height: 100%;
        object-fit: cover; cursor: pointer;
    }
</style>

<script>
    let imageList = [];
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

    function addImages(event) {
        const files = Array.from(event.target.files);
        files.forEach(file => { if(imageList.length<5) imageList.push(file); });
        renderThumbnails();
    }

    function renderThumbnails() {
        const grid = document.getElementById("thumbnailGrid");
        const mainPreview = document.getElementById("mainImagePreview");
        grid.innerHTML = "";

        if(imageList.length>0) mainPreview.src = URL.createObjectURL(imageList[0]);

        for(let i=0;i<5;i++){
            const box = document.createElement("div");
            box.classList.add("thumb-box");

            if(imageList[i]){
                const img = document.createElement("img");
                img.src = URL.createObjectURL(imageList[i]);
                img.onclick = ()=> mainPreview.src = img.src;
                box.appendChild(img);
            } else box.innerText = "이미지 없음";

            grid.appendChild(box);
        }
    }

    function submitFormWithCoordinates(form){
        const geocoder = new kakao.maps.services.Geocoder();
        const roadAddr = document.getElementById("roadAddressValue").value;
        const jibunAddr = document.getElementById("jibunAddressValue").value;
        const finalAddr = roadAddr || jibunAddr;

        if(!finalAddr){ alert("주소가 없습니다. 주소 검색을 먼저 해주세요."); return false; }

        geocoder.addressSearch(finalAddr, function(result, status){
            if(status===kakao.maps.services.Status.OK){
                document.getElementById("latitude").value = result[0].y;
                document.getElementById("longitude").value = result[0].x;
                form.submit();
            } else alert("주소 → 좌표 변환 실패: "+status);
        });

        return false;
    }
</script>

<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
</head>

<body>
<div class="container">
    <h2>모임 수정</h2>

    <form id="updateForm" action="<%= request.getContextPath() %>/meeting/update" method="post" enctype="multipart/form-data" onsubmit="return submitFormWithCoordinates(this);">

        <input type="hidden" name="meetingId" value="<%= m.getMeetingId() %>">
        <input type="hidden" name="locationId" value="<%= m.getLocationId() %>">

        <img id="mainImagePreview" src="<%= (m.getImages()!=null&&!m.getImages().isEmpty())? m.getImages().get(0) : "#" %>" alt="메인 이미지">
        <div id="thumbnailGrid">
            <% if(m.getImages()!=null){ for(String img:m.getImages()){ %>
                <div class="thumb-box"><img src="<%= img %>" onclick="document.getElementById('mainImagePreview').src='<%= img %>'"></div>
            <% }} %>
        </div>
        <label>이미지 업로드 (최대 5장)</label>
        <input type="file" accept="image/*" multiple onchange="addImages(event)">

        <label>주소</label>
        <button type="button" onclick="openJusoPopup()">주소 검색</button>

        <label>도로명주소</label>
        <input type="text" id="roadAddress" disabled value="<%= (addr!=null && addr.getRoadAddress()!=null)? addr.getRoadAddress() : "" %>">
        <input type="hidden" id="roadAddressValue" name="roadAddress" value="<%= (addr!=null && addr.getRoadAddress()!=null)? addr.getRoadAddress() : "" %>">

        <label>지번주소</label>
        <input type="text" id="jibunAddress" disabled value="<%= (addr!=null && addr.getJibunAddress()!=null)? addr.getJibunAddress() : "" %>">
        <input type="hidden" id="jibunAddressValue" name="jibunAddress" value="<%= (addr!=null && addr.getJibunAddress()!=null)? addr.getJibunAddress() : "" %>">

        <label>상세주소</label>
        <input type="text" id="addrDetail" name="addrDetail" value="<%= (m.getAddrDetail()!=null)? m.getAddrDetail() : "" %>">

        <input type="hidden" id="latitude" name="latitude" value="<%= (m.getLatitude()!=null)? m.getLatitude() : "" %>">
        <input type="hidden" id="longitude" name="longitude" value="<%= (m.getLongitude()!=null)? m.getLongitude() : "" %>">

        <label>모임 제목</label>
        <input type="text" name="title" value="<%= m.getTitle() %>" required>

        <label>내용</label>
        <textarea name="content"><%= m.getContent() %></textarea>

        <label>모임 날짜</label>
        <input type="date" name="date" value="<%= m.getDate() %>" required>

        <label>최대 인원</label>
        <input type="number" name="maxMembers" value="<%= m.getMaxMembers() %>">

        <label>현재 인원</label>
        <input type="number" name="currentMembers" value="<%= m.getCurrentMembers() %>">

        <label>참가비</label>
        <input type="number" name="cost" value="<%= m.getCost() %>">

        <label>태그</label>
        <input type="text" name="tag" value="<%= m.getTag() %>">

        <label>상태</label>
        <select name="status">
            <option value="OPEN" <%= "OPEN".equals(m.getStatus())?"selected":"" %>>OPEN</option>
            <option value="CLOSED" <%= "CLOSED".equals(m.getStatus())?"selected":"" %>>CLOSED</option>
            <option value="COMPLETED" <%= "COMPLETED".equals(m.getStatus())?"selected":"" %>>COMPLETED</option>
        </select>

        <button type="submit">모임 수정</button>
    </form>
</div>
</body>
</html>

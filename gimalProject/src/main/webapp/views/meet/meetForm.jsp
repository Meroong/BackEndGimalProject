<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dto.UserAddressDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>모임 생성</title>

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

    /* 메인 이미지 */
    #mainImagePreview {
        width: 100%; height: 250px; object-fit: cover;
        border: 2px solid #ddd; border-radius: 10px; margin-bottom: 10px;
    }

    /* 5칸 썸네일 박스 */
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
    /* 태그 */
    .tag-btn {
        padding: 6px 12px;
        border-radius: 20px;
        border: 1px solid #FF7C40;
        background: #fff;
        color: #FF7C40;
        font-size: 12px;
        cursor: pointer;
    }

    .tag-btn.active {
        background: #FF7C40;
        color: #fff;
    }

    .selected-tag {
        padding: 6px 12px;
        border-radius: 20px;
        background: #FF7C40;
        color: #fff;
        font-size: 12px;
        cursor: pointer;
</style>

<script>
    let imageList = []; // 업로드한 이미지 파일들을 저장하는 배열(최대 5개)
    const POPUP_KEY = "<%= "devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc=" %>";
    const RETURN_URL = "http://localhost:8080<%= request.getContextPath() %>/views/util/addressPopupReturn.jsp";
    
    /* =========================
    태그 선택 로직
	 ========================= */
	 const recommendedTags = [
	     "운동","육아","산책","조깅","러닝",
	     "반려견","반려묘","카페","스터디",
	     "독서","취미","여행","사진","게임"
	 ];
	
	 const MAX_TAGS = 5;
	 let selectedTags = [];
	
	 document.addEventListener("DOMContentLoaded", () => {
	     const area = document.getElementById("recommendedTagArea");
	
	     recommendedTags.forEach(tag => {
	         const btn = document.createElement("button");
	         btn.type = "button";
	         btn.className = "tag-btn";
	         btn.dataset.tag = tag;
	         btn.innerText = "#" + tag;
	
	         btn.onclick = () => toggleTag(tag, btn);
	         area.appendChild(btn);
	     });
	 });
	
	 function toggleTag(tag, btn) {
	     const idx = selectedTags.indexOf(tag);
	
	     if (idx !== -1) {
	         selectedTags.splice(idx, 1);
	         btn.classList.remove("active");
	     } else {
	         if (selectedTags.length >= MAX_TAGS) {
	             alert("태그는 최대 5개까지 가능합니다.");
	             return;
	         }
	         selectedTags.push(tag);
	         btn.classList.add("active");
	     }
	
	     renderSelectedTags();
	     syncTagInput();
	 }
	
	 function renderSelectedTags() {
	     const area = document.getElementById("selectedTagArea");
	     area.innerHTML = "";
	
	     selectedTags.forEach(tag => {
	         const chip = document.createElement("span");
	         chip.className = "selected-tag";
	         chip.innerText = "#" + tag + " ✕";
	
	         chip.onclick = () => {
	             selectedTags = selectedTags.filter(t => t !== tag);
	
	             document.querySelectorAll(".tag-btn").forEach(b => {
	                 if (b.dataset.tag === tag) b.classList.remove("active");
	             });
	
	             renderSelectedTags();
	             syncTagInput();
	         };
	
	         area.appendChild(chip);
	     });
	 }
	
	 function syncTagInput() {
	     document.getElementById("tagInput").value = selectedTags.join(",");
	 }
    
    // 주소 검색 팝업
    function openJusoPopup() {
        window.open(
            "https://business.juso.go.kr/addrlink/addrLinkUrl.do?confmKey=" + POPUP_KEY 
            + "&returnUrl=" + encodeURIComponent(RETURN_URL)
            + "&resultType=4",
            "jusoPopup",
            "width=570,height=420,scrollbars=yes,resizable=yes"
        );
    }

    // 이미지 추가
    function addImages(event) {
        const files = Array.from(event.target.files);
        files.forEach(file => {
            if (imageList.length >= 5) return;  
            imageList.push(file);
        });
        renderThumbnails();
    }

    // 썸네일 + 메인 이미지 갱신
    function renderThumbnails() {
        const grid = document.getElementById("thumbnailGrid");
        const mainPreview = document.getElementById("mainImagePreview");
        grid.innerHTML = "";

        if (imageList.length > 0) {
            mainPreview.src = URL.createObjectURL(imageList[0]);
        }

        for (let i = 0; i < 5; i++) {
            const box = document.createElement("div");
            box.classList.add("thumb-box");

            if (imageList[i]) {
                const img = document.createElement("img");
                img.src = URL.createObjectURL(imageList[i]);
                img.onclick = () => mainPreview.src = img.src;
                box.appendChild(img);
            } else {
                box.innerText = "이미지 없음";
            }
            grid.appendChild(box);
        }
    }

    // form submit 전에 좌표 가져오기
    function submitFormWithCoordinates(form) {
        const geocoder = new kakao.maps.services.Geocoder();
        const roadAddr = document.getElementById("roadAddressValue").value;
        const jibunAddr = document.getElementById("jibunAddressValue").value;
        const finalAddress = roadAddr || jibunAddr;

        if (!finalAddress) {
            alert("주소가 없습니다. 주소 검색을 먼저 해주세요.");
            return false;
        }

        // 좌표 검색 후 submit
        geocoder.addressSearch(finalAddress, function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
                document.getElementById("latitude").value = result[0].y;
                document.getElementById("longitude").value = result[0].x;
                form.submit(); // 서버로 전송
            } else {
                alert("주소 → 좌표 변환 실패: " + status);
            }
        });

        return false; // form 기본 submit 막음, 좌표 완료 후 submit
    }
</script>

<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
</head>

<body>
<div class="container">
    <h2>모임 생성</h2>

    <form id="insertForm" action="<%= request.getContextPath() %>/meeting/insert"
          method="post" enctype="multipart/form-data"
          onsubmit="return submitFormWithCoordinates(this);">

        <!-- 대표 이미지 -->
        <img id="mainImagePreview" src="#" alt="메인 이미지">

        <!-- 5칸 썸네일 -->
        <div id="thumbnailGrid"></div>

        <!-- 이미지 업로드 input -->
        <label>이미지 업로드 (최대 5장)</label>
        <input type="file" accept="image/*" name = "images" multiple onchange="addImages(event)">

        <!-- 주소 검색 -->
        <label>주소</label>
        <button type="button" onclick="openJusoPopup()">주소 검색</button>

        <label>도로명주소</label>
        <input type="text" id="roadAddress" disabled
             value="<%= (session.getAttribute("addressInfo") != null 
                 && ((UserAddressDTO)session.getAttribute("addressInfo")).getRoadAddress() != null) 
                 ? ((UserAddressDTO)session.getAttribute("addressInfo")).getRoadAddress() : "" %>">
        <input type="hidden" id="roadAddressValue" name="roadAddress"
             value="<%= (session.getAttribute("addressInfo") != null 
                 && ((UserAddressDTO)session.getAttribute("addressInfo")).getRoadAddress() != null) 
                 ? ((UserAddressDTO)session.getAttribute("addressInfo")).getRoadAddress() : "" %>">

        <label>지번주소</label>
        <input type="text" id="jibunAddress" disabled
             value="<%= (session.getAttribute("addressInfo") != null 
                 && ((UserAddressDTO)session.getAttribute("addressInfo")).getJibunAddress() != null) 
                 ? ((UserAddressDTO)session.getAttribute("addressInfo")).getJibunAddress() : "" %>">
        <input type="hidden" id="jibunAddressValue" name="jibunAddress"
             value="<%= (session.getAttribute("addressInfo") != null 
                 && ((UserAddressDTO)session.getAttribute("addressInfo")).getJibunAddress() != null) 
                 ? ((UserAddressDTO)session.getAttribute("addressInfo")).getJibunAddress() : "" %>">

        <label>상세주소</label>
        <input type="text" id="addrDetail" name="addrDetail">
        <input type="hidden" id="addrDetailValue" name="addrDetail">

        <input type="hidden" id="latitude" name="latitude">
        <input type="hidden" id="longitude" name="longitude">

        <label>모임 제목</label>
        <input type="text" name="title" required>

        <label>내용</label>
        <textarea name="content"></textarea>

        <label>모임 날짜</label>
        <%
            String today = java.time.LocalDate.now().toString();
        %>
        <input type="datetime-local" name="date" value="<%= today %>" required>

        <label>최대 인원</label>
        <input type="number" name="maxMembers" value="10">

        <label>현재 인원</label>
        <input type="number" name="currentMembers" value="1">

        <label>참가비</label>
        <input type="number" name="cost" value="0">

		<label>태그 선택 (최대 5개)</label>
		
        <label>선택된 태그</label>
        <div id="selectedTagArea" style="display:flex; gap:8px; flex-wrap:wrap;"></div>

        <input type="hidden" name="tag" id="tagInput">

        <label>추천 태그 (클릭해서 선택)</label>
        <div id="recommendedTagArea" style="display:flex; gap:8px; flex-wrap:wrap;"></div>


        

        <label>상태</label>
        <select name="status">
            <option value="OPEN">OPEN</option>
            <option value="CLOSED">CLOSED</option>
            <option value="COMPLETED">COMPLETED</option>
        </select>

        <button type="submit">생성하기</button>
    </form>
</div>
</body>
</html>

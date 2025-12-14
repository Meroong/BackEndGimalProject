<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="dto.UserAddressDTO"%>
<%@ page import="dto.MeetingInfoDTO"%>

<%	// 기조에는 수정 시 모임날짜를 매번 입력해야했음 그리고 이전 날짜도 선택가능했고 7일 이후 날짜도 선택가능했어서 만든 기능 
    MeetingInfoDTO m = (MeetingInfoDTO) request.getAttribute("meetingInfo");

    // 모임 날짜 포맷 (yyyy-MM-dd)
    String meetingDateStr = "";
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
    if(m.getDate() != null) {
        meetingDateStr = sdf.format(m.getDate());
    }

    // 오늘 날짜와 최대 날짜(7일 후) 설정
    java.util.Calendar cal = java.util.Calendar.getInstance();
    String today = sdf.format(cal.getTime());
    cal.add(java.util.Calendar.DATE, 7);
    String maxDate = sdf.format(cal.getTime());
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
        font-size: 12px; color: #aaa; overflow: hidden;
    }
    .thumb-box img {
        position: absolute; top: 0; left: 0; width: 100%; height: 100%;
        object-fit: cover; cursor: pointer;
    }
    .delete-btn {
        position: absolute; top: 3px; right: 3px;
        background: rgba(0,0,0,0.6); color: white;
        padding: 3px 5px; font-size: 10px; border-radius: 4px; cursor: pointer;
    }
    
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
	}
</style>

<script>
    let newImageList = [];      // 새 업로드 이미지
    let existingImageList = []; // 기존 이미지 {id, url}
    let deletedImageIds = [];   // 삭제할 기존 이미지 ID 저장

    <% if (m.getImages() != null) { %>
        <% for (dto.FileResourceDTO img : m.getImages()) { %>
            existingImageList.push({
                id: "<%= img.getId() %>",
                url: "<%= img.getFileUrl() %>"
            });
        <% } %>
    <% } %>

    function renderThumbnails() {
        const grid = document.getElementById("thumbnailGrid");
        grid.innerHTML = "";
        const mainPreview = document.getElementById("mainImagePreview");

        let allImages = [];

        // 기존 이미지
        existingImageList.forEach(img => allImages.push({ type:"old", ...img }));

        // 새 이미지
        newImageList.forEach((file, idx) => {
            allImages.push({ type:"new", file:file, url:URL.createObjectURL(file), index:idx });
        });

        if (allImages.length > 0) {
            mainPreview.src = allImages[0].url;
        }

        for (let i = 0; i < 5; i++) {
            const box = document.createElement("div");
            box.classList.add("thumb-box");

            const imgObj = allImages[i];

            if (imgObj) {
                const img = document.createElement("img");
                img.src = imgObj.url;
                img.onclick = () => mainPreview.src = img.src;
                box.appendChild(img);

                const del = document.createElement("div");
                del.classList.add("delete-btn");
                del.innerText = "X";

                // 기존 이미지 삭제
                if (imgObj.type === "old") {
                    del.onclick = () => deleteOldImage(imgObj.id);
                } 
                // 새 이미지 삭제
                else {
                    del.onclick = () => deleteNewImage(imgObj.index);
                }

                box.appendChild(del);
            } else {
                box.innerText = "이미지 없음";
            }

            grid.appendChild(box);
        }
    }

    function deleteOldImage(id) {
        deletedImageIds.push(id);
        existingImageList = existingImageList.filter(img => img.id !== id);
        document.getElementById("deletedImageIds").value = deletedImageIds.join(",");
        renderThumbnails();
    }

    function deleteNewImage(index) {
        newImageList.splice(index, 1);
        renderThumbnails();
    }

    function addImages(event) {
        const files = Array.from(event.target.files);

        files.forEach(file => {
            if (existingImageList.length + newImageList.length < 5) {
                newImageList.push(file);
            }
        });

        renderThumbnails();
    }

    /* 주소 좌표 변환 */
    function submitFormWithCoordinates(form){
        const geocoder = new kakao.maps.services.Geocoder();
        const roadAddr = document.getElementById("roadAddressValue").value;
        const jibunAddr = document.getElementById("jibunAddressValue").value;
        const finalAddr = roadAddr || jibunAddr;

        if (!finalAddr) {
            alert("주소가 없습니다. 주소 검색을 먼저 해주세요.");
            return false;
        }

        geocoder.addressSearch(finalAddr, function(result, status) {
            if(status === kakao.maps.services.Status.OK){
                document.getElementById("latitude").value = result[0].y;
                document.getElementById("longitude").value = result[0].x;
                form.submit();
            } else {
                alert("주소 → 좌표 변환 실패: " + status);
            }
        });

        return false;
    }
    const POPUP_KEY = "<%= "devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc=" %>";
    const RETURN_URL =
        "<%= request.getContextPath() %>/page/addressPopupReturn?mode=mypage";
    function openJusoPopup() {
        window.open(
            "https://business.juso.go.kr/addrlink/addrLinkUrl.do?confmKey=" + POPUP_KEY
            + "&returnUrl=" + encodeURIComponent(RETURN_URL)
            + "&resultType=4",
            "jusoPopup",
            "width=570,height=420,scrollbars=yes,resizable=yes"
        );
    }
    
    /* =========================
    태그 선택 로직 (수정)
	 ========================= */
	 const recommendedTags = [
	     "운동","육아","산책","조깅","러닝",
	     "반려견","반려묘","카페","스터디",
	     "독서","취미","여행","사진","게임"
	 ];
	
	 const MAX_TAGS = 5;
	
	 // 🔥 기존 태그를 JS 배열로 변환
	 let selectedTags = [];
	 <%
	     if (m.getTag() != null && !m.getTag().isBlank()) {
	         for (String t : m.getTag().split(",")) {
	 %>
	 selectedTags.push("<%= t.trim() %>");
	 <%
	         }
	     }
	 %>
	
	 document.addEventListener("DOMContentLoaded", () => {
	     const area = document.getElementById("recommendedTagArea");
	
	     recommendedTags.forEach(tag => {
	         const btn = document.createElement("button");
	         btn.type = "button";
	         btn.className = "tag-btn";
	         btn.dataset.tag = tag;
	         btn.innerText = "#" + tag;
	
	         if (selectedTags.includes(tag)) {
	             btn.classList.add("active");
	         }
	
	         btn.onclick = () => toggleTag(tag, btn);
	         area.appendChild(btn);
	     });
	
	     renderSelectedTags();
	     syncTagInput();
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
</script>

<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=ef8233e9a835b606aa5918095ec92f2b&libraries=services"></script>
</head>

<body onload="renderThumbnails()">
<div class="container">
    <h2>모임 수정</h2>

    <form id="updateForm" action="<%= request.getContextPath() %>/meeting/update"
          method="post" enctype="multipart/form-data"
          onsubmit="return submitFormWithCoordinates(this);">

        <input type="hidden" name="meetingId" value="<%= m.getMeetingId() %>">
        <input type="hidden" name="locationId" value="<%= m.getLocationId() %>">
        <input type="hidden" id="deletedImageIds" name="deleteImageIds">

        <img id="mainImagePreview" alt="메인 이미지">

        <div id="thumbnailGrid"></div>

        <label>이미지 업로드 (최대 5장)</label>
        <input type="file" accept="image/*" name="images" multiple onchange="addImages(event)">

        <label>주소</label>
        <button type="button" onclick="openJusoPopup()">주소 검색</button>

        <label>도로명주소</label>
		<input type="text" id="roadAddress" disabled
		       value="<%= m.getRoadAddress() != null ? m.getRoadAddress() : "" %>">
		
		<input type="hidden" id="roadAddressValue" name="roadAddress"
		       value="<%= m.getRoadAddress() != null ? m.getRoadAddress() : "" %>">

        <label>지번주소</label>
		<input type="text" id="jibunAddress" disabled
		       value="<%= m.getJibunAddress() != null ? m.getJibunAddress() : "" %>">
		
		<input type="hidden" id="jibunAddressValue" name="jibunAddress"
		       value="<%= m.getJibunAddress() != null ? m.getJibunAddress() : "" %>">

        <label>상세주소</label>
		<input type="text" id="addrDetail" name="addrDetail"
		       value="<%= m.getAddrDetail() != null ? m.getAddrDetail() : "" %>">


        <input type="hidden" id="latitude" name="latitude" value="<%= m.getLatitude() %>">
        <input type="hidden" id="longitude" name="longitude" value="<%= m.getLongitude() %>">

        <label>모임 제목</label>
        <input type="text" name="title" value="<%= m.getTitle() %>" required>

        <label>내용</label>
        <textarea name="content"><%= m.getContent() %></textarea>

        <label>모임 날짜</label>
        <input type="date" name="date" value="<%= meetingDateStr %>" min="<%= today %>" max="<%= maxDate %>" required>

        <label>최대 인원</label>
        <input type="number" name="maxMembers" value="<%= m.getMaxMembers() %>">

        <label>현재 인원</label>
        <input type="number" name="currentMembers" value="<%= m.getCurrentMembers() %>">

        <label>참가비</label>
        <input type="number" name="cost" value="<%= m.getCost() %>">

		<label>태그 선택 (최대 5개)</label>
		
		<label>선택된 태그</label>
		<div id="selectedTagArea" style="display:flex; gap:8px; flex-wrap:wrap;"></div>
		
		<input type="hidden" name="tag" id="tagInput">
		
		<label>추천 태그 (클릭해서 선택)</label>
		<div id="recommendedTagArea" style="display:flex; gap:8px; flex-wrap:wrap;"></div>

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

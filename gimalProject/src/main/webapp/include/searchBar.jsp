<%@ page contentType="text/html;charset=UTF-8" %>

<div class="common-search-bar">

    <!-- 📍 지역 버튼 -->
    <button type="button"
            class="location-btn"
            onclick="openJusoPopup()">
        📍 <span id="currentDong">
            ${sessionScope.addressInfo != null
                ? sessionScope.addressInfo.dongName
                : "우리 동네"}
        </span>
    </button>

    <!-- 🔍 검색어 -->
    <input type="text"
           name="keyword"
           placeholder="제목 또는 내용을 검색하세요"
           value="${param.keyword != null ? param.keyword : ''}" />

    <!-- 검색 버튼 -->
    <button type="submit" class="search-btn">검색</button>

    <!-- ✅ 주소 hidden (단 하나만) -->
    <input type="hidden"
           name="dong"
           id="dongInput"
           value="${sessionScope.addressInfo != null
                   ? sessionScope.addressInfo.dongName
                   : ''}">
</div>

<script>
function openJusoPopup() {
    window.open(
        "https://business.juso.go.kr/addrlink/addrLinkUrl.do"
        + "?confmKey=devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc="
        + "&returnUrl=" + encodeURIComponent(
            "http://localhost:8080/gimalProject/views/util/addressPopupReturn.jsp"
          )
        + "&resultType=4",
        "jusoPopup",
        "width=570,height=420,scrollbars=yes,resizable=yes"
    );
}

// 🔥 addressPopupReturn.jsp 에서 호출됨
function setSelectedAddress(dongName) {
    document.getElementById("currentDong").innerText = dongName;
    document.getElementById("dongInput").value = dongName;
}
</script>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%
    String mode = request.getParameter("mode");
    if (mode == null) mode = "home";
%>

<form id="commonSearchForm" method="get">
<section class="search-section">
    <div class="search-row">

        <%-- 📍 우리동네 : home에서만 표시 --%>
        <% if ("home".equals(mode)) { %>
        <button type="button"
                class="select-like"
                onclick="openJusoPopup()">
            📍 <span id="currentDong">
                ${sessionScope.addressInfo != null
                    ? sessionScope.addressInfo.dongName
                    : "우리 동네"}
            </span>
        </button>
        <% } %>

        <%-- 대상 선택 --%>
        <% if ("home".equals(mode)) { %>
            <select id="targetSelect" name="target">
                <option value="meeting">모임</option>
                <option value="dream">드림</option>
            </select>
        <% } else { %>
            <!-- meeting/list에서는 모임 고정 -->
            <input type="hidden" name="target" value="meeting">
        <% } %>

        <!-- 검색어 -->
        <input type="text"
               name="keyword"
               placeholder="검색어를 입력해주세요"
               value="${param.keyword != null ? param.keyword : ''}">

        <button type="submit" class="search-btn">검색</button>

        <!-- 동 정보 (home에서만 의미 있음) -->
        <input type="hidden"
               name="dong"
               id="dongInput"
               value="${sessionScope.addressInfo != null
                       ? sessionScope.addressInfo.dongName
                       : ''}">
    </div>
</section>
</form>


<script>
document.getElementById("commonSearchForm").addEventListener("submit", function () {
    const targetInput = document.querySelector("[name='target']");
    const target = targetInput ? targetInput.value : "meeting";

    if (target === "dream") {
        this.action = "<%= request.getContextPath() %>/dream/list.do";
    } else {
        this.action = "<%= request.getContextPath() %>/meeting/list";
    }
});

function openJusoPopup() {
    window.open(
        "https://business.juso.go.kr/addrlink/addrLinkUrl.do"
        + "?confmKey=devU01TX0FVVEgyMDI1MTEyNDEwMTMwNjExNjQ4NTc="
        + "&returnUrl=" + encodeURIComponent(
            "http://localhost:8080/gimalProject/views/util/addressPopupReturn.jsp?mode=search"
        )
        + "&resultType=4",
        "jusoPopup",
        "width=570,height=420,scrollbars=yes,resizable=yes"
    );
}

function setSelectedAddress(dongName) {
    document.getElementById("currentDong").innerText = dongName;
    document.getElementById("dongInput").value = dongName;
}
</script>

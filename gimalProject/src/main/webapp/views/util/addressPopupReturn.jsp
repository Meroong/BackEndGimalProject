<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String mode = request.getParameter("mode"); // search | mypage
    String roadAddr = request.getParameter("roadAddrPart1");
    String jibunAddr = request.getParameter("jibunAddr");
    String addrDetail = request.getParameter("addrDetail");

    String dongName = "";
    if (jibunAddr != null) {
        String[] parts = jibunAddr.split(" ");
        dongName = parts[parts.length - 1];
    }
%>

<script>
if (window.opener && !window.opener.closed) {

    // 🔹 공통: input 채우기
    function setValue(id, val) {
        const el = opener.document.getElementById(id);
        if (el) el.value = val || "";
    }

    setValue("roadAddress", "<%= roadAddr %>");
    setValue("jibunAddress", "<%= jibunAddr %>");
    setValue("addrDetail", "<%= addrDetail %>");

    const mode = "<%= mode %>";

    // =========================
    // 🔍 공통 검색바에서 열림
    // =========================
    if (mode === "search") {
        if (opener.setSelectedAddress) {
            opener.setSelectedAddress("<%= dongName %>");
        }

        // 👉 서버 반영은 "검색 버튼" 또는 즉시 submit에서 처리
        if (opener.onSearchAddressSelected) {
            opener.onSearchAddressSelected({
                roadAddress: "<%= roadAddr %>",
                jibunAddress: "<%= jibunAddr %>",
                dongName: "<%= dongName %>"
            });
        }
    }

    // =========================
    // 👤 마이페이지에서 열림
    // =========================
    if (mode === "mypage") {
        // ❗ 여기서는 DB 업데이트 절대 안 함
        // 사용자가 "수정하기" 눌러야 UserController 탄다
    }

    window.close();
}
</script>

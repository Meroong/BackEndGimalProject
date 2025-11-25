<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 팝업에서 전달되는 주소 파라미터
    String roadAddr = request.getParameter("roadAddrPart1");
    String jibunAddr = request.getParameter("jibunAddr");
    String addrDetail = request.getParameter("addrDetail");
%>
<script>
if (window.opener) {
    // 부모창의 읽기 전용 span + hidden input 업데이트
    window.opener.document.getElementById("roadAddress").textContent = "<%= roadAddr %>";
    window.opener.document.getElementById("roadAddressValue").value = "<%= roadAddr %>";

    window.opener.document.getElementById("jibunAddress").textContent = "<%= jibunAddr %>";
    window.opener.document.getElementById("jibunAddressValue").value = "<%= jibunAddr %>";
    
    window.opener.document.getElementById("addrDetail").textContent = "<%= addrDetail != null ? addrDetail.replace("\"", "\\\"") : "" %>";
    window.opener.document.getElementById("addrDetailValue").value = "<%= addrDetail != null ? addrDetail.replace("\"", "\\\"") : "" %>";



    // 팝업 닫기
    window.close();
} else {
    alert("주소 선택 실패 또는 부모 창 없음");
    window.close();
}
</script>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 팝업에서 전달되는 주소 파라미터
    String roadAddr = request.getParameter("roadAddrPart1");
    String jibunAddr = request.getParameter("jibunAddr");
    String addrDetail = request.getParameter("addrDetail");

    // ====== 🔥 디버깅용 출력 ======
    System.out.println("==== [주소 API 반환값 확인] ====");
    System.out.println("roadAddrPart1 = " + roadAddr);
    System.out.println("jibunAddr     = " + jibunAddr);
    System.out.println("addrDetail    = " + addrDetail);
    System.out.println("===============================");
%>
<script>
if (window.opener) {


    // 도로명주소
    window.opener.document.getElementById("roadAddress").value = "<%= roadAddr %>";
    window.opener.document.getElementById("roadAddressValue").value = "<%= roadAddr %>";

    // 지번주소
    window.opener.document.getElementById("jibunAddress").value = "<%= jibunAddr %>";
    window.opener.document.getElementById("jibunAddressValue").value = "<%= jibunAddr %>";

    // 상세주소
    window.opener.document.getElementById("addrDetail").value = "<%= addrDetail != null ? addrDetail.replace("\"", "\\\"") : "" %>";
    window.opener.document.getElementById("addrDetailValue").value = "<%= addrDetail != null ? addrDetail.replace("\"", "\\\"") : "" %>";
    //공통 검색바에서 열렸을 때
	 if (opener.setSelectedAddress) {
	     opener.setSelectedAddress(dongName);
	 }
    
    window.close();
}
</script>

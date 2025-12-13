<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String mode = request.getParameter("mode"); // search | mypage
    if (mode == null) mode = "search";

    String roadAddr   = request.getParameter("roadAddrPart1");
    String jibunAddr  = request.getParameter("jibunAddr");
    String addrDetail = request.getParameter("addrDetail");

    // JS 문자열 안전 처리 (따옴표/개행 등)
    String safeRoad   = (roadAddr == null) ? "" : roadAddr.replace("\\", "\\\\").replace("\"", "\\\"");
    String safeJibun  = (jibunAddr == null) ? "" : jibunAddr.replace("\\", "\\\\").replace("\"", "\\\"");
    String safeDetail = (addrDetail == null) ? "" : addrDetail.replace("\\", "\\\\").replace("\"", "\\\"");
%>

<script>
(function () {
  if (!window.opener || window.opener.closed) return;

  const mode = "<%= mode %>";
  const roadAddr = "<%= safeRoad %>";
  const jibunAddr = "<%= safeJibun %>";
  const addrDetail = "<%= safeDetail %>";

  // ✅ 동/읍/면/리 추출 (도로명 말고 지번 우선!)
  function extractDong(addr) {
    if (!addr) return "";
    const parts = addr.split(/\s+/);
    for (let i = parts.length - 1; i >= 0; i--) {
      const p = parts[i];
      // 동/읍/면/리/가 로 끝나는 토큰을 찾기
      if (/[동읍면리]$/.test(p) || /[가]$/.test(p)) return p;
    }
    return parts[parts.length - 1] || "";
  }
  const dongName = extractDong(jibunAddr) || extractDong(roadAddr);

  // opener에 input 채우는 용도 (mypage)
  function setValue(id, val) {
    const el = opener.document.getElementById(id);
    if (el) el.value = val || "";
  }

  // =========================
  // 👤 마이페이지: 기존대로 input만 채우고 종료
  // =========================
  if (mode === "mypage") {
    setValue("roadAddress", roadAddr);
    setValue("roadAddressValue", roadAddr);

    setValue("jibunAddress", jibunAddr);
    setValue("jibunAddressValue", jibunAddr);

    setValue("addrDetail", addrDetail);
    setValue("addrDetailValue", addrDetail);

    window.close();
    return;
  }

  // =========================
  // 🔍 search(home): 좌표까지 구해서 updateAddress로 POST
  // (fetch/ajax 안 씀, form submit)
  // =========================

  // 1) UI 먼저 바꿔주기(표시용)
  if (opener.setSelectedAddress) opener.setSelectedAddress(dongName);

  // 2) opener에 kakao geocoder 준비될 때까지 기다렸다가 좌표 변환
  const maxWait = 50; // 50 * 100ms = 5초 정도
  let tries = 0;

  function whenKakaoReady(cb) {
    const ok = opener.kakao && opener.kakao.maps && opener.kakao.maps.services && opener.kakao.maps.services.Geocoder;
    if (ok) return cb();
    tries++;
    if (tries > maxWait) {
      // kakao가 없으면 좌표 못 구함 → 그래도 주소만 보내기(맵/날씨는 기본값일 수 있음)
      submitUpdate("", "");
      return;
    }
    setTimeout(() => whenKakaoReady(cb), 100);
  }

  function submitUpdate(lat, lng) {
    const form = opener.document.createElement("form");
    form.method = "post";
    form.action = "<%= request.getContextPath() %>/user/updateAddress";

    function add(name, value) {
      const input = opener.document.createElement("input");
      input.type = "hidden";
      input.name = name;
      input.value = value || "";
      form.appendChild(input);
    }

    add("roadAddress", roadAddr);
    add("jibunAddress", jibunAddr);
    add("addrDetail", addrDetail);
    add("latitude", lat);
    add("longitude", lng);

    opener.document.body.appendChild(form);
    form.submit();
    window.close();
  }

  // 실제 좌표 변환 실행 (도로명 우선, 없으면 지번)
  whenKakaoReady(() => {
    const geocoder = new opener.kakao.maps.services.Geocoder();
    const query = roadAddr || jibunAddr;

    if (!query) {
      submitUpdate("", "");
      return;
    }

    geocoder.addressSearch(query, function(result, status) {
      if (status === opener.kakao.maps.services.Status.OK && result && result[0]) {
        const lat = result[0].y; // 위도
        const lng = result[0].x; // 경도
        submitUpdate(lat, lng);
      } else {
        // 변환 실패해도 주소는 반영
        submitUpdate("", "");
      }
    });
  });

})();
</script>

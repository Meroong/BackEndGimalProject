<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- include/reportModal.jsp -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/common.css">

<!-- 신고 모달 -->
<div id="reportModalOverlay" class="modal-overlay report-modal-overlay" aria-hidden="true">
  <div class="modal report-modal" role="dialog" aria-modal="true" aria-labelledby="reportModalTitle">

    <div class="modal__header">
      <div class="modal__title" id="reportModalTitle">🚨 신고하기</div>
      <button type="button" class="icon-btn" aria-label="닫기" onclick="closeReportModal()">✕</button>
    </div>

    <form id="reportForm" method="post" action="<%= request.getContextPath() %>/report/create">
      <input type="hidden" name="targetType" id="reportTargetType">
      <input type="hidden" name="targetUserId" id="reportTargetUserId">
      <input type="hidden" name="meetingId" id="reportMeetingId">

      <div class="modal__body">
        <div class="field">
          <label for="reportReason">신고 사유</label>
          <textarea id="reportReason"
                    name="reason"
                    class="textarea textarea--lg"
                    placeholder="신고 사유를 입력해주세요"></textarea>
        </div>
      </div>

      <div class="modal__footer">
        <button type="button" class="btn btn--outline" onclick="closeReportModal()">취소</button>
        <button type="submit" class="btn btn--danger">신고하기</button>
      </div>
    </form>

  </div>
</div>

<script>
function openReportModal(targetType, targetUserId, meetingId) {
  document.getElementById("reportTargetType").value = targetType;
  document.getElementById("reportTargetUserId").value = targetUserId || "";
  document.getElementById("reportMeetingId").value = meetingId || "";

  var overlay = document.getElementById("reportModalOverlay");
  overlay.style.display = "flex";
  overlay.setAttribute("aria-hidden", "false");
}

function closeReportModal() {
  var overlay = document.getElementById("reportModalOverlay");
  overlay.style.display = "none";
  overlay.setAttribute("aria-hidden", "true");
}
</script>

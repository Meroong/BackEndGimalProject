<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
/* 모달 배경 */
.report-modal-overlay {
    display: none;
    position: fixed;
    top: 0; left: 0;
    width: 100%; height: 100%;
    background: rgba(0,0,0,0.45);
    justify-content: center;
    align-items: center;
    z-index: 9999;
}

/* 모달 박스 */
.report-modal {
    background: #fff;
    width: 420px;
    padding: 25px;
    border-radius: 16px;
    box-shadow: 0 6px 20px rgba(0,0,0,0.25);
    animation: fadeIn 0.25s ease-out;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(-15px); }
    to { opacity: 1; transform: translateY(0); }
}

.report-modal h3 {
    margin-top: 0;
    font-size: 20px;
    font-weight: 700;
    color: #FF4E4E;
}

.report-modal textarea {
    width: 100%;
    height: 90px;
    resize: none;
    padding: 10px;
    border-radius: 10px;
    border: 1px solid #ccc;
}

.report-modal .modal-btns {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
    gap: 12px;
}

.modal-btn {
    padding: 8px 16px;
    border-radius: 10px;
    border: none;
    cursor: pointer;
    font-weight: 600;
}

.cancel-btn {
    background: #ddd;
}

.submit-btn {
    background: #FF4E4E;
    color: white;
}
</style>

<!-- 신고 모달 HTML -->
<div id="reportModalOverlay" class="report-modal-overlay">
    <div class="report-modal">
        <h3>🚨 신고하기</h3>

        <form id="reportForm" method="post" action="<%= request.getContextPath() %>/report/create">
            <input type="hidden" name="targetType" id="reportTargetType">
            <input type="hidden" name="targetUserId" id="reportTargetUserId">
            <input type="hidden" name="meetingId" id="reportMeetingId"> 

            <label>신고 사유</label>
            <textarea name="reason" placeholder="신고 사유를 입력해주세요"></textarea>

            <div class="modal-btns">
                <button type="button" class="modal-btn cancel-btn" onclick="closeReportModal()">취소</button>
                <button type="submit" class="modal-btn submit-btn">신고하기</button>
            </div>
        </form>
    </div>
</div>

<script>
function openReportModal(targetType, targetUserId, meetingId) {
    document.getElementById("reportTargetType").value = targetType;
    document.getElementById("reportTargetUserId").value = targetUserId || "";
    document.getElementById("reportMeetingId").value = meetingId || "";

    document.getElementById("reportModalOverlay").style.display = "flex";
}

function closeReportModal() {
    document.getElementById("reportModalOverlay").style.display = "none";
}
</script>

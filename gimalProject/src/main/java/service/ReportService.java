package service;

import dao.ReportDAO;
import dto.ReportDTO;

public class ReportService {

    private ReportDAO reportDAO = new ReportDAO();

    // 신고 생성
    public boolean createReport(ReportDTO dto) throws Exception {

        // 기본 유효성 검사
        if (dto.getReporterId() <= 0) {
            throw new Exception("신고자 정보가 올바르지 않습니다.");
        }
        if (dto.getTargetUserId() <= 0) {
            throw new Exception("대상자 정보가 올바르지 않습니다.");
        }
        if (dto.getTargetType() == null || dto.getTargetType().isEmpty()) {
            throw new Exception("신고 타입이 올바르지 않습니다.");
        }
        if (dto.getReason() == null || dto.getReason().trim().isEmpty()) {
            throw new Exception("신고 사유를 입력해주세요.");
        }

        // DAO 호출
        return reportDAO.insertReport(dto);
    }
    public boolean hasAlreadyReported(long reporterId, long targetUserId, String type) {
        return reportDAO.hasReported(reporterId, targetUserId, type);
    }
}

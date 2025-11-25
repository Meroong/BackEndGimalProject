package service;

import java.util.List;

import dao.AdminReportDAO;
import dto.ReportDTO;

public class AdminReportService {

    private AdminReportDAO adminReportDAO = new AdminReportDAO();

    // 신고 전체 목록
    public List<ReportDTO> getReportList() {
        return adminReportDAO.findAll();
    }
    // 신고 한 건 조회 (id로 찾기)
    public ReportDTO getReportById(long id) {
        return adminReportDAO.findAll()
                .stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

package service;

import java.util.List;

import dao.AdminReportDAO;
import dto.ReportDTO;

public class AdminReportService {

    private AdminReportDAO adminReportDAO = new AdminReportDAO();

    // 신고 전체 목록 가져오기
    public List<ReportDTO> getReportList() {
        return adminReportDAO.findAll();
    }
}

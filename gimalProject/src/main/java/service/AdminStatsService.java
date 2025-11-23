package service;

import dao.AdminStatsDAO;
import dto.AdminStatsDTO;

public class AdminStatsService {

    private AdminStatsDAO adminStatsDAO = new AdminStatsDAO();

    // 통계 한 번에 가져오기
    public AdminStatsDTO getStats() {
        return adminStatsDAO.getStats();
    }
}

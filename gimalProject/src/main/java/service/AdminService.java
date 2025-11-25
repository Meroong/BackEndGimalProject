package service;

import java.util.List;

import dao.AdminNoticeDAO;
import dao.AdminReportDAO;
import dao.AdminStatsDAO;
import dao.AdminUserDAO;
import dao.UserDAO;
import dto.AdminNoticeDTO;
import dto.ReportDTO;
import dto.AdminStatsDTO;
import dto.UserDTO;

public class AdminService {

    // DAO 객체
    private AdminNoticeDAO noticeDAO = new AdminNoticeDAO();
    private AdminReportDAO reportDAO = new AdminReportDAO();
    private AdminStatsDAO statsDAO = new AdminStatsDAO();
    private AdminUserDAO adminUserDAO = new AdminUserDAO();
    private UserDAO userDAO = new UserDAO();

    // ===================== 공지 관련 =====================
    public List<AdminNoticeDTO> getNoticeList() {
        return noticeDAO.findAll();
    }

    public boolean writeNotice(AdminNoticeDTO dto) {
        return noticeDAO.insert(dto) > 0;
    }

    public boolean updateNotice(AdminNoticeDTO dto) {
        return noticeDAO.update(dto) > 0;
    }

    public boolean deleteNotice(long id) {
        return noticeDAO.delete(id) > 0;
    }

    // ===================== 신고 관련 =====================
    public List<ReportDTO> getReportList() {
        return reportDAO.findAll();
    }

    public ReportDTO getReportById(long id) {
        return reportDAO.findAll()
                .stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ===================== 통계 관련 =====================
    public AdminStatsDTO getStats() {
        return statsDAO.getStats();
    }

    // ===================== 회원 관련 =====================
    // 전체 회원 목록
    public List<UserDTO> getUserList() {
        return userDAO.findAllUsers();
    }

    // 회원 상세 조회
    public UserDTO getUserById(long id) {
        return userDAO.findAllUsers()
                .stream()
                .filter(u -> u.getAutoId() == id)
                .findFirst()
                .orElse(null);
    }

    // 회원 정지
    public boolean blockUser(long id) {
        return adminUserDAO.updateRole(id, "BLOCKED") > 0;
    }

    // 회원 정지 해제
    public boolean unblockUser(long id) {
        return adminUserDAO.updateRole(id, "USER") > 0;
    }

    // 회원 삭제
    public int deleteUser(long autoId) {
        return adminUserDAO.deleteUser(autoId);
    }
}

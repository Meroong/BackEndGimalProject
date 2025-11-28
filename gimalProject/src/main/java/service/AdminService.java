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

    // ===================== DAO 객체들 =====================
    private final AdminNoticeDAO noticeDAO = new AdminNoticeDAO();
    private final AdminReportDAO reportDAO = new AdminReportDAO();
    private final AdminStatsDAO statsDAO = new AdminStatsDAO();
    private final AdminUserDAO adminUserDAO = new AdminUserDAO();
    private final UserDAO userDAO = new UserDAO();

    // ===================== 공지 관련 =====================

    // 공지 전체 목록
    public List<AdminNoticeDTO> getNoticeList() {
        return noticeDAO.findAll();
    }

    // 공지 등록
    public boolean writeNotice(AdminNoticeDTO dto) {
        return noticeDAO.insert(dto) > 0;
    }

    // 공지 수정
    public boolean updateNotice(AdminNoticeDTO dto) {
        return noticeDAO.update(dto) > 0;
    }

    // 공지 삭제
    public boolean deleteNotice(long id) {
        return noticeDAO.delete(id) > 0;
    }

    // ===================== 신고 관련 =====================

    // 신고 전체 목록
    public List<ReportDTO> getReportList() {
        return reportDAO.findAll();
    }

    // 신고 단건 조회
    public ReportDTO getReportById(long id) {
        return reportDAO.findById(id);
    }

    // 신고 처리 (PENDING -> RESOLVED)
    public boolean resolveReport(long id) {
        return reportDAO.updateStatus(id, "RESOLVED") > 0;
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

    // 회원 단건 조회
    public UserDTO getUserById(long id) {
        // UserDAO에 단건 조회 메서드가 없어서
        // 기존 설계처럼 전체 조회 후 필터링
        return userDAO.findAllUsers()
                .stream()
                .filter(u -> u.getAutoId() == id)
                .findFirst()
                .orElse(null);
    }

    // 회원 정지 (role -> BLOCKED)
    public boolean blockUser(long id) {
        return adminUserDAO.updateRole(id, "BLOCKED") > 0;
    }

    // 회원 정지 해제 (role -> USER)
    public boolean unblockUser(long id) {
        return adminUserDAO.updateRole(id, "USER") > 0;
    }

    // 회원 삭제(탈퇴)
    public int deleteUser(long autoId) {
        return adminUserDAO.deleteUser(autoId);
    }
}

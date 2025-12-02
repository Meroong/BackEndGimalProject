package service;

import java.sql.Connection;
import java.util.List;

import dao.AdminNoticeDAO;
import dao.AdminReportDAO;
import dao.AdminStatsDAO;
import dao.AdminUserDAO;
import dto.AdminNoticeDTO;
import dto.AdminStatsDTO;
import dto.ReportDTO;
import dto.UserDTO;
import util.DBUtil;

public class AdminService {

    public AdminService() {}

    // ================== 회원 ==================
    public List<UserDTO> getUserList() {
        try (Connection conn = DBUtil.getConnection()) {
            AdminUserDAO dao = new AdminUserDAO(conn);
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserDTO getUserById(long id) {
        try (Connection conn = DBUtil.getConnection()) {
            AdminUserDAO dao = new AdminUserDAO(conn);
            return dao.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int blockUser(long id) {
        try (Connection conn = DBUtil.getConnection()) {
            AdminUserDAO dao = new AdminUserDAO(conn);
            return dao.updateBlockStatus(id, true);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int unblockUser(long id) {
        try (Connection conn = DBUtil.getConnection()) {
            AdminUserDAO dao = new AdminUserDAO(conn);
            return dao.updateBlockStatus(id, false);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 🔥 회원 삭제(탈퇴)
    public int deleteUser(long id) {
        System.out.println("[AdminService] deleteUser id = " + id);
        try (Connection conn = DBUtil.getConnection()) {
            AdminUserDAO dao = new AdminUserDAO(conn);
            int result = dao.deleteUser(id);
            System.out.println("[AdminService] deleteUser result = " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ================== 신고 ==================
    public List<ReportDTO> getReportList() {
        try (Connection conn = DBUtil.getConnection()) {
            AdminReportDAO dao = new AdminReportDAO(conn);
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ReportDTO getReportById(long id) {
        try (Connection conn = DBUtil.getConnection()) {
            AdminReportDAO dao = new AdminReportDAO(conn);
            return dao.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 신고 상태 토글 (PENDING <-> RESOLVED)
    public boolean toggleReportStatus(long id) {
        try (Connection conn = DBUtil.getConnection()) {
            AdminReportDAO dao = new AdminReportDAO(conn);

            ReportDTO dto = dao.findById(id);
            if (dto == null) return false;

            String newStatus = "PENDING";
            if ("PENDING".equals(dto.getStatus())) {
                newStatus = "RESOLVED";
            }

            int updated = dao.updateStatus(id, newStatus);
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================== 공지 ==================
    public List<AdminNoticeDTO> getNoticeList() {
        try (Connection conn = DBUtil.getConnection()) {
            AdminNoticeDAO dao = new AdminNoticeDAO(conn);
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeNotice(AdminNoticeDTO dto) {
        try (Connection conn = DBUtil.getConnection()) {
            AdminNoticeDAO dao = new AdminNoticeDAO(conn);
            dao.insert(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNotice(AdminNoticeDTO dto) {
        try (Connection conn = DBUtil.getConnection()) {
            AdminNoticeDAO dao = new AdminNoticeDAO(conn);
            dao.update(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNotice(long id) {
        try (Connection conn = DBUtil.getConnection()) {
            AdminNoticeDAO dao = new AdminNoticeDAO(conn);
            dao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================== 통계 ==================
    public AdminStatsDTO getStats() {
        try (Connection conn = DBUtil.getConnection()) {
            AdminStatsDAO dao = new AdminStatsDAO(conn);
            return dao.getStats();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

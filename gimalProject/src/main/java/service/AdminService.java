package service;

import java.util.List;

import dao.AdminNoticeDAO;
import dao.AdminReportDAO;
import dao.AdminStatsDAO;
import dao.AdminUserDAO;
import dao.UserDAO;
import dto.AdminNoticeDTO;
import dto.AdminStatsDTO;
import dto.ReportDTO;
import dto.UserDTO;

/**
 * 관리자 전용 서비스 레이어
 * - DB 커넥션은 각 DAO(JDBCUtil)를 통해서만 열고 닫도록 단순 위임하는 구조로 변경
 * - 기존에 사용하던 DBUtil, Connection 파라미터 의존성 제거
 */
public class AdminService {

    public AdminService() {}

    // ================== 회원 ==================
    // 전체 회원 목록
    public List<UserDTO> getUserList() {
        try {
            UserDAO dao = new UserDAO();
            return dao.findAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // autoId 기준 회원 한 명 조회
    public UserDTO getUserById(long id) {
        try {
            UserDAO dao = new UserDAO();
            return dao.searchByAutoId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 회원 차단(ROLE -> BLOCKED)
    public int blockUser(long id) {
        try {
            AdminUserDAO dao = new AdminUserDAO();
            return dao.updateRole(id, "BLOCKED");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 회원 차단 해제(ROLE -> USER)
    public int unblockUser(long id) {
        try {
            AdminUserDAO dao = new AdminUserDAO();
            return dao.updateRole(id, "USER");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

 // 🔥 회원 삭제(탈퇴)
    public int deleteUser(long id) {
        System.out.println("[AdminService] deleteUser id = " + id);
        try {
            // 복잡한 AdminUserDAO 말고, 검증된 UserDAO 삭제 메서드를 그대로 사용
            UserDAO userDao = new UserDAO();
            int result = userDao.delete(id);
            System.out.println("[AdminService] deleteUser result = " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    // ================== 신고 ==================
    public List<ReportDTO> getReportList() {
        try {
            AdminReportDAO dao = new AdminReportDAO();
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ReportDTO getReportById(long id) {
        try {
            AdminReportDAO dao = new AdminReportDAO();
            return dao.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 신고 상태 토글 (PENDING <-> RESOLVED)
    public boolean toggleReportStatus(long id) {
        try {
            AdminReportDAO dao = new AdminReportDAO();

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
        try {
            AdminNoticeDAO dao = new AdminNoticeDAO();
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeNotice(AdminNoticeDTO dto) {
        try {
            AdminNoticeDAO dao = new AdminNoticeDAO();
            dao.insert(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNotice(AdminNoticeDTO dto) {
        try {
            AdminNoticeDAO dao = new AdminNoticeDAO();
            dao.update(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNotice(long id) {
        try {
            AdminNoticeDAO dao = new AdminNoticeDAO();
            dao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================== 통계 ==================
    public AdminStatsDTO getStats() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getStats();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
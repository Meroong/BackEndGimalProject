package service;

import java.util.List;

import dao.AdminNoticeDAO;
import dao.AdminReportDAO;
import dao.AdminStatsDAO;
import dao.AdminUserDAO;
import dao.UserDAO;
import dao.AdminMeetingDAO;
import dto.AdminNoticeDTO;
import dto.AdminStatsDTO;
import dto.DailySignupDTO;
import dto.ReportDTO;
import dto.ReportStatusCountDTO;
import dto.UserDTO;
import dto.AdminMeetingDTO;

/**
 * 관리자 전용 서비스 레이어
 * - DB 커넥션은 각 DAO(JDBCUtil)를 통해서만 열고 닫도록 단순 위임하는 구조
<<<<<<< HEAD
 * - 기존에 사용하던 DBUtil, Connection 파라미터 의존성 제거
=======
>>>>>>> admin-almost-end
 */
public class AdminService {

    public AdminService() {}

    // ================== 회원 ==================
    // 전체 회원 목록
    // =====================================================
    // 🔹 회원 관리
    // =====================================================
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

 // 회원 차단(ROLE -> BLOCK)
    public int blockUser(long id) {
        try {
            AdminUserDAO dao = new AdminUserDAO();
            return dao.updateRole(id, "BLOCK");   // ✅ 여기만 바꾸면 됨
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
        try {
            UserDAO userDao = new UserDAO();
            return userDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    // =====================================================
    // 🔹 신고 관리
    // =====================================================
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

    // =====================================================
    // 🔹 공지 관리
    // =====================================================
    public List<AdminNoticeDTO> getNoticeList() {
        try {
            AdminNoticeDAO dao = new AdminNoticeDAO();
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // 공지 단건 조회
    public AdminNoticeDTO getNoticeById(long id) {
        try {
            AdminNoticeDAO dao = new AdminNoticeDAO();
            return dao.findById(id);
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

    // =====================================================
    // 🔹 서비스 통계 (신뢰도 제거 버전)
    // =====================================================

    // 기본 통계 (총 회원 수, 총 신고 수)
    public AdminStatsDTO getStats() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getStats();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public int getTotalUsers() {
        try { return new AdminStatsDAO().getTotalUsers(); }
        catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int getTodayNewUsers() {
        try { return new AdminStatsDAO().getTodayNewUsers(); }
        catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int getTotalItems() {
        try { return new AdminStatsDAO().getTotalItems(); }
        catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int getTotalTransactions() {
        try { return new AdminStatsDAO().getTotalTransactions(); }
        catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int getPendingReports() {
        try { return new AdminStatsDAO().getPendingReports(); }
        catch (Exception e) { e.printStackTrace(); return 0; }
    }

    // 최근 N일 회원가입 통계
    public List<DailySignupDTO> getDailySignupStats(int days) {
        try { return new AdminStatsDAO().getDailySignupStats(days); }
        catch (Exception e) { e.printStackTrace(); return null; }
    }

    // 신고 상태별 개수
    public List<ReportStatusCountDTO> getReportStatusCounts() {
        try { return new AdminStatsDAO().getReportStatusCounts(); }
        catch (Exception e) { e.printStackTrace(); return null; }
    }


    // =====================================================
    // 🔹 모임 관리
    // =====================================================
    public List<AdminMeetingDTO> getMeetingList(String keyword, String status, String reportFilter) {
        try {
            AdminMeetingDAO dao = new AdminMeetingDAO();
            return dao.search(keyword, status, reportFilter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<AdminMeetingDTO> getMeetingList() {
        try {
            AdminMeetingDAO dao = new AdminMeetingDAO();
            return dao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AdminMeetingDTO getMeetingById(long id) {
        try {
            AdminMeetingDAO dao = new AdminMeetingDAO();
            return dao.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int updateMeetingStatus(long id, String status) {
        try {
            AdminMeetingDAO dao = new AdminMeetingDAO();
            return dao.updateStatus(id, status);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteMeeting(long id) {
        try {
            AdminMeetingDAO dao = new AdminMeetingDAO();
            return dao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int deleteReport(long reportId) {
        try {
            AdminReportDAO dao = new AdminReportDAO();
            return dao.deleteById(reportId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}

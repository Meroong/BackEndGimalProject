package service;

import java.util.List;

import dao.AdminNoticeDAO;
import dao.AdminReportDAO;
import dao.AdminStatsDAO;
import dao.AdminUserDAO;
import dao.UserDAO;
import dto.AdminNoticeDTO;
import dto.AdminStatsDTO;
import dto.DailySignupDTO;
import dto.ReportDTO;
import dto.ReportStatusCountDTO;
import dto.UserDTO;
import dto.UserTrustRankDTO;
import dao.AdminMeetingDAO;
import dto.AdminMeetingDTO;
import dao.AdminMeetingDAO;
import dto.AdminMeetingDTO;



/**
 * 관리자 전용 서비스 레이어
 * - DB 커넥션은 각 DAO(JDBCUtil)를 통해서만 열고 닫도록 단순 위임하는 구조
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



    // ================== 통계 (기본 + 확장) ==================

    // 기존: 전체 회원 수 / 전체 신고 수 등 기본 통계
    public AdminStatsDTO getStats() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getStats();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // 상단 카드: 전체 회원 수
    public int getTotalUsers() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getTotalUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 상단 카드: 오늘 가입한 회원 수
    public int getTodayNewUsers() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getTodayNewUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 상단 카드: 전체 상품 수
    public int getTotalItems() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getTotalItems();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 상단 카드: 전체 거래 수
    public int getTotalTransactions() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getTotalTransactions();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 상단 카드: 미처리 신고 수
    public int getPendingReports() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getPendingReports();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 그래프: 최근 N일 회원가입 통계
    public List<DailySignupDTO> getDailySignupStats(int days) {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getDailySignupStats(days);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 표: 신뢰도 높은 유저 TOP N
    public List<UserTrustRankDTO> getTopUsersByTrustScore(int limit) {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getTopUsersByTrustScore(limit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 그래프: 신고 상태별 개수
    public List<ReportStatusCountDTO> getReportStatusCounts() {
        try {
            AdminStatsDAO dao = new AdminStatsDAO();
            return dao.getReportStatusCounts();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ================== 모임 관리 ==================
    // 목록 (검색 + 상태 필터)
    public List<AdminMeetingDTO> getMeetingList(String keyword, String status) {
        try {
            AdminMeetingDAO dao = new AdminMeetingDAO();
            return dao.search(keyword, status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 기존 no-arg 버전 (호환용, 필요하면 남겨둠)
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

}

package controller;

import java.io.IOException;
import java.util.List;

import dto.AdminNoticeDTO;
import dto.AdminStatsDTO;
import dto.DailySignupDTO;
import dto.ReportDTO;
import dto.ReportStatusCountDTO;
import dto.UserDTO;
import dto.AdminMeetingDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminService;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ★ 여기 딱 한 번만 선언
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();   // 예: /users/detail, /notices/write, /reports/detail 등

        if (path == null) {
            path = "";
        } else {
            path = path.trim();
        }

        if (path.endsWith("/") && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }

        System.out.println("[AdminController] pathInfo = " + path);

        if ("".equals(path) || "/".equals(path)) {
            showAdminMain(req, resp);
            return;
        }

        switch (path) {

            // ================== 메인 ==================
            case "/main":
                showAdminMain(req, resp);
                break;

            // ================== 회원 관리 ==================
            case "/user/list":
            case "/users":
                showUserList(req, resp);
                break;

            case "/user/detail":
            case "/users/detail":
                showUserDetail(req, resp);
                break;

            case "/user/block":
            case "/users/block":
                blockUser(req, resp);
                break;

            case "/user/unblock":
            case "/users/unblock":
                unblockUser(req, resp);
                break;

            case "/user/delete":
            case "/users/delete":
                deleteUser(req, resp);
                break;

            // ================== 관리자 통계 ==================
            case "/stats":
            case "/dashboard":
                showAdminStats(req, resp);
                break;

            // ================== 신고 관리 ==================
            case "/report/list":
            case "/reports":
                showReportList(req, resp);
                break;

            case "/report/detail":
            case "/reports/detail":
                showReportDetail(req, resp);
                break;

            case "/report/resolve":
            case "/reports/resolve":
                toggleReportStatus(req, resp);
                break;
                
            case "/report/deleteUser":
            case "/reports/deleteUser":
                deleteUserFromReport(req, resp);
                break;

            // ================== 공지 관리 ==================
            case "/notice/list":
            case "/notices":
                showNoticeList(req, resp);
                break;

            // 공지 작성 (GET: 폼, POST: 저장)
            case "/notice/form":
            case "/notices/write":
                if ("GET".equalsIgnoreCase(req.getMethod())) {
                    showNoticeForm(req, resp);   // 새 글 작성 폼
                } else {
                    saveNotice(req, resp);       // 새 글 저장
                }
                break;

            // 공지 수정 (GET: 기존 글 폼, POST: 수정 저장)
            case "/notice/edit":
            case "/notices/edit":
                if ("GET".equalsIgnoreCase(req.getMethod())) {
                    showNoticeForm(req, resp);   // 수정 폼
                } else {
                    saveNotice(req, resp);       // 수정 저장
                }
                break;

            // 공지 저장 (예전 경로 호환용)
            case "/notice/save":
            case "/notices/save":
                saveNotice(req, resp);
                break;

            // 공지 삭제
            case "/notice/delete":
            case "/notices/delete":
                deleteNotice(req, resp);
                break;

            // ================== 모임 관리 ==================
            case "/meeting/list":
            case "/meetings":
                showMeetingList(req, resp);
                break;

            case "/meeting/detail":
            case "/meetings/detail":
                showMeetingDetail(req, resp);
                break;

            case "/meeting/status":
            case "/meetings/status":
                updateMeetingStatus(req, resp);
                break;

            case "/meeting/delete":
            case "/meetings/delete":
                deleteMeeting(req, resp);
                break;

            default:
                System.out.println("[AdminController] 404, unknown path = " + path);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ================== 메인 ==================
    private void showAdminMain(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/adminMain.jsp");
        rd.forward(req, resp);
    }

    // ================== 회원 관리 ==================
    private void showUserList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<UserDTO> userList = adminService.getUserList();
        req.setAttribute("userList", userList);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/userList.jsp");
        rd.forward(req, resp);
    }

    private void showUserDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = Long.parseLong(req.getParameter("id"));
        UserDTO user = adminService.getUserById(id);

        req.setAttribute("user", user);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/userDetail.jsp");
        rd.forward(req, resp);
    }

    private void blockUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        long id = Long.parseLong(req.getParameter("id"));
        adminService.blockUser(id);

        resp.sendRedirect(req.getContextPath() + "/admin/users/detail?id=" + id);
    }

    private void unblockUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        long id = Long.parseLong(req.getParameter("id"));
        adminService.unblockUser(id);

        resp.sendRedirect(req.getContextPath() + "/admin/users/detail?id=" + id);
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        long id = Long.parseLong(req.getParameter("id"));
        System.out.println("[AdminController] deleteUser id = " + id);

        // 물리 삭제 ❌ → 차단 처리 ✅
        int result = adminService.blockUser(id);
        System.out.println("[AdminController] blockUser result = " + result);

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }



    // ================== 신고 관리 ==================
    private void showReportList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<ReportDTO> reportList = adminService.getReportList();
        req.setAttribute("reportList", reportList);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/reportList.jsp");
        rd.forward(req, resp);
    }

    private void showReportDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = Long.parseLong(req.getParameter("id"));
        ReportDTO report = adminService.getReportById(id);

        req.setAttribute("report", report);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/reportDetail.jsp");
        rd.forward(req, resp);
    }

    private void toggleReportStatus(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        long id = Long.parseLong(req.getParameter("id"));

        adminService.toggleReportStatus(id);

        resp.sendRedirect(req.getContextPath() + "/admin/report/detail?id=" + id);
    }

    // ================== 공지 관리 ==================
    private void showNoticeList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<AdminNoticeDTO> noticeList = adminService.getNoticeList();
        req.setAttribute("noticeList", noticeList);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/noticeList.jsp");
        rd.forward(req, resp);
    }

    private void showNoticeForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");

        // id가 있으면 = 수정 모드 → 기존 공지 조회
        if (idParam != null && !idParam.isEmpty()) {
            long id = Long.parseLong(idParam);
            AdminNoticeDTO notice = adminService.getNoticeById(id);
            req.setAttribute("notice", notice);
        }

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp");
        rd.forward(req, resp);
    }

    private void saveNotice(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        AdminNoticeDTO dto = new AdminNoticeDTO();
        dto.setTitle(title);
        dto.setContent(content);

        if (idParam == null || idParam.isEmpty()) {
            adminService.writeNotice(dto);      // 새 글
        } else {
            long id = Long.parseLong(idParam);
            dto.setId(id);
            adminService.updateNotice(dto);     // 수정
        }

        resp.sendRedirect(req.getContextPath() + "/admin/notice/list");
    }

    private void deleteNotice(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        long id = Long.parseLong(req.getParameter("id"));
        adminService.deleteNotice(id);

        resp.sendRedirect(req.getContextPath() + "/admin/notice/list");
    }

    // ================== 통계 (대시보드) ==================
    private void showAdminStats(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) 기본 통계 (전체 회원 수, 전체 신고 수 등)
        AdminStatsDTO basicStats = adminService.getStats();

        // 2) 상단 카드용 요약 수치
        int totalUsers        = adminService.getTotalUsers();          // 전체 회원 수
        int todayNewUsers     = adminService.getTodayNewUsers();       // 오늘 가입 회원 수
        int totalItems        = adminService.getTotalItems();          // 전체 상품 수
        int totalTransactions = adminService.getTotalTransactions();   // 전체 거래 수
        int pendingReports    = adminService.getPendingReports();      // 미처리(PENDING) 신고 수

        // 3) 그래프 / 표용 상세 데이터 (신뢰도 제외)
        List<DailySignupDTO>       signupStats = adminService.getDailySignupStats(7);  // 최근 7일
        List<ReportStatusCountDTO> reportStats = adminService.getReportStatusCounts(); // 상태별 신고 건수

        // 4) JSP로 전달
        req.setAttribute("basicStats", basicStats);
        req.setAttribute("stats", basicStats);   // 예전 JSP 호환용 이름

        // 상단 카드
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("todayNewUsers", todayNewUsers);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("totalTransactions", totalTransactions);
        req.setAttribute("pendingReports", pendingReports);

        // 그래프/표 데이터
        req.setAttribute("signupStats", signupStats);
        req.setAttribute("reportStats", reportStats);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/stats.jsp");
        rd.forward(req, resp);
    }

    // ================== 모임 관리 ==================
    private void showMeetingList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        String status  = req.getParameter("status");
        String reportFilter = req.getParameter("reportFilter"); // ✅ 추가

        List<AdminMeetingDTO> meetingList = adminService.getMeetingList(keyword, status, reportFilter);

        req.setAttribute("meetingList", meetingList);
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);
        req.setAttribute("reportFilter", reportFilter); // ✅ 추가

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/meetingList.jsp");
        rd.forward(req, resp);
    }


    private void showMeetingDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = Long.parseLong(req.getParameter("id"));

        // 1) 모임 기본 정보
        AdminMeetingDTO meeting = adminService.getMeetingById(id);
        req.setAttribute("meeting", meeting);

        // 2) ✅ 경고 플래그 계산 (DAO 직접 호출)
        dao.AdminMeetingDAO meetingDAO = new dao.AdminMeetingDAO();

        // (A) 신고 유저 포함 경고: DB 수정 없이 바로 가능
        boolean warnReportedUserIncluded = meetingDAO.hasReportedUserInMeeting(id);

        // (B) 신고된 모임 경고: report.target_id 컬럼이 있을 때만 의미 있음
        boolean warnMeetingReported = meetingDAO.hasMeetingReport(id);

        // (옵션) 건수까지 보여주고 싶으면
        int meetingReportCount = meetingDAO.countPendingMeetingReports(id);
        int reportedUserCount  = meetingDAO.countPendingReportedUsersInMeeting(id);

        req.setAttribute("warnReportedUserIncluded", warnReportedUserIncluded);
        req.setAttribute("warnMeetingReported", warnMeetingReported);

        req.setAttribute("meetingReportCount", meetingReportCount);
        req.setAttribute("reportedUserCount", reportedUserCount);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/meetingDetail.jsp");
        rd.forward(req, resp);
    }


    private void updateMeetingStatus(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        long id = Long.parseLong(req.getParameter("id"));
        String status = req.getParameter("status");  // OPEN / CLOSED / COMPLETED

        adminService.updateMeetingStatus(id, status);

        resp.sendRedirect(req.getContextPath() + "/admin/meeting/detail?id=" + id);
    }

 // 모임 삭제
    private void deleteMeeting(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // ★ id 파라미터 먼저 안전하게 꺼내기
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            System.out.println("[AdminController] deleteMeeting 호출됨 - id 파라미터가 없음");
            // 그냥 목록으로 돌려보내고 끝내기 (500 에러 방지)
            resp.sendRedirect(req.getContextPath() + "/admin/meeting/list");
            return;
        }

        long id = Long.parseLong(idParam);
        System.out.println("[AdminController] deleteMeeting id = " + id);

        int result = adminService.deleteMeeting(id);
        System.out.println("[AdminController] deleteMeeting result = " + result);

        resp.sendRedirect(req.getContextPath() + "/admin/meeting/list");
    }
 // 신고 상세에서 "신고 대상 회원 탈퇴" 눌렀을 때
    private void deleteUserFromReport(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        long userId   = Long.parseLong(req.getParameter("userId")); // 대상 유저 auto_id
        long reportId = Long.parseLong(req.getParameter("id"));     // 신고 id

        // 1) 유저 차단(탈퇴 처리)
        int blockResult = adminService.blockUser(userId);
        System.out.println("[AdminController] deleteUserFromReport blockResult = " + blockResult);

        // 2) 신고 데이터 DB에서 삭제
        int delResult = adminService.deleteReport(reportId);
        System.out.println("[AdminController] deleteUserFromReport deleteReport result = " + delResult);

        // 3) 신고 목록으로
        resp.sendRedirect(req.getContextPath() + "/admin/reports");
    }

}
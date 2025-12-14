package controller;

import java.io.IOException;
import java.util.List;

import dto.AdminNoticeDTO;
import dto.AdminStatsDTO;
import dto.DailySignupDTO;
import dto.ReportDTO;
import dto.ReportStatusCountDTO;
import dto.UserDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminService;
import dto.AdminMeetingDTO;


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

            // ================== 공지 관리 ==================
            case "/notice/list":
            case "/notices":
                showNoticeList(req, resp);
                break;
            // 공지 작성 (GET: 폼, POST: 저장)
            case "/notice/form":
            case "/notices/write":
                if ("GET".equalsIgnoreCase(req.getMethod())) {
                    // 새 글 작성 폼
                    showNoticeForm(req, resp);
                } else {
                    // 새 글 저장
                    saveNotice(req, resp);
                }
                break;
            // 공지 수정 (GET: 기존 글 폼, POST: 수정 저장)
            case "/notice/edit":
            case "/notices/edit":
                if ("GET".equalsIgnoreCase(req.getMethod())) {
                    // 수정 폼 - id 파라미터로 글 조회
                    showNoticeForm(req, resp);
                } else {
                    // 수정 저장
                    saveNotice(req, resp);
                }
                break;

            // 공지 저장 (예전 경로 호환용)
            case "/notice/save":
            case "/notices/save":
                saveNotice(req, resp);
                break;
            case "/notice/delete":
                deleteNotice(req, resp);
                break;
            // 공지 삭제
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

        int result = adminService.deleteUser(id);
        System.out.println("[AdminController] deleteUser result = " + result);

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

        // 기존 AdminStatsDTO (전체 회원 수, 전체 신고 수 등)
        AdminStatsDTO basicStats = adminService.getStats();

        // 상단 카드용
        int totalUsers        = adminService.getTotalUsers();
        int todayNewUsers     = adminService.getTodayNewUsers();
        int totalItems        = adminService.getTotalItems();
        int totalTransactions = adminService.getTotalTransactions();
        int pendingReports    = adminService.getPendingReports();

        // 그래프/표용
        List<DailySignupDTO>       signupStats = adminService.getDailySignupStats(7);     // 최근 7일
        List<ReportStatusCountDTO> reportStats = adminService.getReportStatusCounts();

        // JSP에 전달
        req.setAttribute("basicStats", basicStats);

        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("todayNewUsers", todayNewUsers);
        req.setAttribute("totalItems", totalItems);
        req.setAttribute("totalTransactions", totalTransactions);
        req.setAttribute("pendingReports", pendingReports);

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

        List<AdminMeetingDTO> meetingList = adminService.getMeetingList(keyword, status);

        req.setAttribute("meetingList", meetingList);
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);

        RequestDispatcher rd =
                req.getRequestDispatcher("/WEB-INF/views/admin/meetingList.jsp");
        rd.forward(req, resp);
    }

    private void showMeetingDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = Long.parseLong(req.getParameter("id"));
        AdminMeetingDTO meeting = adminService.getMeetingById(id);

        req.setAttribute("meeting", meeting);

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

        long id = Long.parseLong(req.getParameter("id"));
        System.out.println("[AdminController] deleteMeeting id = " + id);

        int result = adminService.deleteMeeting(id);
        System.out.println("[AdminController] deleteMeeting result = " + result);

        resp.sendRedirect(req.getContextPath() + "/admin/meeting/list");
    }


}


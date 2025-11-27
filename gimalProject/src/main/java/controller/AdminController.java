package controller;

import java.io.IOException;
import java.util.List;

import auth.JwtAuth;
import dto.AdminNoticeDTO;
import dto.AdminStatsDTO;
import dto.ReportDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.AdminService;
import util.AuthUtil;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {

    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 세션에서 JWT 헤더 꺼내기
        HttpSession session = request.getSession(false);
        String authHeader = (session != null)
                ? (String) session.getAttribute("Authorization")
                : null;

        // 로그인 체크
        if (authHeader == null) {
            response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
            return;
        }

        // ADMIN 권한 체크
        String role = AuthUtil.getRole(request);
        if (!"ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String path = request.getPathInfo();

        // ===================== 관리자 메인 =====================
        if (path == null || "/".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/admin/adminMain.jsp")
                   .forward(request, response);

        // ===================== 공지 삭제 =====================
        } else if (path.startsWith("/notices/delete")) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                long id = Long.parseLong(idParam);
                adminService.deleteNotice(id);
            }
            response.sendRedirect(request.getContextPath() + "/admin/notices");

        // ===================== 공지 수정 =====================
        } else if (path.startsWith("/notices/edit")) {
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                long id = Long.parseLong(request.getParameter("id"));
                AdminNoticeDTO dto = adminService.getNoticeList()
                        .stream()
                        .filter(n -> n.getId() == id)
                        .findFirst()
                        .orElse(null);
                request.setAttribute("notice", dto);
                request.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp")
                       .forward(request, response);

            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                request.setCharacterEncoding("UTF-8");
                long id = Long.parseLong(request.getParameter("id"));
                String title = request.getParameter("title");
                String content = request.getParameter("content");

                AdminNoticeDTO dto = new AdminNoticeDTO();
                dto.setId(id);
                dto.setTitle(title);
                dto.setContent(content);

                boolean success = adminService.updateNotice(dto);
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/admin/notices");
                } else {
                    request.setAttribute("error", "수정 실패");
                    request.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp")
                           .forward(request, response);
                }
            }

        // ===================== 공지 작성 =====================
        } else if (path.startsWith("/notices/write")) {
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                request.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp")
                       .forward(request, response);

            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                request.setCharacterEncoding("UTF-8");
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String writer = "ADMIN";

                AdminNoticeDTO dto = new AdminNoticeDTO();
                dto.setTitle(title);
                dto.setContent(content);
                dto.setWriter(writer);

                boolean success = adminService.writeNotice(dto);
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/admin/notices");
                } else {
                    request.setAttribute("error", "공지 등록에 실패했습니다.");
                    request.getRequestDispatcher("/WEB-INF/views/admin/noticeForm.jsp")
                           .forward(request, response);
                }
            }

        // ===================== 공지 리스트 =====================
        } else if (path.startsWith("/notices")) {
            List<AdminNoticeDTO> noticeList = adminService.getNoticeList();
            request.setAttribute("noticeList", noticeList);
            request.getRequestDispatcher("/WEB-INF/views/admin/noticeList.jsp")
                   .forward(request, response);

        // ===================== 신고 상세 =====================
        } else if (path.startsWith("/reports/detail")) {
            long id = Long.parseLong(request.getParameter("id"));
            ReportDTO report = adminService.getReportById(id);
            request.setAttribute("report", report);
            request.getRequestDispatcher("/WEB-INF/views/admin/reportDetail.jsp")
                   .forward(request, response);

        // ===================== 신고 처리 완료 =====================
        } else if (path.startsWith("/reports/resolve")) {
            long id = Long.parseLong(request.getParameter("id"));
            boolean success = adminService.resolveReport(id);
            // TODO: 필요하면 success 여부에 따라 메시지 처리 추가 가능
            response.sendRedirect(request.getContextPath() + "/admin/reports");

        // ===================== 신고 리스트 =====================
        } else if (path.startsWith("/reports")) {
            List<ReportDTO> reportList = adminService.getReportList();
            request.setAttribute("reportList", reportList);
            request.getRequestDispatcher("/WEB-INF/views/admin/reportList.jsp")
                   .forward(request, response);

        // ===================== 통계 =====================
        } else if (path.startsWith("/stats")) {
            AdminStatsDTO stats = adminService.getStats();
            request.setAttribute("stats", stats);
            request.getRequestDispatcher("/WEB-INF/views/admin/stats.jsp")
                   .forward(request, response);

        // ===================== 회원 상세 =====================
        } else if (path.startsWith("/users/detail")) {
            long id = Long.parseLong(request.getParameter("id"));
            UserDTO user = adminService.getUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/admin/userDetail.jsp")
                   .forward(request, response);

        // ===================== 회원 블록 =====================
        } else if (path.startsWith("/users/block")) {
            long id = Long.parseLong(request.getParameter("id"));
            adminService.blockUser(id);
            response.sendRedirect(request.getContextPath() + "/admin/users/detail?id=" + id);

        // ===================== 회원 블록 해제 =====================
        } else if (path.startsWith("/users/unblock")) {
            long id = Long.parseLong(request.getParameter("id"));
            adminService.unblockUser(id);
            response.sendRedirect(request.getContextPath() + "/admin/users/detail?id=" + id);

        // ===================== 회원 삭제 =====================
        } else if (path.startsWith("/users/delete")) {
            long id = Long.parseLong(request.getParameter("id"));
            int result = adminService.deleteUser(id);
            System.out.println(result > 0 ? "삭제 완료" : "삭제 실패");
            response.sendRedirect(request.getContextPath() + "/admin/users");

        // ===================== 회원 리스트 =====================
        } else if (path.startsWith("/users")) {
            List<UserDTO> userList = adminService.getUserList();
            request.setAttribute("userList", userList);
            request.getRequestDispatcher("/WEB-INF/views/admin/userList.jsp")
                   .forward(request, response);

        // ===================== 기타 404 =====================
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

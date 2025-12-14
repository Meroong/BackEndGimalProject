package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import dto.ReportDTO;
import service.ReportService;
import util.AuthUtil;

@WebServlet("/report/*")
public class ReportController extends HttpServlet {

    private ReportService reportService = new ReportService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        // 로그인 체크 
        Long loginId = AuthUtil.getAutoId(req);

        if (loginId == -1) {
            // 로그인 페이지 이동
        	resp.sendRedirect(req.getContextPath() + "/page/login");
            return;
        }

        switch (path) {

            //  신고 생성
            case "/create":

                String targetType = req.getParameter("targetType");   // USER / ITEM / MEETING
                String reason = req.getParameter("reason");
                long targetUserId = Long.parseLong(req.getParameter("targetUserId"));

                // DTO 채우기
                ReportDTO dto = new ReportDTO();
                dto.setReporterId(loginId);
                dto.setTargetUserId(targetUserId);
                dto.setTargetType(targetType);
                dto.setReason(reason);

                // 이전 페이지 (모임 상세 등)
                String referer = req.getHeader("Referer");
                if (referer == null || !referer.startsWith(req.getScheme() + "://" + req.getServerName())) {
                    referer = req.getContextPath() + "/home";
                }

                try {
                    boolean result = reportService.createReport(dto);

                    if (!result) {
                        throw new Exception("신고 등록이 실패했습니다.");
                    }

                    resp.setContentType("text/html; charset=UTF-8");
                    resp.getWriter().println(
                        "<script>alert('신고가 정상적으로 접수되었습니다.');" +
                        "location.href='" + referer + "';</script>"
                    );
                } catch (Exception e) {
                    e.printStackTrace();

                    resp.setContentType("text/html; charset=UTF-8");
                    resp.getWriter().println(
                        "<script>alert('오류 발생: " + e.getMessage().replace("'", "") + "');" +
                        "history.back();</script>"
                    );
                }

                return;

            // =========================
            // 🚫 잘못된 요청
            // =========================
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "잘못된 신고 요청입니다.");
                return;
        }
    }
}

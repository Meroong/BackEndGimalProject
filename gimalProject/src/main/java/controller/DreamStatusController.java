package controller;

import dao.DreamPostDAO;
import dto.DreamPostDTO;
import util.AuthUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dream/status.do")
public class DreamStatusController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DreamPostDAO dreamPostDAO = new DreamPostDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Long autoId = AuthUtil.getAutoId(request);
        if (autoId == -1) {
            response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
            return;
        }

        String paramId  = request.getParameter("dreamId");
        String newStatus = request.getParameter("status");

        // 파라미터 체크
        if (paramId == null || newStatus == null) {
            response.sendRedirect(request.getContextPath() + "/dream/list.do");
            return;
        }

        long dreamId;
        try {
            dreamId = Long.parseLong(paramId);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/dream/list.do");
            return;
        }

        // 글 존재 + 작성자 권한 확인
        DreamPostDTO post = dreamPostDAO.findByIdWithImages(dreamId);
        if (post == null || !autoId.equals(post.getWriterId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // status 값은 OPEN / CLOSE만 허용
        if (!"OPEN".equals(newStatus) && !"CLOSE".equals(newStatus)) {
            response.sendRedirect(request.getContextPath() + "/dream/detail.do?itemId=" + dreamId);
            return;
        }

        dreamPostDAO.updateStatus(dreamId, autoId, newStatus);

        String ctx = request.getContextPath();
        response.sendRedirect(ctx + "/dream/detail.do?itemId=" + dreamId);
    }
}

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

@WebServlet("/dream/delete.do")
public class DreamDeleteController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DreamPostDAO dreamPostDAO = new DreamPostDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Long autoId = AuthUtil.getAutoId(request);
        if (autoId == -1) {
        	response.sendRedirect(request.getContextPath() + "/page/login");
            return;
        }

        String paramId = request.getParameter("dreamId");
        if (paramId == null) {
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

        DreamPostDTO post = dreamPostDAO.findByIdWithImages(dreamId);
        if (post == null || !autoId.equals(post.getWriterId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        boolean deleted = dreamPostDAO.softDeletePost(dreamId, autoId);
        // 필요하다면 여기서 ImageService/FileResourceDAO로 파일도 같이 삭제 처리 가능

        String ctx = request.getContextPath();
        response.sendRedirect(ctx + "/dream/list.do");
    }
}

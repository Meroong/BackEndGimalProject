package controller;

import dao.DreamPostDAO;
import dto.DreamPostDTO;
import util.AuthUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dream/detail.do")
public class DreamDetailController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DreamPostDAO dreamPostDAO = new DreamPostDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String paramId = request.getParameter("itemId");
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

        // 조회수 증가 (선택)
        dreamPostDAO.increaseViewCount(dreamId);

        DreamPostDTO post = dreamPostDAO.findByIdWithImages(dreamId);
        if (post == null) {
            response.sendRedirect(request.getContextPath() + "/dream/list.do");
            return;
        }

        Long autoId = AuthUtil.getAutoId(request);  // 로그인 안 되어 있으면 -1 가정
        boolean isOwner = (autoId != null && autoId != -1 && autoId.equals(post.getWriterId()));

        request.setAttribute("post", post);
        request.setAttribute("isOwner", isOwner);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/dream/detail.jsp");
        rd.forward(request, response);
    }
}

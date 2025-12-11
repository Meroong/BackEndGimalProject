package controller;

import dao.DreamPostDAO;
import dto.DreamPostDTO;
import service.ImageService;
import util.AuthUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/dream/edit.do")
@MultipartConfig
public class DreamEditController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DreamPostDAO dreamPostDAO = new DreamPostDAO();
    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long autoId = AuthUtil.getAutoId(request);
        if (autoId == -1) {
            response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
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
            // 권한 없음 또는 존재하지 않는 글
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("post", post);
        RequestDispatcher rd = request.getRequestDispatcher("/dream/edit.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Long autoId = AuthUtil.getAutoId(request);
        if (autoId == -1) {
            response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
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

        DreamPostDTO original = dreamPostDAO.findByIdWithImages(dreamId);
        if (original == null || !autoId.equals(original.getWriterId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String title         = request.getParameter("title");
        String categoryCode  = request.getParameter("category_code");
        String conditionCode = request.getParameter("condition_code");
        String dong          = request.getParameter("dong");
        String content       = request.getParameter("content");

        DreamPostDTO dto = new DreamPostDTO();
        dto.setDreamId(dreamId);
        dto.setTitle(title);
        dto.setCategoryCode(categoryCode);
        dto.setConditionCode(conditionCode);
        dto.setDong(dong);
        dto.setContent(content);

        boolean updated = dreamPostDAO.updatePost(dto, autoId);
        if (!updated) {
            request.setAttribute("errorMessage", "게시글 수정에 실패했습니다. 잠시 후 다시 시도해주세요.");
            request.setAttribute("post", original);
            RequestDispatcher rd = request.getRequestDispatcher("/dream/edit.jsp");
            rd.forward(request, response);
            return;
        }

        // 1) 기존 이미지 삭제 (체크한 것만)
        String[] deleteUrls = request.getParameterValues("deleteImageUrl");
        String uploadRoot = "C:/upload/post"; // 업로드 루트 경로 (write/edit에서 쓰는 것과 동일하게)

        if (deleteUrls != null && deleteUrls.length > 0) {
            for (String fileUrl : deleteUrls) {
                imageService.deletePostImage(autoId, dreamId, fileUrl, uploadRoot);
            }
        }

        // 2) 새로운 이미지 추가 업로드
        Collection<Part> parts = request.getParts();
        String uploadPath = uploadRoot + "/" + dreamId;
        String usedType = "POST";

        for (Part part : parts) {
            if ("images".equals(part.getName()) && part.getSize() > 0) {
                imageService.uploadFileForPost(autoId, part, uploadPath, usedType, dreamId);
            }
        }

        String ctx = request.getContextPath();
        response.sendRedirect(ctx + "/dream/detail.do?itemId=" + dreamId);
    }
}

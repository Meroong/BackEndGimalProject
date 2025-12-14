package controller;

import dao.DreamPostDAO;
import dto.DreamPostDTO;
import util.AuthUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import service.ImageService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/dream/write.do")
@MultipartConfig
public class DreamWriteController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DreamPostDAO dreamPostDAO = new DreamPostDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	Long autoId = AuthUtil.getAutoId(request);
    	
    	if(autoId == -1) {
    		response.sendRedirect(request.getContextPath() + "/page/login");
        	return;
        }

    	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/dream/write.jsp");
    		rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        Long autoId = AuthUtil.getAutoId(request);
    	
    	if(autoId == -1) {
    		response.sendRedirect(request.getContextPath() + "/page/login");
        	return;
        }

        Long writerId = AuthUtil.getAutoId(request);   // 기존 유틸에서 userId 추출
        String writerType = AuthUtil.getRole(request); // WORKER / EMPLOYER 등

        String title = request.getParameter("title");
        String categoryCode = request.getParameter("category_code");
        String conditionCode = request.getParameter("condition_code");
        String dong = request.getParameter("dong");
        String content = request.getParameter("content");


        DreamPostDTO dto = new DreamPostDTO();
        dto.setWriterId(writerId);
        dto.setWriterType(writerType);
        dto.setTitle(title);
        dto.setCategoryCode(categoryCode);
        dto.setConditionCode(conditionCode);
        dto.setDong(dong);
        dto.setContent(content);
        dto.setStatus("OPEN");

        Long newId;
        
        try {
            newId = dreamPostDAO.insert(dto);
            if (newId == null) {
                request.setAttribute("errorMessage", "게시글 등록에 실패했습니다. 잠시 후 다시 시도해주세요.");
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/dream/write.jsp");
                	rd.forward(request, response);
                return;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        /* 포스팅 이미지 관련 기능 */
        Collection<Part> parts = request.getParts();
        String uploadPath = "C:/upload/post/"+newId;
        String usedType = "POST";
        ImageService imageService = new ImageService();
        
        for (Part part : parts) {
            // input name="images" 인 파일만 처리
            if ("images".equals(part.getName()) && part.getSize() > 0) {
                // 파일 개수만큼 여러 번 호출
                imageService.uploadFileForPost(autoId, part, uploadPath, usedType, newId);
                // 필요하면 result 체크해서 로깅/에러 처리
            }
        }

        String ctx = request.getContextPath();
        response.sendRedirect(ctx + "/dream/list.do");
    }
}

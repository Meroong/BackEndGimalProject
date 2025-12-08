package controller;

import java.io.IOException;
import java.util.List;

import dto.ItemDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.DreamService;

@WebServlet("/dream/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 15    // 15MB
)
public class DreamController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DreamService dreamService;

    @Override
    public void init() throws ServletException {
        dreamService = new DreamService();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();
        
        System.out.println("DreamController 요청: " + path);

        if (path == null || path.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/dream/list");
            return;
        }

        try {
            switch (path) {
                // 1. 목록 화면 (/dream/list)
                case "/list":
                	System.out.println("목록");
                    List<ItemDTO> list = dreamService.getDreamList();
                    for(ItemDTO dto : list) {
                    	System.out.println(dto.getCategory_id());
                    }
                    req.setAttribute("dreamList", list);
                    req.getRequestDispatcher("/dream/dream_list.jsp").forward(req, resp);
                    break;

                // 2. 등록 화면 (/dream/write)
                case "/write":
                    req.getRequestDispatcher("/dream/dream_write.jsp").forward(req, resp);
                    break;

                // 3. 등록 처리 (/dream/register)
                // 주의: dream_write.jsp의 form action을 "/dream/register"로 수정해야 합니다!
                case "/register":
                    boolean result = dreamService.registerDream(req);
                    if (result) {
                        resp.sendRedirect(req.getContextPath() + "/dream/list");
                    } else {
                        resp.setContentType("text/html; charset=UTF-8");
                        resp.getWriter().write("<script>alert('등록에 실패했습니다.'); history.back();</script>");
                    }
                    break;
                    
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
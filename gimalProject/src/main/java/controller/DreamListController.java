package controller;

import dto.DreamSearchCondition;
import dto.DreamPostDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.AuthUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dao.DreamPostDAO;

@WebServlet("/dream/list.do")
public class DreamListController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String dong = defaultIfBlank(request.getParameter("dong"), "마곡동");
        String keyword = resolveKeyword(request);
        String category = trim(request.getParameter("category"));
        boolean excludeDone = "Y".equals(request.getParameter("excludeDone"));
        boolean newOnly = "Y".equals(request.getParameter("newOnly"));
        String sort = trim(request.getParameter("sort"));
        
        if (sort == null || sort.isEmpty()) {
            sort = "LATEST"; // 기본은 최신순
        }
        
     	// [추가] 내 글만 보기 여부
        boolean mine = "Y".equals(request.getParameter("mine"));

        // [추가] 로그인 사용자 ID
        Long autoId = AuthUtil.getAutoId(request);   // 로그인 안 되어 있으면 -1 또는 null이라고 가정

        // 로그인 안 했는데 mine=Y가 들어온 경우는 무시
        if (mine && (autoId == null || autoId <= 0L)) {
            mine = false;
        }
        
        String[] conditionArr = request.getParameterValues("condition");
        List<String> conditionCodes =
                (conditionArr != null) ? Arrays.asList(conditionArr) : Collections.emptyList();

     // ▶ 체크박스 상태용 boolean 계산
        boolean conditionNew      = conditionCodes.contains("새거");
        boolean conditionLikeNew  = conditionCodes.contains("흠집없는 중고");
        boolean conditionUsed     = conditionCodes.contains("사용감 있는 중고");
        
        DreamSearchCondition cond = new DreamSearchCondition();
        cond.setDong(dong);
        cond.setKeyword(keyword);
        cond.setCategoryCode(category);
        cond.setExcludeDone(excludeDone);
        cond.setNewOnly(newOnly);
        cond.setConditionCodes(conditionCodes);
        cond.setSort(sort);
        
        // mine이 켜져 있으면 이 사용자가 쓴 글만
        if (mine && autoId != null && autoId > 0L) {
            cond.setWriterId(autoId);
        }
        
        DreamPostDAO dpDao = new DreamPostDAO();
        List<DreamPostDTO> dreamList = dpDao.getDreamPostList(cond);

        request.setAttribute("cond", cond);
        request.setAttribute("dreamList", dreamList);

        // JSP에서 체크박스 상태로 사용할 값들
        request.setAttribute("conditionNew", conditionNew);
        request.setAttribute("conditionLikeNew", conditionLikeNew);
        request.setAttribute("conditionUsed", conditionUsed);
        
        // JSP에서 체크박스 상태 찍을 때 쓰기 위함
        request.setAttribute("mine", mine);
        
        RequestDispatcher rd = request.getRequestDispatcher("/dream/list.jsp");
        rd.forward(request, response);
    }

    private String trim(String s) {
        return (s == null) ? null : s.trim();
    }

    private String defaultIfBlank(String s, String def) {
        if (s == null) return def;
        String t = s.trim();
        return t.isEmpty() ? def : t;
    }
    
    // 여러 개의 keyword 파라미터가 올 때, 비어있지 않은 값을 우선으로 선택
    private String resolveKeyword(HttpServletRequest request) {
        String[] keywords = request.getParameterValues("keyword");
        if (keywords == null || keywords.length == 0) {
            return null;
        }

        for (String k : keywords) {
            if (k != null && !k.trim().isEmpty()) {
                return k.trim();   // 첫 번째로 발견한 비어있지 않은 값 사용 (예: 보행기)
            }
        }
        return null; // 전부 빈 문자열이면 null
    }

}

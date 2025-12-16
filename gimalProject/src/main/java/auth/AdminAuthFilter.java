package auth;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.AuthUtil;

@WebFilter("/admin/*")   // /admin 아래 전체에 필터 적용
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 1) 세션에서 JWT 토큰 꺼내기
        HttpSession session = req.getSession(false);
        String authHeader = (session != null)
                ? (String) session.getAttribute("Authorization")
                : null;

        // 로그인 안 되어 있으면 → 로그인 페이지로
        if (authHeader == null || authHeader.isBlank()) {
        	resp.sendRedirect(req.getContextPath() + "/page/login");
            return;
        }

        // 2) 토큰에서 role 꺼내기
        String role = AuthUtil.getRole(req);

        // 토큰이 깨졌거나 role 이 없으면 → 메인으로 돌려보내기
        if (role == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // 3) ADMIN 이 아니면 → 메인으로
        if (!"ADMIN".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // 4) 여기까지 통과하면 관리자이므로 계속 진행
        chain.doFilter(request, response);
    }
}

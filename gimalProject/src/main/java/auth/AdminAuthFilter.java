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

@WebFilter("/admin/*")   // /admin 아래 전체에 적용
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 1) 세션에서 JWT 토큰 꺼내기
        HttpSession session = req.getSession(false);
        String authHeader = null;

        if (session != null) {
            authHeader = (String) session.getAttribute("Authorization");
        }

        // 2) 토큰 없으면 메인으로
        if (authHeader == null || authHeader.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // 3) 토큰에서 role 꺼내서 ADMIN인지 확인
        String role;
        try {
            role = AuthUtil.getRole(req);
        } catch (Exception e) {
            // 토큰 깨졌거나 만료되면 메인으로 튕김
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        if (!"ADMIN".equals(role)) {
            // 관리자 아니면 메인으로
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // 4) 여기까지 통과하면 진짜 관리자니까 다음으로 진행
        chain.doFilter(request, response);
    }
}

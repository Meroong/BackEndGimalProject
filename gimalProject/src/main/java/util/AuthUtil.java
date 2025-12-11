package util;

import java.net.http.HttpRequest;

import auth.JwtAuth;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AuthUtil {
	
	//세션에서 jwt 토큰을 가져옴
	public static Claims getClaim(HttpServletRequest req) {
		HttpSession session = req.getSession(false);	
		if(session != null) {
			String token = (String) session.getAttribute("Authorization");
			return JwtAuth.validateToken(token);
		}
		return null;
	}
	
	//토큰에서 autoId를 가져옴
	public static long getAutoId(HttpServletRequest req) {
		Claims claim = getClaim(req);
		if(claim == null) return -1;
		return claim.get("autoId", Long.class);
	}
	//토큰에서 role을 가져옴
	public static String getRole(HttpServletRequest req) {
		Claims claim = getClaim(req);
		if(claim == null) return null;
		return claim.get("role", String.class);
	}
	
	//로그인 확인
	public boolean isLogedIn (HttpServletRequest req) {
		return getClaim(req) != null;
	}
}

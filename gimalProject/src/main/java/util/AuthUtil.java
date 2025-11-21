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
			String token = (String) session.getAttribute("Authorizaion");
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

}

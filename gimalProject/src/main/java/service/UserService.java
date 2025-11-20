package service;

import java.io.IOException;
import java.net.http.HttpRequest;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import auth.JwtAuth;
import dao.UserDAO;
import dto.ResponseDTO;
import dto.UserDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserService {
	
	//회원가입
	public ResponseDTO registerUser(String userId, String password, String nickName, String userName, String addressIdStr, String addressDetail) {
		UserDAO dao = new UserDAO();


	    System.out.println("registerUser 호출: userId=" + userId + ", password=" + password + ", nickname=" + nickName);

	    if (userId == null || password == null || userName == null) {
	        System.out.println("파라미터 누락!");
	        return new ResponseDTO(false, "아이디, 비밀번호 또는 이름 누락");
	    }

	    if(dao.isNicknameDuplicate(nickName)) {
	        System.out.println("닉네임 중복");
	        return new ResponseDTO(false, "이미 존재하는 닉네임입니다.");
	    }

	    if(dao.isUserIdDuplicate(userId)) {
	        System.out.println("아이디 중복");
	        return new ResponseDTO(false, "이미 존재하는 아이디입니다.");
	    }

	    UserDTO dto = new UserDTO();
	    dto.setUserId(userId);          
	    dto.setUserPassword(password); 
	    dto.setUserName(userName);      
	    dto.setNickname(nickName);      
	    dto.setRole("USER");                                    
	    dto.setAddressId(1);                                    // 기본값 디비 방법 정하고 추후 수정 예정


	    // addressId 유효한 값으로 설정
	    try {
	        long addressId = (addressIdStr != null && !addressIdStr.isEmpty()) ? Integer.parseInt(addressIdStr) : 1; 
	        dto.setAddressId(addressId);
	    } catch (NumberFormatException e) {
	        dto.setAddressId(1); // 기본값 1로 처리
	    }



	    boolean result = dao.insert(dto);
	    System.out.println("회원가입 결과: " + result);

	    return result ? new ResponseDTO(true, "회원가입 성공") 
	                  : new ResponseDTO(false, "회원가입 실패");
	}

	//로그인 jwt토큰 방식
	public ResponseDTO loginUser (String id, String password, HttpSession session) {
		UserDAO dao = new UserDAO();
		UserDTO dto = new UserDTO();
		
		if(id == null) {
			return new ResponseDTO(false, "아이디를 입력해주세요.");
		}
		if(password == null) {
			return new ResponseDTO(false, "비밀번호를 입력해주세요.");
		}
		dto.setUserId(id);
		dto.setUserPassword(password);
		dto = dao.searchForLogin(id, password);
		
		if(dto != null) {
			JwtAuth auth = new JwtAuth();
			String jwt = auth.generateToken(dto.getUserId(), dto.getAutoId(), dto.getRole());
			session.setAttribute("Authorization", "Bearer "+jwt);
			System.out.println("로그인 성공");
			HttpServletResponse res;
			res.sendRedirect(path + "/views/index.jsp");
			return  new ResponseDTO(true,"로그인 성공!");
		}
		else return  new ResponseDTO(false,"로그인 실패!");
		
		
	}


	//탈퇴
	
	//회원정보수정
	//HttpServletRequest request 서비스에서 이렇게 처리하지 않고 컨트롤러에서 하도록 http 객체 의존으로 인한 아키텍처 위반
	
	public ResponseDTO updateUser(HttpServletRequest request) {
		UserDAO dao = new UserDAO();
		UserDTO dto = new UserDTO();
		
		
		
		
		
		
		
		return new ResponseDTO(true, "회원정보 수정성공");
	}
	
	//회원정보조회
	//로그아웃
	public void logoutUser (HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		String path = request.getContextPath();

		try {
			response.sendRedirect(path + "/views/index.jsp");
			System.out.println("로그아웃 성공!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("로그아웃 실패!!");
			e.printStackTrace();
		}
	}
}

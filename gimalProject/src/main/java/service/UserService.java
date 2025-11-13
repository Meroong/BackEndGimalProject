package service;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import auth.JwtAuth;
import dao.UserDAO;
import dto.ResponseDTO;
import dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserService {

	//로그인 jwt토큰 방식
	public ResponseDTO loginUser (HttpServletRequest request, HttpServletResponse response) {
		UserDAO dao = new UserDAO();
		UserDTO dto = new UserDTO();
		String id = request.getParameter("userId");
		String password = request.getParameter("userPassword");
		
		if(id == null) {
			return new ResponseDTO("fail", "아이디를 입력해주세요.");
		}
		if(password == null) {
			return new ResponseDTO("fail", "비밀번호를 입력해주세요.");
		}
		dto.setUserId(id);
		dto.setUserPassword(password);
		dto = dao.searchForLogin(id, password);
		
		if(dto != null) {
			JwtAuth auth = new JwtAuth();
			String jwt = auth.generateToken(dto.getUserId(), dto.getAutoId(), dto.getRole());
			response.setHeader("Authorization", "Bearer "+jwt);
			System.out.println("로그인 성공");
		}
		
		return  new ResponseDTO("success","로그인 성공!");
	}
	 
	
	public ResponseDTO registerUser(HttpServletRequest request) {
		UserDAO dao = new UserDAO();
	    String userId = request.getParameter("userId");
	    String password = request.getParameter("userPassword");
	    String nickname = request.getParameter("nickname");
	    String userName = request.getParameter("userName"); // 회원 이름
	    String addressIdStr = request.getParameter("addressId"); // 문자열로 받아옴
	    String addressDetail = request.getParameter("addressDetail");

	    System.out.println("registerUser 호출: userId=" + userId + ", password=" + password + ", nickname=" + nickname);

	    if (userId == null || password == null || userName == null) {
	        System.out.println("파라미터 누락!");
	        return new ResponseDTO("fail", "아이디, 비밀번호 또는 이름 누락");
	    }

	    if(dao.isNicknameDuplicate(nickname)) {
	        System.out.println("닉네임 중복");
	        return new ResponseDTO("fail", "이미 존재하는 닉네임입니다.");
	    }

	    if(dao.isUserIdDuplicate(userId)) {
	        System.out.println("아이디 중복");
	        return new ResponseDTO("fail", "이미 존재하는 아이디입니다.");
	    }

	    UserDTO dto = new UserDTO();
	    dto.setUserId(request.getParameter("userId"));          
	    dto.setUserPassword(request.getParameter("userPassword")); 
	    dto.setUserName(request.getParameter("userName"));      
	    dto.setNickname(request.getParameter("nickname"));      
	    dto.setRole("USER");                                    
	    dto.setAddressId(1);                                    // 기본값 디비 방법 정하고 추후 수정 예정


	    // addressId 유효한 값으로 설정
	    try {
	        int addressId = (addressIdStr != null && !addressIdStr.isEmpty()) ? Integer.parseInt(addressIdStr) : 1; 
	        dto.setAddressId(addressId);
	    } catch (NumberFormatException e) {
	        dto.setAddressId(1); // 기본값 1로 처리
	    }



	    boolean result = dao.insert(dto);
	    System.out.println("회원가입 결과: " + result);

	    return result ? new ResponseDTO("success", "회원가입 성공") 
	                  : new ResponseDTO("fail", "회원가입 실패");
	}


	//탈퇴
	
	//회원정보수정
	
	//
}

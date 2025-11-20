package service;

import auth.JwtAuth;
import dao.UserDAO;
import dto.ResponseDTO;
import dto.UserDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UserService {
	
	private final UserDAO userDAO = new UserDAO();
    private final JwtAuth jwtAuth = new JwtAuth();
	
//회원가입
	public ResponseDTO registerUser(HttpServletRequest request) {
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

	    if(nickname != null && !nickname.trim().isEmpty() && userDAO.isNicknameDuplicate(nickname)) {
	        System.out.println("닉네임 중복");
	        return new ResponseDTO("fail", "이미 존재하는 닉네임입니다.");
	    }

	    if(userDAO.isUserIdDuplicate(userId)) {
	        System.out.println("아이디 중복");
	        return new ResponseDTO("fail", "이미 존재하는 아이디입니다.");
	    }

	    UserDTO dto = new UserDTO();
	    dto.setUserId(userId);          
	    dto.setUserPassword(password); 
	    dto.setUserName(nickname);      
	    dto.setNickname(userName);      
	    dto.setRole("USER");                                    
	    dto.setAddressId(1);                                    // 기본값 디비 방법 정하고 추후 수정 예정


	    // addressId 유효한 값으로 설정
	    try {
	        int addressId = (addressIdStr != null && !addressIdStr.isEmpty()) ? Integer.parseInt(addressIdStr) : 1; 
	        dto.setAddressId(addressId);
	    } catch (NumberFormatException e) {
	        dto.setAddressId(1); // 기본값 1로 처리
	    }



	    boolean result = userDAO.insert(dto);
	    System.out.println("회원가입 결과: " + result);

	    return result ? new ResponseDTO("success", "회원가입 성공") 
	                  : new ResponseDTO("fail", "회원가입 실패");
	}

//로그인
	public ResponseDTO loginUser (HttpServletRequest request) {
		UserDTO dto = new UserDTO();
		HttpSession session =  request.getSession();
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
		dto = userDAO.searchForLogin(id, password);
		
		if(dto != null) { // 로그인 성공시 토근 생성, 세션 저장
			String jwt = jwtAuth.generateToken(dto.getUserId(), dto.getAutoId(), dto.getRole());
			session.setAttribute("Authorization", "Bearer "+jwt);
			
			// 보안상 비밀번호 제거 후 정보 반환
			dto.setUserPassword(null);
			System.out.println("로그인 성공");
			return  new ResponseDTO("success","로그인 성공!");
		}
		else return  new ResponseDTO("fail","로그인 실패!");
		
		
	}
	
// 내 정보 조회
	public ResponseDTO getMyInfo(HttpServletRequest request) {
        int autoId = getAutoIdFromToken(request);
        
        if (autoId == -1) {
            return new ResponseDTO("fail", "로그인이 필요하거나 유효하지 않은 토큰입니다.");
        }

        // DB 조회
        UserDTO user = userDAO.searchByAutoId(autoId);
        if (user != null) {
            user.setUserPassword(null); 
            return new ResponseDTO("success", "정보 조회 성공");
        }
        return new ResponseDTO("fail", "사용자 정보를 찾을 수 없습니다.");
    }


// 탈퇴
	public ResponseDTO deleteUser(HttpServletRequest request) {
		int autoId = getAutoIdFromToken(request);
		
		if (autoId == -1) {
            return new ResponseDTO("fail", "로그인이 필요하거나 유효하지 않은 토큰입니다.");
        }
		
		try {
            // DB 삭제
            userDAO.delete(autoId);
            
            // 세션 만료 (로그아웃)
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
            
            return new ResponseDTO("success", "회원 탈퇴가 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO("fail", "탈퇴 처리 중 오류가 발생했습니다.");
        }
	}
	
// 회원 정보 수정
	public ResponseDTO updateUser(HttpServletRequest request) {
		int autoId = getAutoIdFromToken(request);
		
		if (autoId == -1) {
            return new ResponseDTO("fail", "로그인이 필요하거나 유효하지 않은 토큰입니다.");
        }
		
		String newPassword = request.getParameter("userPassword");
		String newNickname = request.getParameter("nickname");
		String addressIdStr = request.getParameter("addressId");
		String addressDetail = request.getParameter("addressDetail");
		
		UserDTO dto = new UserDTO();
		dto.setAutoId(autoId); // DAO의 WHERE auto_id = ? 에 사용됨
		
		boolean hasUpdates = false; // 수정할 내용 존재 여부 플래그
		
		// 비밀번호 변경 로직
		if (newPassword != null && !newPassword.trim().isEmpty()) {
			dto.setUserPassword(newPassword);
			hasUpdates = true;
		}
		
		// 닉네임 변경 로직 (줌복 체크 포함)
		if (newNickname != null && !newNickname.trim().isEmpty()) {
			// 현재 내 정보 가져오기 (기존 닉네임 확인용)
			UserDTO currentUser = userDAO.searchByAutoId(autoId);
			
			// 입력한 닉네임이 현재 내 닉네임과 다를 경우에만 중복 체크 수행
			if (currentUser != null && !newNickname.equals(currentUser.getNickname())) {
				if (userDAO.isNicknameDuplicate(newNickname)) {
					return new ResponseDTO("fail", "이미 사용 중인 닉네임입니다.");
				}
			}
			
			// 중복이 아니거나 내 기존 닉네임과 같다면 수정 반영
			dto.setNickname(newNickname);
			hasUpdates = true;
		}
		
		// 주소 변경 로직
		if (addressIdStr != null && !addressIdStr.trim().isEmpty()) {
			try {
				int addrId = Integer.parseInt(addressIdStr);
				if (addrId != 0) {
					dto.setAddressId(addrId);
					hasUpdates = true;
				}
			} catch (NumberFormatException e) {
				return new ResponseDTO("fail", "잘못된 주소 형식입니다.");
			}
		}
		
		if (addressDetail != null) { 
			dto.setAddressDetail(addressDetail);
			hasUpdates = true;
		}
		
		
		// 변경할 내용 없으면 종료
		if (!hasUpdates) {
			return new ResponseDTO("fail", "수정할 정보를 입력해주세요.");
		}
		
		// 업데이트 (DAO 호출)
		try {
			userDAO.updateUser(dto);
			return new ResponseDTO("success", "회원정보 수정성공");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseDTO("fail", "회원정보 수정 중 오류가 발생했습니다.");
		}
	}
	
// (공통 메서드) 토큰에서 autoId 추출
	private int getAutoIdFromToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return -1;

        // 1. 세션에서 토큰 문자열 가져오기
        String jwtToken = (String) session.getAttribute("Authorization");
        
        // 2. 토큰이 없거나 형식이 안 맞으면 실패
        if (jwtToken == null) return -1;

        // "Bearer " 접두사 제거
        if (jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }

        // 3. 토큰 검증 (JwtAuth 클래스 활용)
        // validateToken이 실패하면 null을 반환한다고 가정
        Claims claims = jwtAuth.validateToken(jwtToken);
        
        if (claims == null) {
            return -1;
        }

        // 4. 토큰 내부의 autoId 반환
        try {
            return (Integer) claims.get("autoId");
        } catch (Exception e) {
            return -1;
        }
    }
}

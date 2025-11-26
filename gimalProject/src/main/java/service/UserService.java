package service;

import dao.ChatRoomUserDAO;
import dao.UserAddressDAO;
import dao.UserDAO;
import dto.ResponseDTO;
import dto.UserAddressDTO;
import dto.UserDTO;
import jakarta.servlet.http.HttpSession;

public class UserService {

    private final UserDAO userDAO = new UserDAO();
    private final UserAddressDAO addressDAO = new UserAddressDAO();

    // =============================
    // 회원가입
    // =============================
    public ResponseDTO registerUser(
            String userId,
            String password,
            String nickname,
            String userName,
            String roadAddress,
            String jibunAddress,
			String addrDetail/*
								 * , //추후 확장 예정 String latitudeStr, String longitudeStr
								 */

    ) {
        System.out.println("registerUser 호출: userId=" + userId + ", password=" + password + ", nickname=" + nickname);

        if (userId == null || password == null || userName == null) {
            return new ResponseDTO(false, "아이디, 비밀번호 또는 이름 누락");
        }

        if (nickname != null && !nickname.trim().isEmpty() && userDAO.isNicknameDuplicate(nickname)) {
            return new ResponseDTO(false, "이미 존재하는 닉네임입니다.");
        }

        if (userDAO.isUserIdDuplicate(userId)) {
            return new ResponseDTO(false, "이미 존재하는 아이디입니다.");
        }
        //1. 유저정보 저장 
        UserDTO dto = new UserDTO();
        dto.setUserId(userId);
        dto.setUserPassword(password);
        dto.setUserName(userName);
        dto.setNickname(nickname);
        dto.setRole("USER");
        
        

        long autoId = userDAO.insert(dto);
        System.out.println("회원가입"+autoId);
        if (autoId == -1)
        	
            return new ResponseDTO(false, "회원가입 실패(유저 저장 오류)");
        
        
        // 2) 주소 정보 저장
        UserAddressDTO addr = new UserAddressDTO();
        addr.setUserId(autoId);
        addr.setRoadAddress(roadAddress);
        addr.setJibunAddress(jibunAddress);
        addr.setAddrDetail(addrDetail);
        int result = addressDAO.saveOrUpdate(addr);
		/*
		 * try { if (latitudeStr != null && !latitudeStr.isEmpty())
		 * addr.setLatitude(Double.parseDouble(latitudeStr)); if (longitudeStr != null
		 * && !longitudeStr.isEmpty())
		 * addr.setLongitude(Double.parseDouble(longitudeStr)); } catch (Exception e) {
		 * return new ResponseDTO(false, "위도/경도 값이 잘못되었습니다."); }
		 */
        
        return result > 0
                ? new ResponseDTO(true, "회원가입 성공")
                : new ResponseDTO(false, "회원가입 실패");
    }

    // =============================
    // 로그인 (DTO 반환, 세션/JWT는 컨트롤러에서 처리)
    // =============================
    public UserDTO loginUser(String id, String password) {
    	System.out.println("로그인서비스:");
        if (id == null || id.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;

        UserDTO dto = userDAO.searchForLogin(id, password);
        System.out.println(dto);
        
        if (dto != null) {
            dto.setUserPassword(null); // 보안 목적
            return dto;
        }
        return null;
    }

    // =============================
    // 내 정보 조회  수정 필요(손주성)
    // =============================
    public UserDTO getMyInfo(long autoId) {
        return userDAO.searchByAutoId(autoId);
    }

    // =============================
    // 회원 탈퇴
    // =============================
    public boolean deleteUser(long autoId) {
    	System.out.println("deleteUserService:");
        try {

            // 주소 삭제
            addressDAO.deleteAddress(autoId);

            // 채팅방 관련 데이터 삭제
            new ChatRoomUserDAO().quitRoomForDeleteUser(autoId);

            // 최종 유저 삭제
            int userDeleted = userDAO.delete(autoId);
            if (userDeleted == 0) {
                return false;
            }



            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // =============================
    // 회원 정보 수정
    // =============================
    public ResponseDTO updateUser(
            long autoId,
            String newPassword,
            String newNickname,
            String roadAddress,
            String jibunAddress,
			String addrDetail/*
								 * , String latitudeStr, String longitudeStr
								 */
    ) {
    	//1. User 수정
        UserDTO dto = new UserDTO();
        dto.setAutoId(autoId);
        boolean hasUpdates = false;

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            dto.setUserPassword(newPassword);
            hasUpdates = true;
        }

        if (newNickname != null && !newNickname.trim().isEmpty()) {
            UserDTO current = userDAO.searchByAutoId(autoId);
            if (current != null && !newNickname.equals(current.getNickname())) {
                if (userDAO.isNicknameDuplicate(newNickname)) {
                    return new ResponseDTO(false, "이미 사용 중인 닉네임입니다.");
                }
            }
            dto.setNickname(newNickname);
            hasUpdates = true;
        }
        if (hasUpdates) {
            userDAO.updateUser(dto);
        }
        // ===== 2) 주소 수정 =====
        UserAddressDTO addr = new UserAddressDTO();
        addr.setUserId(autoId);
        addr.setRoadAddress(roadAddress);
        addr.setJibunAddress(jibunAddress);
        addr.setAddrDetail(addrDetail);

        //추후 확장 예정 카카오맵
		/*
		 * try { if (latitudeStr != null && !latitudeStr.isEmpty())
		 * addr.setLatitude(Double.parseDouble(latitudeStr)); if (longitudeStr != null
		 * && !longitudeStr.isEmpty())
		 * addr.setLongitude(Double.parseDouble(longitudeStr)); } catch (Exception e) {
		 * return new ResponseDTO(false, "잘못된 위도/경도 값입니다."); }
		 */
        addressDAO.saveOrUpdate(addr);

        //유저 정보의 hasUpdates를 확장하여 주소 정보까지 확인
        if (!hasUpdates &&
                (roadAddress == null && jibunAddress == null && addrDetail == null))
            return new ResponseDTO(false, "수정할 내용을 입력해주세요.");

        try {
            
            return new ResponseDTO(true, "회원정보 수정성공");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "회원정보 수정 중 오류가 발생했습니다.");
        }
    }

    // =============================
    // 로그아웃 (컨트롤러에서 세션 invalidate)
    // =============================
    public void logoutUser(HttpSession session) {
        session.invalidate();
        System.out.println("로그아웃 성공!!");
    }
    public UserAddressDTO getAddressInfo(long autoId) {
    	UserAddressDTO dto = new UserAddressDTO();
    	//오토아이디 기반으로 주소 정보 가져오기 
    	dto =addressDAO.getAddressByUserId(autoId);
    	
    	if(dto == null) {
    		System.out.println("getAddressInfo: dto is null");
    		dto = new UserAddressDTO();
    	}
    	return dto;
    }
}

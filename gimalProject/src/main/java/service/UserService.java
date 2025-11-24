package service;

import dao.UserDAO;
import dto.ResponseDTO;
import dto.UserDTO;
import jakarta.servlet.http.HttpSession;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    // =============================
    // 회원가입
    // =============================
    public ResponseDTO registerUser(
            String userId,
            String password,
            String nickname,
            String userName,
            String addressIdStr,
            String addressDetail
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

        UserDTO dto = new UserDTO();
        dto.setUserId(userId);
        dto.setUserPassword(password);
        dto.setUserName(userName);
        dto.setNickname(nickname);
        dto.setRole("USER");

        try {
            long addrId = (addressIdStr != null && !addressIdStr.trim().isEmpty())
                    ? Integer.parseInt(addressIdStr)
                    : 1;
            dto.setAddressId(addrId);
        } catch (NumberFormatException e) {
            dto.setAddressId(1);
        }

        dto.setAddressDetail(addressDetail);

        boolean result = userDAO.insert(dto);

        return result
                ? new ResponseDTO(true, "회원가입 성공")
                : new ResponseDTO(false, "회원가입 실패");
    }

    // =============================
    // 로그인 (DTO 반환, 세션/JWT는 컨트롤러에서 처리)
    // =============================
    public UserDTO loginUser(String id, String password) {
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
    // 내 정보 조회
    // =============================
    public UserDTO getMyInfo(int autoId) {
        return userDAO.searchByAutoId(autoId);
    }

    // =============================
    // 회원 탈퇴
    // =============================
    public boolean deleteUser(int autoId) {
        try {
            userDAO.delete(autoId);
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
            String addressIdStr,
            String addressDetail
    ) {
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

        if (addressIdStr != null && !addressIdStr.trim().isEmpty()) {
            try {
                int addr = Integer.parseInt(addressIdStr);
                dto.setAddressId(addr);
                hasUpdates = true;
            } catch (NumberFormatException e) {
                return new ResponseDTO(false, "잘못된 주소 형식입니다.");
            }
        }

        if (addressDetail != null) {
            dto.setAddressDetail(addressDetail);
            hasUpdates = true;
        }

        if (!hasUpdates) return new ResponseDTO(false, "수정할 정보를 입력해주세요.");

        try {
            userDAO.updateUser(dto);
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
}

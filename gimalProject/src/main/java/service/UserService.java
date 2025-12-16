package service;

import dao.ChatRoomUserDAO;
import dao.FileResourceDAO;
import dao.UserAddressDAO;
import dao.UserDAO;
import dto.UserAddressDTO;
import dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import util.DongUtil;

public class UserService {

    private final UserDAO userDAO = new UserDAO();
    private final UserAddressDAO addressDAO = new UserAddressDAO();


    // =============================
    // 회원가입 (예외 기반)
    // =============================
    public void registerUser(
            String userId,
            String password,
            String nickname,
            String userName,
            String roadAddress,
            String jibunAddress,
            String addrDetail
    ) {
        System.out.println("registerUser 호출");

        if (userId == null || password == null || userName == null) {
            throw new RuntimeException("아이디, 비밀번호 또는 이름 누락");
        }

        if (nickname != null && !nickname.trim().isEmpty() && userDAO.isNicknameDuplicate(nickname)) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        if (userDAO.isUserIdDuplicate(userId)) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // 유저 저장
        UserDTO dto = new UserDTO();
        dto.setUserId(userId);
        dto.setUserPassword(password);
        dto.setUserName(userName);
        dto.setNickname(nickname);
        dto.setRole("USER");

        long autoId = userDAO.insert(dto);
        if (autoId == -1) {
            throw new RuntimeException("회원가입 실패 (유저 저장 오류)");
        }

        // 주소 저장
        UserAddressDTO addr = new UserAddressDTO();
        addr.setUserId(autoId);
        addr.setRoadAddress(roadAddress);
        addr.setJibunAddress(jibunAddress);
        addr.setAddrDetail(addrDetail);

        int result = addressDAO.saveOrUpdate(addr);
        if (result <= 0) {
            throw new RuntimeException("회원가입 실패 (주소 저장 오류)");
        }
    }


    // =============================
    // 로그인
    // =============================
    public UserDTO loginUser(String id, String password) {
        if (id == null || id.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;

        UserDTO dto = userDAO.searchForLogin(id, password);
        if (dto != null) dto.setUserPassword(null);

        return dto;
    }


    // =============================
    // 내 정보 조회
    // =============================
    public UserDTO getUserInfo(long autoId) {
        return userDAO.searchByAutoId(autoId);
    }


    // =============================
    // 회원 탈퇴 (예외 기반)
    // =============================
    public void deleteUser(long autoId) {
        System.out.println("deleteUserService:");

        try {
            boolean rs = new FileResourceDAO().deleteFileByUsed("PROFILE", autoId);
            if (!rs) {
                throw new RuntimeException("프로필 이미지 삭제 실패");
            }

            int userDeleted = userDAO.delete(autoId);
            if (userDeleted == 0) {
                throw new RuntimeException("회원 삭제 실패");
            }

        } catch (Exception e) {
            throw new RuntimeException("회원 삭제 중 오류 발생: " + e.getMessage());
        }
    }


    // =============================
    // 회원 정보 수정 (예외 기반)
    // =============================
    public void updateUser(
            long autoId,
            String newPassword,
            String newNickname,
            String roadAddress,
            String jibunAddress,
            String addrDetail,
            String latitudeStr,
            String longitudeStr
    ) {

        System.out.println("updateUser 호출");

        boolean hasUpdates = false;

        // 1. User 수정
        UserDTO dto = new UserDTO();
        dto.setAutoId(autoId);

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            dto.setUserPassword(newPassword);
            hasUpdates = true;
        }

        if (newNickname != null && !newNickname.trim().isEmpty()) {
            UserDTO current = userDAO.searchByAutoId(autoId);

            if (current != null && !newNickname.equals(current.getNickname())) {
                if (userDAO.isNicknameDuplicate(newNickname)) {
                    throw new RuntimeException("이미 사용 중인 닉네임입니다.");
                }
            }
            dto.setNickname(newNickname);
            hasUpdates = true;
        }

        if (hasUpdates) {
            userDAO.updateUser(dto);
        }

        // 2. 주소 수정
        UserAddressDTO addr = new UserAddressDTO();
        addr.setUserId(autoId);
        addr.setRoadAddress(roadAddress);
        addr.setJibunAddress(jibunAddress);
        addr.setAddrDetail(addrDetail);

        try {
            if (latitudeStr != null && !latitudeStr.isEmpty())
                addr.setLatitude(Double.parseDouble(latitudeStr));
            if (longitudeStr != null && !longitudeStr.isEmpty())
                addr.setLongitude(Double.parseDouble(longitudeStr));
        } catch (Exception e) {
            throw new RuntimeException("잘못된 위도/경도 값입니다.");
        }
        //동음면리 정보 추출 
        DongUtil dongUtil = new DongUtil();
        String dongName = dongUtil.extractAreaUnit(jibunAddress);
        System.out.println(dongName);
        addr.setDongName(dongName);

        addressDAO.saveOrUpdate(addr);

        // 변경된 것이 하나도 없으면 오류
        if (!hasUpdates &&
                (roadAddress == null && jibunAddress == null && addrDetail == null)) {
            throw new RuntimeException("수정할 내용을 입력해주세요.");
        }
    }


    // =============================
    // 로그아웃
    // =============================
    public void logoutUser(HttpSession session) {
        session.invalidate();
        System.out.println("로그아웃 성공");
    }


    // =============================
    // 주소 정보 조회
    // =============================
    public UserAddressDTO getAddressInfo(long autoId) {
        UserAddressDTO dto = addressDAO.getAddressByUserId(autoId);

        if (dto == null) {
            return new UserAddressDTO();
        }
        return dto;
    }
}

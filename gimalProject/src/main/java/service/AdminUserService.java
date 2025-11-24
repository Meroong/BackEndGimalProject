package service;

import java.util.List;

import dao.UserDAO;
import dto.UserDTO;

public class AdminUserService {

    private UserDAO userDAO = new UserDAO();

    // 1) 회원 전체 목록
    public List<UserDTO> getUserList() {
        return userDAO.findAllUsers();   // ← UserDAO에 실제 있는 메서드
    }

    // 2) 회원 한 명 조회 (상세보기용)
    public UserDTO getUserById(long id) {
        return userDAO.findAllUsers()    // 전체에서
                .stream()
                .filter(u -> u.getAutoId() == id) // autoId로 한 명만 골라서
                .findFirst()
                .orElse(null);
    }
}

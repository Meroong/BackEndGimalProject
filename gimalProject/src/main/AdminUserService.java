package service;

import java.util.List;

import dao.AdminUserDAO;
import dao.UserDAO;
import dto.UserDTO;

public class AdminUserService {

    private UserDAO userDAO = new UserDAO();
    private AdminUserDAO adminUserDAO = new AdminUserDAO();

    // 전체 회원 목록
    public List<UserDTO> getUserList() {
        return userDAO.findAllUsers();
    }

    // 회원 한 명 조회 (상세보기용)
    public UserDTO getUserById(long id) {
        return userDAO.findAllUsers()
                .stream()
                .filter(u -> u.getAutoId() == id)
                .findFirst()
                .orElse(null);
    }

    // 회원 정지 (role → BLOCKED)
    public boolean blockUser(long id) {
        return adminUserDAO.updateRole(id, "BLOCKED") > 0;
    }

    // 정지 해제 (role → USER)
    public boolean unblockUser(long id) {
        return adminUserDAO.updateRole(id, "USER") > 0;
    }
    public int deleteUser(long autoId) {
        return adminUserDAO.deleteUser(autoId);
    }

}

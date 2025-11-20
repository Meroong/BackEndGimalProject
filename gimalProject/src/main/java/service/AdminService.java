package service;

import java.util.List;

import dao.UserDAO;
import dto.UserDTO;

public class AdminService {

    private UserDAO userDAO = new UserDAO();

    // 전체 회원 조회
    public List<UserDTO> getAllUsers() {
        return userDAO.findAllUsers();
    }

}

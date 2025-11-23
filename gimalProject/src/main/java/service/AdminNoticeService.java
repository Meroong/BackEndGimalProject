package service;

import java.util.List;

import dao.AdminNoticeDAO;
import dto.AdminNoticeDTO;

public class AdminNoticeService {

    private AdminNoticeDAO adminNoticeDAO = new AdminNoticeDAO();

    // 공지 목록 가져오기
    public List<AdminNoticeDTO> getNoticeList() {
        return adminNoticeDAO.findAll();
    }
}

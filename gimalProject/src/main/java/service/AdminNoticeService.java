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
 // 공지 등록
    public boolean writeNotice(AdminNoticeDTO dto) {
        int result = adminNoticeDAO.insert(dto);
        return result > 0;
    }
 // 공지 수정
    public boolean updateNotice(AdminNoticeDTO dto) {
        return adminNoticeDAO.update(dto) > 0;
    }
    public boolean deleteNotice(long id) {
        return adminNoticeDAO.delete(id) > 0;
    }

}

package service;

import java.util.List;
import dao.DreamDAO;
import dto.ItemDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class DreamService {
    
    private final DreamDAO itemDAO = new DreamDAO();

    // 목록 조회
    public List<ItemDTO> getDreamList() {
        return itemDAO.selectListByType("DREAM");
    }

    // 드림 등록
    public boolean registerDream(HttpServletRequest request) {
        try {
            // Multipart 요청일 경우 request.getParameter가 null일 수 있음.
            // 컨트롤러에서 @MultipartConfig 설정을 해야 getParameter가 정상 작동함.
            String tradeType = request.getParameter("tradeType");
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String categoryIdStr = request.getParameter("categoryId");
            String sellerIdStr = request.getParameter("sellerId");
            String priceStr = request.getParameter("price");

            // 유효성 검사
            if (title == null || content == null || sellerIdStr == null) {
                System.out.println("필수 파라미터 누락");
                return false;
            }
            ItemDTO dto = new ItemDTO();
            dto.setTrade_type(tradeType); // "DREAM"
            dto.setTitle(title);
            dto.setContent(content);
            dto.setCategory_id(Integer.parseInt(categoryIdStr));
            dto.setSeller_id(Integer.parseInt(sellerIdStr));
            dto.setPrice(Integer.parseInt(priceStr)); // 0

            // 파일 업로드 처리 로직 (file_resource 테이블 insert)은 여기에 추가되어야 함
            // 여기서는 생략하고 item 테이블만 저장
            
            return itemDAO.insert(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
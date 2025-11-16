package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.ChatRoomDTO;
import util.JDBCUtil;

public class ChatRoomDAO {
    private long id;              // 채팅방 식별자
    private long itemId;          // 상품 ID (거래 채팅일 경우)
    private String roomType;      // 채팅방 유형 (PRIVATE / GROUP)
    private String createdAt;     // 생성일시
    
 
    //채팅방 목록 조회
    public ArrayList<ChatRoomDTO> getChatRoomById(int id){
    	ArrayList<ChatRoomDTO> chatList = new ArrayList<ChatRoomDTO>();
    	String sql = "select * from chat_room where auto_id = ?;";
    	ResultSet rs = null;
    	
    	//연결 및 쿼리
    	try {
        	PreparedStatement pstmt = JDBCUtil.jdbcCon().prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("채팅방 목록 조회 실패!");
			e.printStackTrace();
		}
    	try {
			while(rs.next()) {
				ChatRoomDTO dto = new ChatRoomDTO();
				
				dto.setUserId(id);
				dto.setRoomId(rs.getInt("id"));
				dto.setItemId(rs.getInt("item_id"));
				dto.setRoomType(rs.getString("room_type"));
				chatList.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("채팅방 목록 세팅 중 에러!");
			e.printStackTrace();
		}
    	return chatList;
    }
    // 채팅방 생성
    
    // 채팅방 삭제
    
    //채팅방 나가기
}

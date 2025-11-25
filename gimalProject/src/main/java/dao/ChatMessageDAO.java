package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import dto.ChatMessageDTO;
import util.JDBCUtil;

public class ChatMessageDAO {
	
	//채팅방 아이디 기반으로 메시지 가져오기
	public ArrayList<ChatMessageDTO> getMessageByRoomId(long roomId){
		ArrayList<ChatMessageDTO> messageList = new ArrayList<ChatMessageDTO>();
		ResultSet rs = null;
		String sql = "select * from chat_message where room_id = ?;";
    	
    	try(Connection con = JDBCUtil.jdbcCon();
    		PreparedStatement pstmt = con.prepareStatement(sql);) {
 
    		pstmt.setLong(1, roomId);
    		
    		rs= pstmt.executeQuery();
    		
    		while(rs.next()) {
    			ChatMessageDTO dto = new ChatMessageDTO();
    			dto.setRoomId(rs.getLong("room_id"));
    			dto.setMessageId(rs.getLong("message_id"));
    			dto.setContent(rs.getString("content"));
    			dto.setSenderId(rs.getLong("sender_id"));
    			dto.setSentAt(rs.getTimestamp("sent_at"));
    			messageList.add(dto);

    		}
    	} catch (SQLException e) {
    		System.out.println("채팅방 서치 쿼리 중 에러!");
    		e.printStackTrace();
    	}
    	return messageList;
	}
	
	//채팅 전송 dao
	public int sendMessage(ChatMessageDTO dto) {
		String sql = "insert into chat_message(room_id, sender_id, content) values(?, ?, ?);";
		
		try(Connection con = JDBCUtil.jdbcCon();
	    	PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			pstmt.setLong(1, dto.getRoomId());
			pstmt.setLong(2, dto.getSenderId());
			pstmt.setString(3, dto.getContent());
			
			//디비 쿼리 결과 영향받은 행 수를 반환
			return pstmt.executeUpdate();
			
		}catch (SQLException e) {
			System.out.println("채팅 전송 디비 쿼리 중 에러!");
			e.printStackTrace();
			return 0;
		}
	}
	public int
}

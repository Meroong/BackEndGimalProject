package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import dto.ChatMessageDTO;
import util.JDBCUtil;

public class ChatMessageDAO {
	public ArrayList<ChatMessageDTO> getMessageByRoomId(long roomId){
		ArrayList<ChatMessageDTO> messageList = new ArrayList<ChatMessageDTO>();
		ResultSet rs = null;
		String sql = "select * from chat_message where room_id = ?;";
    	
    	try {
    		PreparedStatement pstmt = JDBCUtil.jdbcCon().prepareStatement(sql);
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
    		System.out.println("채팅방 삭제 중 에러!");
    		e.printStackTrace();
    	}
    	return messageList;
	}
	public void sendMessage(ChatMessageDTO dto) {
		String sql = "insert into chat_message values("

	}
}

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
	public ArrayList<ChatMessageDTO> getMessageByRoomId(long roomId) {

	    ArrayList<ChatMessageDTO> messageList = new ArrayList<>();

	    String sql =
	        "SELECT "
	      + " m.message_id, "
	      + " m.room_id, "
	      + " m.sender_id, "
	      + " m.message_type, "
	      + " m.content, "
	      + " m.sent_at, "
	      + " u.nickname AS senderNickname, "
	      + " pf.file_url AS senderProfile, "
	      + " cf.file_url AS imageUrl "
	      + "FROM chat_message m "
	      + "LEFT JOIN user u ON m.sender_id = u.auto_id "

	      // 프로필 이미지
	      + "LEFT JOIN file_resource pf "
	      + " ON pf.used_type = 'PROFILE' "
	      + " AND pf.used_id = u.auto_id "

	      // 채팅 이미지
	      + "LEFT JOIN file_resource cf "
	      + " ON cf.used_type = 'CHAT' "
	      + " AND cf.used_id = m.message_id "

	      + "WHERE m.room_id = ? "
	      + "ORDER BY m.sent_at ASC";

	    try (Connection con = JDBCUtil.jdbcCon();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        pstmt.setLong(1, roomId);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {

	                ChatMessageDTO dto = new ChatMessageDTO();

	                dto.setMessageId(rs.getLong("message_id"));
	                dto.setRoomId(rs.getLong("room_id"));
	                dto.setSenderId(rs.getLong("sender_id"));
	                dto.setMessageType(rs.getString("message_type"));
	                dto.setContent(rs.getString("content"));
	                dto.setSentAt(rs.getTimestamp("sent_at"));

	                // JOIN 컬럼
	                dto.setSenderNickname(rs.getString("senderNickname"));
	                dto.setSenderProfile(rs.getString("senderProfile"));
	                dto.setImageUrl(rs.getString("imageUrl"));

	                messageList.add(dto);
	            }
	        }

	    } catch (SQLException e) {
	        System.out.println("채팅방 메시지 조회 중 에러!");
	        e.printStackTrace();
	    }

	    return messageList;
	}
	
	//채팅 전송 dao
	public Long sendMessage(ChatMessageDTO dto) {

	    String sql =
	        "INSERT INTO chat_message (room_id, sender_id, message_type, content) "
	      + "VALUES (?, ?, ?, ?)";

	    try (Connection con = JDBCUtil.jdbcCon();
	         PreparedStatement pstmt =
	             con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

	        pstmt.setLong(1, dto.getRoomId());
	        pstmt.setLong(2, dto.getSenderId());
	        pstmt.setString(3, dto.getMessageType());
	        pstmt.setString(4, dto.getContent());

	        int affectedRow = pstmt.executeUpdate();
	        if (affectedRow == 0) return null;

	        try (ResultSet rs = pstmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                return rs.getLong(1); // message_id
	            }
	        }

	    } catch (SQLException e) {
	        System.out.println("채팅 메시지 INSERT 중 에러!");
	        e.printStackTrace();
	    }
	    return null;
	}
}

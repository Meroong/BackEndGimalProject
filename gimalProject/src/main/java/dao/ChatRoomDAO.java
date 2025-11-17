package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.ChatRoomDTO;
import dto.ChatRoomUserDTO;
import dto.ResponseDTO;
import util.JDBCUtil;

public class ChatRoomDAO {

    
 
    //채팅방 목록 조회
    public ChatRoomDTO getChatRoomById(Long room_id){
    	ChatRoomDTO chatList = new ChatRoomDTO();
    	String sql = "select * from chat_room where room_id = ?;";
    	ResultSet rs = null;
    	ChatRoomDTO dto = null;
    	
    	//연결 및 쿼리
    	try (Connection con = JDBCUtil.jdbcCon();
    	         PreparedStatement pstmt = con.prepareStatement(sql))
    	{
			pstmt.setLong(1, room_id);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("채팅방 목록 조회 실패!");
			e.printStackTrace();
		}
    	try {
			while(rs.next()) {
				dto = new ChatRoomDTO();
				
                dto.setRoomId(room_id);
                dto.setRoomType(rs.getString("room_type"));
                dto.setItemId(rs.getLong("item_id"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("채팅방 목록 세팅 중 에러!");
			e.printStackTrace();
		}
    	return chatList;
    }
    
    // 채팅방 생성
    public int createChatRoom(ChatRoomDTO dto) {
    	String sql = "insert into chat_room(item_id, room_type, host_id) values(?, ?, ?);"; //거래 채팅을 넣느냐 마느냐? 현재는 item_id가 포함됨
    	
    	try {
    		PreparedStatement pstmt = JDBCUtil.jdbcCon().prepareStatement(sql);
    		pstmt.setLong(1, dto.getItemId());
    		pstmt.setString(2, dto.getRoomType());
    		pstmt.setLong(3, dto.getHostId());
    		
    		return pstmt.executeUpdate();
    	} catch (SQLException e) {
    		System.out.println("채팅방 생성 중 에러!");
    		e.printStackTrace();
    		return 0;
    	}
    }
    
    // 채팅방 삭제
    public int deleteChatRoom(long host_id, long room_id) {
    	String sql = "delete from chat_room where(host_id = ? and room_id = ?);";
    	
    	try {
    		PreparedStatement pstmt = JDBCUtil.jdbcCon().prepareStatement(sql);
    		pstmt.setLong(1, host_id);
    		pstmt.setLong(2, room_id);
    		
    		return pstmt.executeUpdate();
    	
    	} catch (SQLException e) {
    		System.out.println("채팅방 삭제 중 에러!");
    		e.printStackTrace();
    		return 0;
    	}
    	
    }
    
}

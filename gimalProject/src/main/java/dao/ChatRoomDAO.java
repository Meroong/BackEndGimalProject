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

    
 
	//특정 채팅방 정보 조회 - 유저가 채팅방 입장 시 사용 || 채팅방 목록 구할 때 사용
	public ChatRoomDTO getChatRoomInfo(Long room_id) {
	    String sql = "SELECT * FROM chat_room WHERE room_id = ?;";
	    
	    try (Connection con = JDBCUtil.jdbcCon();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {
	        
	        pstmt.setLong(1, room_id);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) { // 한 건만 조회
	                ChatRoomDTO dto = new ChatRoomDTO();
	                dto.setRoomId(rs.getLong("room_id"));
	                dto.setRoomType(rs.getString("room_type"));
	                dto.setItemId(rs.getLong("item_id"));
	                dto.setHostId(rs.getLong("host_id")); 
	                dto.setMeetingId(rs.getLong("meeting_id"));
	                return dto;
	            }
	        }
	        
	    } catch (SQLException e) {
	        System.out.println("채팅방 조회 중 에러!");
	        e.printStackTrace();
	    }
	    
	    return null; // 조회 실패 또는 데이터 없음
	}
	
    // 채팅방 생성
    public int createChatRoom(ChatRoomDTO dto) {
    	String sql;
    	boolean isItemRoom = dto.getItemId() != null;
    	
    	if(isItemRoom) {
    		sql = "insert into chat_room(item_id, room_type, host_id) values(?, ?, ?);";
    	}
    	else {
    		sql = "insert into chat_room(meeting_id, room_type, host_id) values (?, ?, ?);";
    	}
    	
    	try (Connection con = JDBCUtil.jdbcCon();
    			PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
    		
    		if(isItemRoom) {
	    		pstmt.setLong(1, dto.getItemId());
	    		pstmt.setString(2, dto.getRoomType());
	    		pstmt.setLong(3, dto.getHostId());
    		}
    		else {
        		pstmt.setLong(1, dto.getMeetingId());
        		pstmt.setString(2, dto.getRoomType());
        		pstmt.setLong(3, dto.getHostId());
    		}
        		int affectedRows = pstmt.executeUpdate();
        	
                if (affectedRows == 0) {
                    throw new SQLException("채팅방 생성 실패, 영향 받은 행이 없습니다.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // 생성된 room_id 반환
                    } else {
                        throw new SQLException("채팅방 생성 실패, 생성된 ID를 가져올 수 없습니다.");
                    }
                }
                }catch (SQLException e) {
		    		System.out.println("채팅방 생성 중 에러!");
		    		e.printStackTrace();
		    		return 0;
                }
                
}
    
    //채팅방 삭제 (일단 냅두기)
    public int deleteChatRoom(long host_id, long room_id) {
    	String sql = "delete from chat_room where host_id = ? and room_id = ?;";
    	
    	try(Connection con = JDBCUtil.jdbcCon();
    		PreparedStatement pstmt = con.prepareStatement(sql);){
    		
    		pstmt.setLong(1, host_id);
    		pstmt.setLong(2, room_id);
    		
    		return pstmt.executeUpdate();
    	
    	} catch (SQLException e) {
    		System.out.println("채팅방 삭제 중 에러!");
    		e.printStackTrace();
    		return 0;
    	}
    	
    }
    //채팅방 존재여부 확인 (개인용)
    public boolean isPrivateRoomExists(long item_id, long host_id, long receiver_id) {
    	String sql = """
    			SELECT cr.room_id
    			FROM chat_room cr
	            JOIN chat_room_user cru 
	            ON cr.room_id = cru.room_id
	            WHERE cr.item_id = ? AND cr.host_id = ?
	            GROUP BY cr.room_id
	            HAVING COUNT(*) = 2					
    			AND SUM(CASE WHEN cru.user_id IN (?, ?) THEN 1 ELSE 0 END) = 2
            """;
    	//그룹화 결과에 Having을 통해 조건 부여 
    	//1.참여자가 2인 경우의 조건 그리고 
    	//2. 참여자의 아이디가 호스트와 리시버에 속하는 경우 1로 간주 아니면 0으로 해서 합이 2인경우의 조건만 반환
    	try(Connection con = JDBCUtil.jdbcCon();
        		PreparedStatement pstmt = con.prepareStatement(sql);){
        		
        		pstmt.setLong(1, item_id);
        		pstmt.setLong(2, host_id);
        		pstmt.setLong(3, host_id);
        		pstmt.setLong(4, receiver_id);
        		
        		try(ResultSet rs = pstmt.executeQuery();){
        			return rs.next();
        		}
        	} catch (SQLException e) {
        		System.out.println("채팅방 삭제 중 에러!");
        		e.printStackTrace();
        		return false;
        	}
    }
    //채팅방 존재여부 확인 (그룹용)
	 public boolean isGroupRoomExist(long meetingId, long hostId) {
	        String sql = "SELECT room_id FROM chat_room WHERE host_id = ? AND meeting_id = ?";

	        try (Connection con = JDBCUtil.jdbcCon();
	             PreparedStatement pstmt = con.prepareStatement(sql)) {

	            pstmt.setLong(1, hostId);
	            pstmt.setLong(2, meetingId);

	            try (ResultSet rs = pstmt.executeQuery()) {
	                return rs.next(); // 이미 존재하면 true
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	 public boolean isHost(long roomId, long hostId) {
		 String sql = "select * from chat_room where room_id = ? and host_id = ?";
		 
	        try (Connection con = JDBCUtil.jdbcCon();
		             PreparedStatement pstmt = con.prepareStatement(sql)) {

		            pstmt.setLong(1, roomId);
		            pstmt.setLong(2, hostId);

		            try (ResultSet rs = pstmt.executeQuery()) {
		                return rs.next(); // 이미 존재하면 true
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		            return false;
		        }
	 }
	 // PRIVATE 채팅방 조회
	 public Integer findPrivateRoom(long itemId, long hostId, long receiverId) {
	     String sql = """
	         SELECT cr.id
	         FROM chat_room cr
	         JOIN chat_room_user u1 ON cr.id = u1.room_id
	         JOIN chat_room_user u2 ON cr.id = u2.room_id
	         WHERE cr.item_id = ?
	           AND cr.room_type = 'PRIVATE'
	           AND u1.user_id = ?
	           AND u2.user_id = ?
	     """;

	     try (Connection con = JDBCUtil.jdbcCon();
	          PreparedStatement ps = con.prepareStatement(sql)) {

	         ps.setLong(1, itemId);
	         ps.setLong(2, hostId);
	         ps.setLong(3, receiverId);

	         try (ResultSet rs = ps.executeQuery()) {
	             if (rs.next()) {
	                 return rs.getInt("id");
	             }
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	     return null;
	 }
}

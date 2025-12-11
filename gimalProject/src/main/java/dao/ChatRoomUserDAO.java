package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.ChatRoomUserDTO;
import util.JDBCUtil;

public class ChatRoomUserDAO {
	public ArrayList<Long> getRoomIdsByUser(long user_id){
		ArrayList<Long> idList = new ArrayList<Long>();
		String sql  = "select room_id from chat_room_user where user_id = ?;";
		
		try (Connection con = new JDBCUtil().jdbcCon();
			PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, user_id);
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					idList.add(rs.getLong("room_id"));
				}
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idList;
	}
	public ArrayList<Long> getUserInfo(long room_id){
		ArrayList<Long> userList = new ArrayList<Long>();
		String sql  = "select user_id from chat_room_user where room_id = ?;";

		try (Connection con = new JDBCUtil().jdbcCon();
				PreparedStatement pstmt = con.prepareStatement(sql);){
				pstmt.setLong(1, room_id);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						userList.add(rs.getLong("user_id"));
					}
				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return userList;
		}
	public int quitRoom(long userId, long roomId) {
		String sql = "delete from chat_room_user where user_id =? and room_id = ?; ";
        try (Connection con = JDBCUtil.jdbcCon();
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	            pstmt.setLong(1, userId);
	            pstmt.setLong(2, roomId);

	         
	            return pstmt.executeUpdate(); //affectedRow
	            

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return 0;
	        }
	}
	//유저가 방에 있는지 확인 
	public boolean isUserInRoom(long userId, long roomId) {
		 	String sql = "select * from chat_room_user where user_id = ? and room_id = ?;"; 
		 
	        try (Connection con = JDBCUtil.jdbcCon();
		         PreparedStatement pstmt = con.prepareStatement(sql)) {

		            pstmt.setLong(1, userId);
		            pstmt.setLong(2, roomId);

		            try (ResultSet rs = pstmt.executeQuery()) {
		                return rs.next(); // 이미 존재하면 true
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		            return false;
		        }
	 }
	//탈퇴 시 모든 채팅방 나가기
	public int quitRoomForDeleteUser(long autoId) {
		String sql = "delete from chat_room_user where user_id =?; ";
        try (Connection con = JDBCUtil.jdbcCon();
   	         PreparedStatement pstmt = con.prepareStatement(sql)) {

   	            pstmt.setLong(1, autoId);
   	            
   	            return pstmt.executeUpdate(); //affectedRow
   	    }catch (SQLException e) {
   	            e.printStackTrace();
   	            return 0;
   	    }
   	}
	//모임 참여시 혹은 방장 초대
	public boolean addUserToRoom(long userId, long roomId) {
        String sql = "INSERT INTO chat_room_user (room_id, user_id) VALUES (?, ?);";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, roomId);
            pstmt.setLong(2, userId);
            return pstmt.executeUpdate() >0 ;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	

}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.JDBCUtil;

public class ChatRoomUserDAO {
	public ArrayList<Long> getRoomIdsByUser(long user_id){
		ArrayList<Long> idList = new ArrayList<Long>();
		String sql  = "select room_id from chat_room_user where user_id = ?;";
		ResultSet rs = null;
		
		try (Connection con = new JDBCUtil().jdbcCon();
			PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, user_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				idList.add(rs.getLong("room_id"));
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return idList;
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
	
	public int addUserToRoom(long userId, long roomId) {
        String sql = "INSERT INTO chat_room_user (room_id, user_id) VALUES (?, ?);";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, roomId);
            pstmt.setLong(2, userId);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
	

}

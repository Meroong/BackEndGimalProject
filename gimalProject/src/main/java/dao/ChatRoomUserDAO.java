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

}

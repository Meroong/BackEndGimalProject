package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import dto.MeetingDTO;
import dto.UserDTO;
import util.JDBCUtil;

public class MeetingDAO {
	
   // private int meetingId;         // 모임 ID (PK)
   // private String title;          // 모임 제목
   // private String content;        // 모임 설명
   // private Timestamp date;        // 모임 날짜
   // private String location;       // 장소
   // private int maxMembers;        // 최대 인원
   // private int currentMembers;    // 현재 인원
   // private int cost;              // 참가비
   // private String tag;            // 태그 (단순 텍스트)
   //private String status;         // 상태 (모집중 / 마감 등)
    
	
	//게시글 전체조회
	public ArrayList<MeetingDTO> searchAll(){
		ArrayList<MeetingDTO> aList = new ArrayList<MeetingDTO>();
		String sql = "select * from meeting;";
		
		try (Connection con = JDBCUtil.jdbcCon()) {
            if (con == null) throw new RuntimeException("DB 연결 실패");
            
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                
            }
            catch(SQLException e) {
            	System.out.println("sql 쿼리 오류");
            	e.printStackTrace();
            }
		for(MeetingDTO dto : )
			
			return aList;
		}
		catch(){
			
		}
		
		
	}
 // 게시글 업데이트
    public void updateMeet(MeetingDTO dto) {
        StringBuilder sql = new StringBuilder("UPDATE meeting SET ");
        int fieldCount = 0;

        try (Connection con = JDBCUtil.jdbcCon()) {
            if (con == null) throw new RuntimeException("DB 연결 실패");

            if (dto.getTitle() != null) { sql.append("title = ?, "); fieldCount++; }
            if (dto.getContent() != null) { sql.append("content = ?, "); fieldCount++; }
            if (dto.getDate() != null) { sql.append("date = ?, "); fieldCount++; }
            if (dto.getLocation() != null) { sql.append("location = ?, "); fieldCount++; }
            if (dto.getMaxMembers() != 0) { sql.append("max_members = ?, "); fieldCount++; }
            if (dto.getCurrentMembers() != 0) { sql.append("current_members = ?, "); fieldCount++; }
            if (dto.getCost() != 0) { sql.append("cost = ?, "); fieldCount++; }
            if (dto.getTag() != null) { sql.append("tag = ?, "); fieldCount++; }   //일단 통 텍스트로 태그 테이블 구상해야함
            if (dto.getStatus() != null) { sql.append("status = ?, "); fieldCount++; }
            

            if (fieldCount == 0) return; // 업데이트할 필드가 없으면 종료

            // 마지막 콤마 제거
            sql.setLength(sql.length() - 2);
            sql.append(" WHERE id = ?");

            try (PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
                int index = 1;

                if (dto.getTitle() != null) pstmt.setString(index++, dto.getTitle());
                if (dto.getContent() != null) pstmt.setString(index++, dto.getContent());
                if (dto.getDate() != null) pstmt.setTimestamp(index++, new java.sql.Timestamp(dto.getDate().getTime()));
                if (dto.getLocation() != null) pstmt.setString(index++, dto.getLocation());
                if (dto.getMaxMembers() != 0) pstmt.setInt(index++, dto.getMaxMembers());
                if (dto.getCurrentMembers() != 0) pstmt.setInt(index++, dto.getCurrentMembers());
                if (dto.getCost() != 0) pstmt.setInt(index++, dto.getCost());
                if (dto.getTag() != null) pstmt.setString(index++, dto.getTag());
                if (dto.getStatus() != null) pstmt.setString(index++, dto.getStatus());

                // WHERE 조건
                pstmt.setInt(index, dto.getMeetingId());

                pstmt.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
    }

    // 게시글 작성
    public boolean insert(MeetingDTO dto) {
        String sql = "INSERT INTO meeting (title, content, date, location, max_members, current_members, cost, tag, status\r\n"
        		+ ") "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setTimestamp(3, dto.getDate());
            pstmt.setString(4, dto.getLocation());
            pstmt.setInt(5, dto.getMaxMembers());
            pstmt.setInt(6, dto.getCurrentMembers());
            pstmt.setInt(7, dto.getCost());
            pstmt.setString(8, dto.getTag());
            pstmt.setString(9, dto.getStatus());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
        }
        return false;
    }

    // 제목으로 검색
    public MeetingDTO searchByTitle(String userId, String userPassword) {
        
        return null;
    }

    // 내용으로 검색
    public MeetingDTO searchByContent(int autoId) {
       
        return null;
    }

    // 게시글 삭제
    public void delete(int autoId) {
    
    }
}

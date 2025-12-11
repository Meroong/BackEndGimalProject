package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import dto.MeetingDTO;
import dto.MeetingInfoDTO;
import util.JDBCUtil;

public class MeetingDAO {

    //게시판 목록을 위한 조회
    public ArrayList<MeetingInfoDTO> getPostList(){
        //모임 아이디 제목 날짜 상태 태그
        ArrayList<MeetingInfoDTO> aList = new ArrayList<MeetingInfoDTO>();
        String sql = """
        	    SELECT 
        	        m.id,
        	        m.title,
        	        m.content,
        	        m.date,
        	        m.location_id,
        	        m.max_members,
        	        m.current_members,
        	        m.tag,
        	        m.status,
        	        m.created_at,

        	        l.road_address

        	    FROM meeting m
        	    JOIN meeting_location l ON m.location_id = l.id
        	""";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                MeetingInfoDTO dto = new MeetingInfoDTO();
                dto.setMeetingId(rs.getLong("id"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setDate(rs.getTimestamp("date"));
                dto.setLocationId(rs.getLong("location_id"));
                dto.setMaxMembers(rs.getInt("max_members"));
                dto.setCurrentMembers(rs.getInt("current_members"));
                dto.setTag(rs.getString("tag"));
                dto.setStatus(rs.getString("status"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setRoadAddress(rs.getString("road_address"));
                aList.add(dto);
            }
        } catch(SQLException e) {
            System.out.println("sql 쿼리 오류");
            e.printStackTrace();
        }
        return aList;
    }

    //게시글 상세조회 (모든 필드)
    public MeetingDTO getPostDetail(long meetingId){
        MeetingDTO dto = new MeetingDTO();
        String sql = "select * from meeting where id = ?;";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId); // <-- meetingId 세팅
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    dto.setMeetingId(rs.getLong("id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setDate(rs.getTimestamp("date"));
                    dto.setLocationId(rs.getLong("location_id"));
                    dto.setMaxMembers(rs.getInt("max_members"));
                    dto.setCurrentMembers(rs.getInt("current_members"));
                    dto.setCost(rs.getInt("cost"));
                    dto.setTag(rs.getString("tag"));
                    dto.setStatus(rs.getString("status"));
                    dto.setWeather(rs.getString("weather")); //날씨 정보
                    dto.setCreatorId(rs.getLong("creator_id")); // 게시자 ID
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    dto.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch(SQLException e) {
            System.out.println("sql 쿼리 오류");
            e.printStackTrace();
        }
        return dto;
    }

    // 게시글 업데이트
    public boolean updateMeet(MeetingDTO dto) {
        StringBuilder sql = new StringBuilder("UPDATE meeting SET ");
        int fieldCount = 0;

        try (Connection con = JDBCUtil.jdbcCon()) {
            if (con == null) throw new RuntimeException("DB 연결 실패");

            if (dto.getTitle() != null) { sql.append("title = ?, "); fieldCount++; }
            if (dto.getContent() != null) { sql.append("content = ?, "); fieldCount++; }
            if (dto.getDate() != null) { sql.append("date = ?, "); fieldCount++; }
            if (dto.getLocationId() != 0) { sql.append("location_id = ?, "); fieldCount++; }
            if (dto.getMaxMembers() != 0) { sql.append("max_members = ?, "); fieldCount++; }
            if (dto.getCurrentMembers() != 0) { sql.append("current_members = ?, "); fieldCount++; }
            if (dto.getCost() != 0) { sql.append("cost = ?, "); fieldCount++; }
            if (dto.getTag() != null) { sql.append("tag = ?, "); fieldCount++; }
            if (dto.getStatus() != null) { sql.append("status = ?, "); fieldCount++; }
            if (dto.getWeather() != null) { sql.append("weather = ?, "); fieldCount++; }
            if (dto.getCreatorId() != 0) { sql.append("creator_id = ?, "); fieldCount++; }

            if (fieldCount == 0) {
                System.out.println("업데이트할 필드가 없음");
                return false;
            }

            // 마지막 콤마 제거
            sql.setLength(sql.length() - 2);
            sql.append(" WHERE id = ? and creator_id = ?");

            try (PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
                int index = 1;

                if (dto.getTitle() != null) pstmt.setString(index++, dto.getTitle());
                if (dto.getContent() != null) pstmt.setString(index++, dto.getContent());
                if (dto.getDate() != null) pstmt.setTimestamp(index++, new java.sql.Timestamp(dto.getDate().getTime()));
                if (dto.getLocationId() != 0) pstmt.setLong(index++, dto.getLocationId());
                if (dto.getMaxMembers() != 0) pstmt.setInt(index++, dto.getMaxMembers());
                if (dto.getCurrentMembers() != 0) pstmt.setInt(index++, dto.getCurrentMembers());
                if (dto.getCost() != 0) pstmt.setInt(index++, dto.getCost());
                if (dto.getTag() != null) pstmt.setString(index++, dto.getTag());
                if (dto.getStatus() != null) pstmt.setString(index++, dto.getStatus());
                if (dto.getWeather() != null) pstmt.setString(index++, dto.getWeather());
                if (dto.getCreatorId() != 0) pstmt.setLong(index++, dto.getCreatorId());

                pstmt.setLong(index++, dto.getMeetingId());
                pstmt.setLong(index++, dto.getCreatorId());

                int rs = pstmt.executeUpdate();
                return rs>0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 혹은 쿼리오류");
            return false;
        }
    }

    // 게시글 작성 (생성된 meeting_id 반환)
    public long insert(MeetingDTO dto) {
        String sql = "INSERT INTO meeting (title, content, date, location_id, max_members, current_members, cost, tag, status, weather, creator_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setTimestamp(3, dto.getDate());
            pstmt.setLong(4, dto.getLocationId());
            pstmt.setInt(5, dto.getMaxMembers());
            pstmt.setInt(6, dto.getCurrentMembers());
            pstmt.setInt(7, dto.getCost());
            pstmt.setString(8, dto.getTag());
            pstmt.setString(9, dto.getStatus());
            pstmt.setString(10, dto.getWeather());
            pstmt.setLong(11, dto.getCreatorId()); // 게시자 ID

            int result = pstmt.executeUpdate();

            if (result == 0) {
                return -1;  // INSERT 실패
            }

            // 생성된 PK 가져오기
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1); // 생성된 meeting_id
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 오류 또는 쿼리 오류");
        }

        return -1;
    }

    // 제목으로 검색
    public MeetingDTO searchByTitle(String title) {
        // 구현 가능
        return null;
    }

    // 내용으로 검색
    public MeetingDTO searchByContent(String content) {
        // 구현 가능
        return null;
    }

    // 게시글 삭제
    public void delete(long meetingId, long creator_id) {
        String sql = "DELETE FROM meeting WHERE id = ? and creator_id = ?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, meetingId);
            pstmt.setLong(2, creator_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean increaseCurrentMembers(long meetingId) {
        String sql = "UPDATE meeting SET current_members = current_members + 1 WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean decreaseCurrentMembers(long meetingId) {
        String sql = "UPDATE meeting " +
                     "SET current_members = CASE WHEN current_members > 0 THEN current_members - 1 ELSE 0 END " +
                     "WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 회비 전용 조회 메서드 (채팅/결제용)
    public Integer getMeetingCostByMeetingId(long meetingId) {
        String sql = "SELECT cost FROM meeting WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cost");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // 모임 없을 때
    }
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dto.MeetingDTO;
import dto.MeetingInfoDTO;
import util.JDBCUtil;

public class MeetingDAO {

    //게시판 목록을 위한 조회
    /**
     * 필터 없이 전체 목록 조회 (기존 사용 메서드)
     */
    public ArrayList<MeetingInfoDTO> getPostList() {
        // 내부에서 필터 공통 메서드 호출 (필터 없음)
        return getPostListFiltered(null, null, null, null, null, null);
    }

    /**
     * ✅ 필터 기능이 붙은 목록 조회 메서드
     *
     * @param category  카테고리(산책/헬스/애견 등) -> tag 컬럼 LIKE 검색
     * @param dateStr   yyyy-MM-dd 형식 날짜
     * @param keyword   제목/내용 검색어
     * @param status    모임 상태 (OPEN, CLOSED, COMPLETED) / null 또는 "ALL" 이면 전체
     * @param weather   날씨 (맑음, 흐림, 비, 이슬비, 천둥번개, 눈, 기타) / null 또는 "ALL" 이면 전체
     */
    public ArrayList<MeetingInfoDTO> getPostListFiltered(String category,
    													String dateFrom,
    													String dateTo,
    													String keyword,
    													String status,
    													String weather) {


        ArrayList<MeetingInfoDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("""
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
                    m.view_count,
                    m.weather,
                    m.created_at,
                    l.road_address,
                    l.jibun_address
                FROM meeting m
                JOIN meeting_location l ON m.location_id = l.id
                WHERE 1 = 1
                """);

        // 동적 파라미터
        List<Object> params = new ArrayList<>();

        // 1) 카테고리 필터 (tag 컬럼 LIKE 검색)
        if (category != null && !category.isBlank() && !"전체".equals(category)) {
            sql.append(" AND m.tag LIKE ? ");
            params.add("%" + category + "%");
        }

        
        // 2) 날짜 범위 필터 (yyyy-MM-dd ~ yyyy-MM-dd)
        // dateFrom, dateTo 둘 다 있으면 BETWEEN, 하나만 있으면 >= 또는 <=
        boolean hasFrom = (dateFrom != null && !dateFrom.isBlank());
        boolean hasTo   = (dateTo   != null && !dateTo.isBlank());

        if (hasFrom && hasTo) {
            sql.append(" AND DATE(m.date) BETWEEN ? AND ? ");
            params.add(java.sql.Date.valueOf(dateFrom));
            params.add(java.sql.Date.valueOf(dateTo));
        } else if (hasFrom) {
            sql.append(" AND DATE(m.date) >= ? ");
            params.add(java.sql.Date.valueOf(dateFrom));
        } else if (hasTo) {
            sql.append(" AND DATE(m.date) <= ? ");
            params.add(java.sql.Date.valueOf(dateTo));
        }


        // 3) 키워드 필터 (제목 + 내용)
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (m.title LIKE ? OR m.content LIKE ?) ");
            String like = "%" + keyword + "%";
            params.add(like);
            params.add(like);
        }

        // 4) 모집 상태 필터 (OPEN / CLOSED / COMPLETED)
        if (status != null && !status.isBlank() && !"ALL".equals(status)) {
            sql.append(" AND m.status = ? ");
            params.add(status);
        }

        // 5) 날씨 필터 (맑음/흐림/비/이슬비/천둥번개/눈/기타)
        if (weather != null && !weather.isBlank() && !"ALL".equals(weather)) {
            sql.append(" AND m.weather = ? ");
            params.add(weather);
        }

        // 정렬: 가까운 모임 날짜 순으로
        sql.append(" ORDER BY m.date ASC ");

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
            int idx = 1;
            for (Object p : params) {
                if (p instanceof java.sql.Date) {
                    pstmt.setDate(idx++, (java.sql.Date) p);
                } else {
                    pstmt.setObject(idx++, p);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
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
                    dto.setViewCount(rs.getInt("view_count"));
                    dto.setWeather(rs.getString("weather"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    dto.setRoadAddress(rs.getString("road_address"));
                    dto.setJibunAddress(rs.getString("jibun_address"));
                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            System.out.println("sql 쿼리 오류");
            e.printStackTrace();
        }

        return list;
    }

    //게시글 상세조회 (모든 필드)
    public MeetingDTO getPostDetail(long meetingId){
        MeetingDTO dto = new MeetingDTO();
        String sql = "select * from meeting where id = ?;";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);
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
                    dto.setViewCount(rs.getInt("view_count"));
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
        String sql = "INSERT INTO meeting (title, content, date, location_id, max_members, current_members, cost, tag, status, view_count, weather, creator_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            pstmt.setInt(10, 0);
            pstmt.setString(11, dto.getWeather());
            pstmt.setLong(12, dto.getCreatorId()); // 게시자 ID

            int result = pstmt.executeUpdate();

            if (result == 0) {
                return -1;
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 연결 오류 또는 쿼리 오류");
        }

        return -1;
    }

    public MeetingDTO searchByTitle(String title) {
        return null;
    }

    public MeetingDTO searchByContent(String content) {
        return null;
    }

    // 게시글 삭제
    public boolean delete(long meetingId, long creator_id) {
        String sql = "DELETE FROM meeting WHERE id = ? and creator_id = ?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, meetingId);
            pstmt.setLong(2, creator_id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

        return null;
    }
    public boolean increaseViewCount(long meetingId) {
        String sql = "UPDATE meeting SET view_count = view_count + 1 WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // meetingId로 location_id 조회
    public Long getLocationIdByMeetingId(long meetingId) {
        String sql = "SELECT location_id FROM meeting WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, meetingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("location_id");
                }
            }

        } catch (SQLException e) {
            System.out.println("location_id 조회 중 SQL 오류");
            e.printStackTrace();
        }

        return null; // 조회 실패 또는 존재하지 않는 meeting
    }
    // 모임 상태 변경
    public boolean updateStatus(long meetingId, String status) {
        String sql = "UPDATE meeting SET status = ? WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setLong(2, meetingId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 날짜 지난 모임 자동 마감
    public int closeExpiredMeetings() {
        String sql = """
            UPDATE meeting
            SET status = 'CLOSED'
            WHERE status = 'OPEN'
              AND date < NOW()
        """;

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            return pstmt.executeUpdate(); // 몇 건 바뀌었는지
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public List<MeetingInfoDTO> findActiveMeetingsForMap() {

        String sql = """
            SELECT 
                m.id,
                m.title,
                l.latitude,
                l.longitude,
                m.meeting_time AS date
            FROM meeting m
            JOIN meeting_location l ON m.location_id = l.id
            WHERE m.status = 'OPEN'
              AND m.meeting_time > NOW()
              AND l.latitude IS NOT NULL
              AND l.longitude IS NOT NULL
        """;

        List<MeetingInfoDTO> list = new ArrayList<>();

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MeetingInfoDTO dto = new MeetingInfoDTO();
                dto.setMeetingId(rs.getLong("id"));
                dto.setTitle(rs.getString("title"));
                dto.setLatitude(rs.getDouble("latitude"));
                dto.setLongitude(rs.getDouble("longitude"));
                dto.setDate(rs.getTimestamp("date")); 
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}

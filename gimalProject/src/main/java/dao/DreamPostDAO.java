package dao;

import dto.DreamPostDTO;
import dto.DreamSearchCondition;
import dto.FileResourceDTO;
import util.JDBCUtil;
import util.TimeAgoUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DreamPostDAO {

    // 드림 게시글 등록
    public Long insert(DreamPostDTO dto) throws SQLException {

        String sql = "INSERT INTO dream_post (" +
                "writer_id, writer_type, title, content, " +
                "category_code, condition_code, price, dong, " +
                "status" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, dto.getWriterId());
            pstmt.setString(2, dto.getWriterType());
            pstmt.setString(3, dto.getTitle());
            pstmt.setString(4, dto.getContent());
            pstmt.setString(5, dto.getCategoryCode());
            pstmt.setString(6, dto.getConditionCode());
            pstmt.setInt(7, dto.getPrice());
            pstmt.setString(8, dto.getDong());
            pstmt.setString(9, dto.getStatus());   // 기본 OPEN

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                return null;
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    dto.setDreamId(id);
                    return id;
                }
            }
        }

        return null;
    }
    
    public List<DreamPostDTO> findDreamPosts(DreamSearchCondition cond) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DreamPostDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dream_id, writer_id, title, content, category_code, ");
        sql.append("       dong, ");
        sql.append("       condition_code, status, created_at, updated_at, view_count ");
        sql.append("  FROM dream_post ");
        sql.append(" WHERE is_deleted = 0 ");

        List<Object> params = new ArrayList<>();

        if (cond != null) {
            if (cond.getDong() != null && !cond.getDong().isEmpty()) {
                sql.append("   AND dong = ? ");
                params.add(cond.getDong());
            }

            if (cond.isExcludeDone()) {
                // CLOSE(나눔완료) 제외
                sql.append("   AND status <> 'CLOSE' ");
            }

            if (cond.isNewOnly()) {
                // 새상품만 보기 = NEW 상태만
                sql.append("   AND condition_code = '새거' ");
            }

            if (cond.getCategoryCode() != null && !cond.getCategoryCode().isEmpty()) {
                sql.append("   AND category_code = ? ");
                params.add(cond.getCategoryCode());
            }

            if (cond.getConditionCodes() != null && !cond.getConditionCodes().isEmpty()) {
                sql.append("   AND condition_code IN (");
                for (int i = 0; i < cond.getConditionCodes().size(); i++) {
                    if (i > 0) {
                        sql.append(", ");
                    }
                    sql.append("?");
                    params.add(cond.getConditionCodes().get(i));
                }
                sql.append(") ");
            }

            if (cond.getKeyword() != null && !cond.getKeyword().isEmpty()) {
                sql.append("   AND (title LIKE ? OR content LIKE ?) ");
                String like = "%" + cond.getKeyword() + "%";
                params.add(like);
                params.add(like);
            }
        }

        String sort = (cond != null && cond.getSort() != null && !cond.getSort().isEmpty())
                ? cond.getSort()
                : "LATEST";

        if ("OLDEST".equalsIgnoreCase(sort)) {
            sql.append(" ORDER BY created_at ASC ");   // 과거순
        } else {
            sql.append(" ORDER BY created_at DESC ");  // 최신순(기본)
        }

        sql.append(" LIMIT 40 ");
        
        try {
            conn = JDBCUtil.jdbcCon();
            pstmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                DreamPostDTO dto = mapRow(rs);
                list.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, rs, pstmt);
        }

        return list;
    }
    
    private DreamPostDTO mapRow(ResultSet rs) throws SQLException {
        DreamPostDTO dto = new DreamPostDTO();
        dto.setDreamId(rs.getLong("dream_id"));
        dto.setWriterId(rs.getLong("writer_id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setCategoryCode(rs.getString("category_code"));
        dto.setDong(rs.getString("dong"));
        dto.setConditionCode(rs.getString("condition_code"));
        dto.setStatus(rs.getString("status"));
        dto.setViewCount(rs.getInt("view_count"));
        dto.setCreatedAt(rs.getTimestamp("created_at"));
        dto.setUpdatedAt(rs.getTimestamp("updated_at"));

        return dto;
    }

    
    public List<DreamPostDTO> getDreamPostList(DreamSearchCondition cond) {

        List<DreamPostDTO> list = findDreamPosts(cond);
        List<DreamPostDTO> result = new ArrayList<>();

        FileResourceDAO fDao = new FileResourceDAO();
        for (DreamPostDTO dto : list) {
            dto.setTimeAgoLabel(TimeAgoUtil.format(dto.getCreatedAt()));
//            System.out.println("[DEBUG]"+fDao.getPostUrls(dto.getWriterId(), dto.getDreamId(), "POST"));
            dto.setImagesUrl(fDao.getPostUrls(dto.getWriterId(), dto.getDreamId(), "POST"));
            result.add(dto);
        }

        return result;
    }


 // 단일 게시글 조회 (이미지 포함)
    public DreamPostDTO findByIdWithImages(long dreamId) {
        String sql = "SELECT dream_id, writer_id, writer_type, title, content, " +
                     "       category_code, condition_code, price, dong, status, " +
                     "       view_count, created_at, updated_at " +
                     "  FROM dream_post " +
                     " WHERE is_deleted = 0 " +
                     "   AND dream_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.jdbcCon();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, dreamId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                DreamPostDTO dto = mapRow(rs);

                // 시간 라벨
                dto.setTimeAgoLabel(TimeAgoUtil.format(dto.getCreatedAt()));

                // 이미지 URL
                FileResourceDAO fDao = new FileResourceDAO();
                dto.setImagesUrl(fDao.getPostUrls(dto.getWriterId(), dto.getDreamId(), "POST"));

                return dto;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, rs, pstmt);
        }

        return null;
    }

    // 조회수 증가 (상세 페이지 진입 시 사용 선택)
    public void increaseViewCount(long dreamId) {
        String sql = "UPDATE dream_post SET view_count = view_count + 1 " +
                     " WHERE dream_id = ? AND is_deleted = 0";

        try (Connection conn = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dreamId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 드림 상태(OPEN / CLOSE)만 변경
    public boolean updateStatus(long dreamId, long writerId, String status) {
        // 허용 상태만 필터링
        if (!"OPEN".equals(status) && !"CLOSE".equals(status)) {
            return false;
        }

        String sql = "UPDATE dream_post " +
                     "   SET status = ?, " +
                     "       updated_at = NOW() " +
                     " WHERE dream_id = ? " +
                     "   AND writer_id = ? " +
                     "   AND is_deleted = 0";

        try (Connection conn = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setLong(2, dreamId);
            pstmt.setLong(3, writerId);

            int updated = pstmt.executeUpdate();
            return updated > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // 게시글 수정
    public boolean updatePost(DreamPostDTO dto, long writerId) {
        String sql = "UPDATE dream_post " +
                     "   SET title = ?, " +
                     "       content = ?, " +
                     "       category_code = ?, " +
                     "       condition_code = ?, " +
                     "       dong = ?, " +
                     "       updated_at = NOW() " +
                     " WHERE dream_id = ? " +
                     "   AND writer_id = ? " +
                     "   AND is_deleted = 0";

        try (Connection conn = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setString(3, dto.getCategoryCode());
            pstmt.setString(4, dto.getConditionCode());
            pstmt.setString(5, dto.getDong());
            pstmt.setLong(6, dto.getDreamId());
            pstmt.setLong(7, writerId);

            int updated = pstmt.executeUpdate();
            return updated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 소프트 삭제 (is_deleted = 1 + 상태 CLOSE)
    public boolean softDeletePost(long dreamId, long writerId) {
        String sql = "UPDATE dream_post " +
                     "   SET is_deleted = 1, " +
                     "       status = 'CLOSE', " +
                     "       updated_at = NOW() " +
                     " WHERE dream_id = ? " +
                     "   AND writer_id = ? " +
                     "   AND is_deleted = 0";

        try (Connection conn = JDBCUtil.jdbcCon();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, dreamId);
            pstmt.setLong(2, writerId);

            int updated = pstmt.executeUpdate();
            return updated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

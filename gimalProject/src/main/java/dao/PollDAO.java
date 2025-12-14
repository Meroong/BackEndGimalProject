package dao;

import dto.*;
import util.JDBCUtil;

import java.sql.*;
import java.util.*;

public class PollDAO {

    public long insertPoll(PollDTO poll) throws Exception {
        String sql = "INSERT INTO poll (room_id, title, expire_at) VALUES (?, ?, ?)";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, poll.getRoomId());
            ps.setString(2, poll.getTitle());
            ps.setTimestamp(3, poll.getExpireAt());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        }
    }

    public void insertOption(PollOptionDTO opt) throws Exception {
        String sql = "INSERT INTO poll_option (poll_id, option_text) VALUES (?, ?)";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, opt.getPollId());
            ps.setString(2, opt.getOptionText());
            ps.executeUpdate();
        }
    }

    public List<PollDTO> getPollList(long roomId) throws Exception {
        String sql = "SELECT * FROM poll WHERE room_id = ? ORDER BY created_at ASC";
        List<PollDTO> list = new ArrayList<>();

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, roomId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PollDTO p = new PollDTO();
                p.setId(rs.getLong("id"));
                p.setRoomId(rs.getLong("room_id"));
                p.setTitle(rs.getString("title"));
                p.setExpireAt(rs.getTimestamp("expire_at"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                Timestamp expireAt = rs.getTimestamp("expire_at");
                p.setExpireAt(expireAt);

                boolean closed = false;
                if (expireAt != null) {
                    closed = expireAt.before(new Timestamp(System.currentTimeMillis()));
                }
                p.setClosed(closed);
                list.add(p);
            }
        }
        return list;
    }

    public List<PollOptionDTO> getOptions(long pollId) throws Exception {
        String sql = """
            SELECT o.*, COUNT(v.user_id) AS cnt
            FROM poll_option o
            LEFT JOIN poll_vote v ON o.id = v.option_id
            WHERE o.poll_id = ?
            GROUP BY o.id
        """;

        List<PollOptionDTO> list = new ArrayList<>();
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, pollId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PollOptionDTO o = new PollOptionDTO();
                o.setId(rs.getLong("id"));
                o.setPollId(rs.getLong("poll_id"));
                o.setOptionText(rs.getString("option_text"));
                o.setVoteCount(rs.getInt("cnt"));
                list.add(o);
            }
        }
        return list;
    }

    public boolean hasVoted(long pollId, long userId) throws Exception {
        String sql = "SELECT 1 FROM poll_vote WHERE poll_id=? AND user_id=?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, pollId);
            ps.setLong(2, userId);
            return ps.executeQuery().next();
        }
    }

    public void insertVote(PollVoteDTO v) throws Exception {
        String sql = "INSERT INTO poll_vote (poll_id, user_id, option_id) VALUES (?, ?, ?)";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, v.getPollId());
            ps.setLong(2, v.getUserId());
            ps.setLong(3, v.getOptionId());
            ps.executeUpdate();
        }
    }
    public PollDTO getPoll(long pollId) throws Exception {
        String sql = "SELECT * FROM poll WHERE id = ?";

        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, pollId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PollDTO p = new PollDTO();
                p.setId(rs.getLong("id"));
                p.setRoomId(rs.getLong("room_id"));
                p.setTitle(rs.getString("title"));

                Timestamp expireAt = rs.getTimestamp("expire_at");
                p.setExpireAt(expireAt);
                p.setCreatedAt(rs.getTimestamp("created_at"));

                boolean closed = false;
                if (expireAt != null) {
                    closed = expireAt.before(new Timestamp(System.currentTimeMillis()));
                }
                p.setClosed(closed);

                return p;
            }
        }
        return null;
    }
    public void closePoll(long pollId) throws Exception {
        String sql = "UPDATE poll SET expire_at = NOW() WHERE id=?";
        try (Connection con = JDBCUtil.jdbcCon();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, pollId);
            ps.executeUpdate();
        }
    }
    public void deletePoll(long pollId) throws Exception {
        Connection con = null;

        try {
            con = JDBCUtil.jdbcCon();
            con.setAutoCommit(false);

            try (PreparedStatement ps1 =
                     con.prepareStatement("DELETE FROM poll_vote WHERE poll_id = ?")) {
                ps1.setLong(1, pollId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 =
                     con.prepareStatement("DELETE FROM poll_option WHERE poll_id = ?")) {
                ps2.setLong(1, pollId);
                ps2.executeUpdate();
            }

            try (PreparedStatement ps3 =
                     con.prepareStatement("DELETE FROM poll WHERE id = ?")) {
                ps3.setLong(1, pollId);
                ps3.executeUpdate();
            }

            con.commit();
        } catch (Exception e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) con.setAutoCommit(true);
        }
    }

}

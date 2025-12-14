package service;

import dao.PollDAO;
import dto.*;

import java.sql.Timestamp;
import java.util.*;

public class PollService {

    private PollDAO pollDAO = new PollDAO();

    public void createPoll(long roomId, String title, Timestamp expireAt, String[] options) throws Exception {
        PollDTO poll = new PollDTO();
        poll.setRoomId(roomId);
        poll.setTitle(title);
        poll.setExpireAt(expireAt);

        long pollId = pollDAO.insertPoll(poll);

        for (String opt : options) {
            PollOptionDTO o = new PollOptionDTO();
            o.setPollId(pollId);
            o.setOptionText(opt);
            pollDAO.insertOption(o);
        }
    }


    public List<PollDTO> getPollListByRoom(long roomId) throws Exception {
        List<PollDTO> list = pollDAO.getPollList(roomId);
        for (PollDTO p : list) {
            p.setOptions(pollDAO.getOptions(p.getId()));
        }
        return list;
    }

    public boolean submitVote(long pollId, long userId, long optionId) throws Exception {
        PollDTO poll = pollDAO.getPoll(pollId);

        // ⛔ 마감된 투표
        if (poll.isClosed()) return false;

        // ⛔ 기간 만료
        if (poll.getExpireAt() != null &&
            poll.getExpireAt().before(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }

        if (pollDAO.hasVoted(pollId, userId)) return false;

        PollVoteDTO v = new PollVoteDTO();
        v.setPollId(pollId);
        v.setUserId(userId);
        v.setOptionId(optionId);

        pollDAO.insertVote(v);
        return true;
    }

    // 👑 호스트용
    public void closePoll(long pollId) throws Exception {
        pollDAO.closePoll(pollId);
    }

    public void deletePoll(long pollId, long userId, long hostId) throws Exception {
        if (userId != hostId) return;
        pollDAO.deletePoll(pollId);
    }
}

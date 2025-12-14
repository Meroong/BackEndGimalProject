package controller;

import java.sql.Timestamp;

import dto.UserDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.PollService;

@WebServlet("/vote/*")
public class PollController extends HttpServlet {

    private PollService pollService = new PollService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws java.io.IOException {

        String path = req.getPathInfo();
        HttpSession session = req.getSession();

        UserDTO user = (UserDTO) session.getAttribute("userInfo");
        if (user == null) {
            resp.sendError(401);
            return;
        }

        long userId = user.getAutoId();

        try {
            // 🔹 투표 생성
            if ("/create".equals(path)) {

                long roomId = Long.parseLong(req.getParameter("roomId"));
                String title = req.getParameter("title");

                Timestamp end = Timestamp.valueOf(
                        req.getParameter("endTime").replace("T", " ") + ":00"
                );

                String[] options = {
                        req.getParameter("opt1"),
                        req.getParameter("opt2")
                };

                pollService.createPoll(roomId, title, end, options);
                resp.sendRedirect(req.getHeader("Referer"));
            }

            // 🔹 투표 제출
            else if ("/submit".equals(path)) {

                long pollId = Long.parseLong(req.getParameter("voteId"));
                long optionId = Long.parseLong(req.getParameter("optionId"));

                pollService.submitVote(pollId, userId, optionId);
                resp.sendRedirect(req.getHeader("Referer"));
            }

            // 🔹 투표 삭제 (호스트 전용)
            else if ("/delete".equals(path)) {

                long pollId = Long.parseLong(req.getParameter("voteId"));
                long hostId = Long.parseLong(req.getParameter("hostId"));
                // 👉 hostId는 JSP hidden input으로 넘기는 게 현실적

                pollService.deletePoll(pollId, userId, hostId);
                resp.sendRedirect(req.getHeader("Referer"));
            }
            else if ("/close".equals(path)) {

                long pollId = Long.parseLong(req.getParameter("voteId"));
                long hostId = Long.parseLong(req.getParameter("hostId"));

                // 🔐 호스트만 가능
                if (userId != hostId) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                pollService.closePoll(pollId);
                resp.sendRedirect(req.getHeader("Referer"));
            }

            else {
                resp.sendError(404);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}

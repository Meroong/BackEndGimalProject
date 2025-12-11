package controller;
import java.sql.Timestamp;

import dto.UserDTO;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.PollService;

@WebServlet("/vote/*")
public class PollController extends HttpServlet {

    private PollService pollService = new PollService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, java.io.IOException {
        String path = req.getPathInfo();
        HttpSession session = req.getSession();
        long userId = ((UserDTO)session.getAttribute("userInfo")).getAutoId();

        try {
            if ("/create".equals(path)) {
                long roomId = Long.parseLong(req.getParameter("roomId"));
                String title = req.getParameter("title");
                Timestamp end = Timestamp.valueOf(req.getParameter("endTime").replace("T", " ") + ":00");

                String[] options = {
                        req.getParameter("opt1"),
                        req.getParameter("opt2")
                };

                pollService.createPoll(roomId, title, end, options);
                resp.sendRedirect(req.getHeader("Referer"));
            }

            else if ("/submit".equals(path)) {
                long pollId = Long.parseLong(req.getParameter("voteId"));
                long optionId = Long.parseLong(req.getParameter("optionId"));

                pollService.submitVote(pollId, userId, optionId);
                resp.sendRedirect(req.getHeader("Referer"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}

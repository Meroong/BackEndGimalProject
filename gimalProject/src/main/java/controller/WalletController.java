package controller;

import java.io.IOException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MeetingService;
import service.WalletService;
import util.AuthUtil;

@WebServlet("/wallet/*")
public class WalletController extends HttpServlet {

    private WalletService walletService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        walletService = new WalletService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        // ⭐ 핵심: 복귀 주소
        String returnUrl = req.getParameter("returnUrl");
        if (returnUrl == null || returnUrl.isBlank() || !returnUrl.startsWith("/")) {
            returnUrl = "/page/mypage";
        }

        Long userId = AuthUtil.getAutoId(req);
        if (userId == -1) {
            resp.sendRedirect(req.getContextPath() + "/page/login");
            return;
        }

        switch (path) {

        // =====================
        // 포인트 충전
        // =====================
        case "/charge": {
            try {
                int amount = Integer.parseInt(req.getParameter("amount"));

                walletService.charge(
                        userId,
                        req.getParameter("cardNumber"),
                        req.getParameter("cvc"),
                        req.getParameter("cardPw"),
                        amount
                );

                int balance = walletService.getBalance(userId);
                req.getSession().setAttribute("walletBalance", balance);
                req.getSession().setAttribute("successMessage", "포인트가 충전되었습니다.");

            } catch (Exception e) {
                req.getSession().setAttribute("errorMessage", e.getMessage());
            }

            resp.sendRedirect(req.getContextPath() + returnUrl);
            return;
        }

        // =====================
        // 모임 회비 결제
        // =====================
        case "/pay": {
            String meetingId = req.getParameter("meetingId");
            String roomId    = req.getParameter("roomId");

            try {
                int amount = Integer.parseInt(req.getParameter("amount"));

                walletService.payForMeeting(userId, amount, "meeting_fee");
                new MeetingService().markAsPaid(Long.parseLong(meetingId), userId);

                int balance = walletService.getBalance(userId);
                req.getSession().setAttribute("walletBalance", balance);
                req.getSession().setAttribute("successMessage", "회비 결제가 완료되었습니다.");

                // ⭐ 채팅 결제는 채팅으로
                resp.sendRedirect(req.getContextPath() + "/chat/room/" + roomId);
                return;

            } catch (Exception e) {
                req.getSession().setAttribute("errorMessage", e.getMessage());
                resp.sendRedirect(req.getContextPath() + returnUrl);
                return;
            }
        }

        default:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

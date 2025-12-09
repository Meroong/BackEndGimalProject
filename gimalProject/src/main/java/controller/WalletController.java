package controller;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.WalletService;
import util.AuthUtil;

@WebServlet("/wallet/*")
public class WalletController extends HttpServlet {

    private WalletService walletService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        walletService = new WalletService();
        System.out.println("walletController: ON");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        switch (path) {

        // 포인트 충전 
        case "/charge":
        	System.out.println("Controller: charge");
            Long autoId = AuthUtil.getAutoId(req);

            if (autoId == -1) {
                resp.sendRedirect(req.getContextPath() + "/views/user/login.jsp");
                return;
            }

            String cardNumber = req.getParameter("cardNumber");
            String cvc = req.getParameter("cvc");
            String cardPw = req.getParameter("cardPw");
            String amountStr = req.getParameter("amount");

            try {
                int amount = Integer.parseInt(amountStr);

                walletService.charge(autoId, cardNumber, cvc, cardPw, amount);

                req.setAttribute("successMessage", "포인트가 충전되었습니다.");

                int balance = walletService.getBalance(autoId);
                req.setAttribute("walletBalance", balance);

                req.getRequestDispatcher("/views/user/mypage.jsp").forward(req, resp);
                return;

            } catch (Exception e) {
                req.setAttribute("errorMessage", e.getMessage());

                try {
                    int balance = walletService.getBalance(autoId);
                    req.setAttribute("walletBalance", balance);
                } catch (Exception ex) {
                    req.setAttribute("walletBalance", 0);
                }

                req.getRequestDispatcher("/views/user/mypage.jsp").forward(req, resp);
                return;
            }

        // 모임 회비 결제 (채팅방에서 사용)
        case "/pay":
            System.out.println("Controller: meeting pay");

            Long userId = AuthUtil.getAutoId(req);

            if (userId == -1) {
                resp.sendRedirect(req.getContextPath() + "/views/user/login.jsp");
                return;
            }

            String meetId = req.getParameter("meetingId");
            String amountStr2 = req.getParameter("amount");

            try {
                int amount = Integer.parseInt(amountStr2);

                walletService.payForMeeting(
                        userId,
                        amount,
                        "모임 회비 결제"
                );

                req.setAttribute("successMessage", "모임 회비 결제가 완료되었습니다.");

                resp.sendRedirect(req.getContextPath() + "/chat/room/" + meetId);
                return;

            } catch (Exception e) {
                req.setAttribute("errorMessage", e.getMessage());
                req.getRequestDispatcher("/views/chat/chat_room.jsp").forward(req, resp);
                return;
            }

        default:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

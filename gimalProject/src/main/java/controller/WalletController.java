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
                req.getSession().setAttribute("walletBalance", balance);

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

            String meetId = req.getParameter("meetingId"); // 회비 기록용
            String roomId = req.getParameter("roomId");    // 채팅방 이동용
            String amountStr2 = req.getParameter("amount");

            try {
                int amount = Integer.parseInt(amountStr2);

                // 회비 차감 처리
                walletService.payForMeeting(
                        userId,
                        amount,
                        "meeting_fee"
                );
                new MeetingService().markAsPaid(Long.parseLong(meetId), userId);
                
                // ⭐ 지갑 잔액 갱신 (세션 업데이트)
                int updatedBalance = walletService.getBalance(userId);
                req.getSession().setAttribute("walletBalance", updatedBalance);
                
                // 성공 처리 후 채팅방으로 이동 (roomId로 이동해야 정상)
                req.getSession().setAttribute("successMessage", "회비 결제가 완료되었습니다.");
                resp.sendRedirect(req.getContextPath() + "/chat/room/" + roomId);
                return;

            } catch (Exception e) {

                String msg = e.getMessage();
                
                // ⭐ 실패 시에도 잔액 한번 갱신해주는 것이 안정적
                try {
                    int updatedBalance = walletService.getBalance(userId);
                    req.getSession().setAttribute("walletBalance", updatedBalance);
                } catch (Exception ignore) {}

                // 포인트 부족 시 마이페이지로 이동
                if (msg != null && msg.contains("포인트")) {
                    req.getSession().setAttribute("errorMessage", "포인트가 부족합니다. 충전 후 다시 결제해주세요.");
                    resp.sendRedirect(req.getContextPath() + "/views/user/mypage.jsp");
                    return;
                }

                // 기타 오류는 다시 채팅방으로
                req.getSession().setAttribute("errorMessage", msg);
                resp.sendRedirect(req.getContextPath() + "/chat/room/" + roomId);
                return;
            }


        default:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

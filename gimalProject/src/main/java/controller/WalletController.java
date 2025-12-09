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

        /* ==========================================================
         * 포인트 충전
         * ========================================================== */
        case "/charge":
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

                //성공 시 메시지 세팅
                req.setAttribute("successMessage", "포인트가 충전되었습니다.");

                // 잔액 다시 조회해서 전달
                int balance = walletService.getBalance(autoId);
                req.setAttribute("walletBalance", balance);

                req.getRequestDispatcher("/views/user/mypage.jsp").forward(req, resp);
                return;

            } catch (Exception e) { // ✅ 전부 RuntimeException 기반 처리
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

        default:
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

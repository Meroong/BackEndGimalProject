package service;

import dao.MockCardDAO;
import dao.WalletDAO;
import dto.UserWalletDTO;

public class WalletService {

    private final WalletDAO walletDAO = new WalletDAO();
    private final MockCardDAO mockCardDAO = new MockCardDAO();

    /* =============================
     * 포인트 잔액 조회
     * ============================= */
    public int getBalance(long userId) {
        UserWalletDTO wallet = walletDAO.findByUserId(userId);

        if (wallet == null) {
            walletDAO.insertNew(userId);
            return 0;
        }

        return wallet.getBalance();
    }

    /* =============================
     * 포인트 충전 (예외 기반)
     * ============================= */
    public void charge(
            long userId,
            String cardNumber,
            String cvc,
            String cardPw,
            int amount
    ) {

        if (amount <= 0) {
            throw new RuntimeException("충전 금액이 올바르지 않습니다.");
        }

        boolean approved = mockCardDAO.checkAndUseCard(
                cardNumber, cvc, cardPw, amount
        );

        if (!approved) {
            throw new RuntimeException("카드 승인에 실패했습니다. 카드 정보 또는 한도를 확인해주세요.");
        }

        UserWalletDTO wallet = walletDAO.findByUserId(userId);
        if (wallet == null) {
            walletDAO.insertNew(userId);
            wallet = new UserWalletDTO();
            wallet.setUserId(userId);
            wallet.setBalance(0);
        }

        int newBalance = wallet.getBalance() + amount;

        walletDAO.updateBalance(userId, newBalance);
        walletDAO.insertHistory(userId, "CHARGE", amount, "카드 충전");
    }
}

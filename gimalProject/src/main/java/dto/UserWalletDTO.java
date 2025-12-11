package dto;

public class UserWalletDTO {

    private long userId;   // user.auto_id
    private int balance;  // 현재 포인트 잔액

    public UserWalletDTO() {}

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}

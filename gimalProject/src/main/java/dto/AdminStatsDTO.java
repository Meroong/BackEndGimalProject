package dto;

public class AdminStatsDTO {

    private int totalUsers;     // 전체 회원 수
    private int totalReports;   // 전체 신고 수

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(int totalReports) {
        this.totalReports = totalReports;
    }
}

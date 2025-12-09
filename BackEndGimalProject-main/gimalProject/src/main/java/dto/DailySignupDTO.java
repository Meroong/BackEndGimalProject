package dto;

import java.sql.Date;

public class DailySignupDTO {
    private Date date;
    private int count;

    public DailySignupDTO() {}

    public DailySignupDTO(Date date, int count) {
        this.date = date;
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

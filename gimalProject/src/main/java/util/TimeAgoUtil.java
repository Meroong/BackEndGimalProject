package util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeAgoUtil {

    public static String format(Timestamp createdAt) {
        if (createdAt == null) {
            return "";
        }

        long now = System.currentTimeMillis();
        
        long nineHours = 9L * 60 * 60 * 1000;   // 9시간(ms)
        long adjustedMillis = createdAt.getTime() - nineHours;
        
//        System.out.println("[DEBUG] now: " + now + " , createdAt: " + adjustedMillis);
        long diffMillis = now - adjustedMillis;
        if (diffMillis < 0) {
            diffMillis = 0;
        }

        long seconds = diffMillis / 1000;
        if (seconds < 60) {
            return "방금 전";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "분 전";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "시간 전";
        }

        long days = hours / 24;
        if (days < 7) {
            return days + "일 전";
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
        return fmt.format(createdAt);
    }
}

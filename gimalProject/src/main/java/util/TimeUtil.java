package util;

import java.sql.Timestamp;

public class TimeUtil {

    public static String toTimeAgo(Timestamp time) {
    	
    	 // 타임이 널인 경우
        if (time == null) {
            return "방금 전";   // 또는 "시간 정보 없음"
        }

        long now = System.currentTimeMillis();     // 현재 시각 (ms)
        long past = time.getTime();                // DB 시각 (ms)

        long diffSec = (now - past) / 1000;        // 초 차이

        if (diffSec < 60) {
            return diffSec + "초 전";
        }

        long diffMin = diffSec / 60;

        if (diffMin < 60) {
            return diffMin + "분 전";
        }

        long diffHour = diffMin / 60;

        if (diffHour < 24) {
            return diffHour + "시간 전";
        }

        long diffDay = diffHour / 24;
        return diffDay + "일 전";
    }
}

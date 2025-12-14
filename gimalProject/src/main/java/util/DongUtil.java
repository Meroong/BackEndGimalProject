package util;

public class DongUtil {
    public String extractAreaUnit(String jibunAddress) {
    	System.out.println("Service: extractAreaUnit");
        if (jibunAddress == null || jibunAddress.isBlank()) {
            return "구로동"; // 기본값
        }

        String[] parts = jibunAddress.split(" ");
        for (int i = parts.length - 1; i >= 0; i--) {
            String p = parts[i];
            if (
                p.endsWith("동") ||
                p.endsWith("읍") ||
                p.endsWith("면") ||
                p.endsWith("리")
            ) {
                return p;
            }
        }
        return "구로동";
    }
}

package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class AuthConfig {
    private static Properties props = new Properties();

    static {
        try (InputStream input = AuthConfig.class.getClassLoader()
                .getResourceAsStream("config/authConfig.properties")) {

            if (input == null) {
                throw new RuntimeException("authConfig.properties 파일을 찾을 수 없습니다.");
            }

            props.load(input);

            if (props.getProperty("JWT_SECRET") == null) {
                throw new RuntimeException("JWT_SECRET 값이 authConfig.properties에 없습니다.");
            }
            if (props.getProperty("JWT_EXPIRATION") == null) {
                throw new RuntimeException("JWT_EXPIRATION 값이 authConfig.properties에 없습니다.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Auth 설정 로드 실패", e);
        }
    }

    public static long getJWTExp() {
        return Long.parseLong(props.getProperty("JWT_EXPIRATION"));
    }

    public static String getJWTSec() {
        String sec = props.getProperty("JWT_SECRET");
        if (sec == null) throw new RuntimeException("JWT_SECRET가 설정되지 않았습니다.");
        return sec;
    }

}



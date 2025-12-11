package util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import service.WeatherService;

import java.util.Timer;
import java.util.TimerTask;

@WebListener
public class WeatherSchedulerListener implements ServletContextListener {

    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("WeatherSchedulerListener 시작");

        timer = new Timer(true); // 데몬 스레드

        // 30분마다 실행 (30 * 60 * 1000 ms)
        long period = 30 * 60 * 1000;

        // 예시: 서울 좌표
        double lat = 37.5665;
        double lon = 126.9780;

        WeatherService service = new WeatherService();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("날씨 API 호출 및 DB 저장 시작");
                service.saveWeather(lat, lon);
            }
        }, 0, period); // 서버 시작 즉시 실행, 이후 30분마다 반복
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (timer != null) {
            timer.cancel();
            System.out.println("WeatherSchedulerListener 종료");
        }
    }
}

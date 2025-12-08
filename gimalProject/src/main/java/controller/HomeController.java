package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import dto.WeatherDTO;
import dto.UserAddressDTO;
import service.WeatherService;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    private WeatherService weatherService = new WeatherService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        double lat, lon;

        // 로그인 여부 확인 후 세션에서 위도/경도 가져오기
        UserAddressDTO address = (UserAddressDTO) session.getAttribute("addressInfo");
        if (address != null && address.getLatitude() != null && address.getLongitude() != null) {
            lat = address.getLatitude();
            lon = address.getLongitude();
        } else {
            // 비로그인 시 디폴트 좌표
            lat = 37.501;
            lon = 126.884;
        }

        // WeatherService 호출: API 호출 후 DB 저장
        weatherService.saveWeather(lat, lon);

        // 최신 날씨 정보 DB에서 가져오기
        WeatherDTO weather = weatherService.getLatestWeather();

        // 날씨가 null이면 기본값 설정 (DB 오류 대비)
        if (weather == null) {
            weather = new WeatherDTO(0.0, "기타", 0, null);
        }

        // 날씨 속성 JSP에 전달
        req.setAttribute("weather", weather);

        // 날씨별 배경 이미지
        String bgImage = getBackgroundImage(weather.getWeather());
        req.setAttribute("bgImage", bgImage);

        // index.jsp로 포워드
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private String getBackgroundImage(String weather) {
        switch (weather) {
            case "맑음": return "resources/images/sunny.png";
            case "흐림": return "resources/images/cloudy.png";
            case "비": return "resources/images/rainy.png";
            case "눈": return "resources/images/snow.png";
            default: return "resources/images/default.png";
        }
    }
}

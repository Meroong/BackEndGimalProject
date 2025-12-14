package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import dto.WeatherDTO;
import dto.MeetingInfoDTO;
import dto.UserAddressDTO;
import service.MeetingService;
import service.WeatherService;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    private WeatherService weatherService = new WeatherService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        double lat = 37.501;
        double lon = 126.884;

        UserAddressDTO address =
            (UserAddressDTO) session.getAttribute("addressInfo");

        // 로그인/비로그인 공통 처리
        if (address != null) {
            if (address.getLatitude() != null) {
                lat = address.getLatitude();
            }
            if (address.getLongitude() != null) {
                lon = address.getLongitude();
            }
        }

        // 좌표만 넘김 (로그인 여부 모름)
        WeatherDTO weather = weatherService.getWeather(lat, lon);

        if (weather == null) {
            weather = new WeatherDTO(0.0, "기타", 0, null);
        }

        req.setAttribute("weather", weather);
        req.setAttribute("lat", lat);
        req.setAttribute("lng", lon);
        req.setAttribute("bgImage",
            getBackgroundImage(weather.getWeather()));

        MeetingService meetingService = new MeetingService();

        req.setAttribute("meetings",
            meetingService.getActiveMeetingsForMap());
        req.setAttribute("popularMeetings",
            meetingService.getPopularMeetings(3));

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }


    private String getBackgroundImage(String weather) {
        switch (weather) {
            case "맑음": return "resources/images/sunny.png";
            case "흐림": return "resources/images/cloudy.png";
            case "비":   return "resources/images/rainy.png";
            case "눈":   return "resources/images/snow.png";
            default:     return "resources/images/default.png";
        }
    }
}

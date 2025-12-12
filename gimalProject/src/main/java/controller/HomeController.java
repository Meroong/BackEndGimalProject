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

        // 로그인 사용자 주소 좌표 사용
        UserAddressDTO address =
            (UserAddressDTO) session.getAttribute("addressInfo");

        if (address != null &&
            address.getLatitude() != null &&
            address.getLongitude() != null) {

            lat = address.getLatitude();
            lon = address.getLongitude();
        }

        //  WeatherService 단일 진입점 호출 ⭐
        WeatherDTO weather = weatherService.getWeather(lat, lon);

        // 안전 장치 (API/DB 오류 대비)
        if (weather == null) {
            weather = new WeatherDTO(0.0, "기타", 0, null);
        }


        String bgImage = getBackgroundImage(weather.getWeather());
        req.setAttribute("bgImage", bgImage);

        List<MeetingInfoDTO> meetings = new MeetingService().getActiveMeetingsForMap();
        for( MeetingInfoDTO dto : meetings) {
        	System.out.println(dto.getLatitude());
        }
        
        //JSP 전달
        req.setAttribute("meetings", meetings);
        req.setAttribute("weather", weather);
        req.setAttribute("lat", lat);
        req.setAttribute("lng", lon);
        req.setAttribute("weather", weather);

        // 포워드
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

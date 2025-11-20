package controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;
import dto.ResponseDTO;
import dto.UserDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@WebServlet("/user/*")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private UserService userService;

    // URL 경로와 실행할 함수를 매핑해두는 Map
    private Map<String, Function<HttpServletRequest, ResponseDTO>> actionMap;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = new UserService();
        actionMap = new HashMap<>();
        
        // "이 경로가 들어오면 -> 이 메서드를 실행해라" 라고 미리 등록
        actionMap.put("/register", userService::registerUser);
        actionMap.put("/login", userService::loginUser);
        actionMap.put("/update", userService::updateUser);
        actionMap.put("/delete", userService::deleteUser);
        actionMap.put("/info", userService::getMyInfo);
        
        // 로그아웃은 별도 로직이 필요하므로 람다식으로 직접 구현
        actionMap.put("/logout", (req) -> {
            req.getSession().invalidate();
            return new ResponseDTO("success", "로그아웃 되었습니다.");
        });
        System.out.println("UserController: ON (Map Initialized)");
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String path = req.getPathInfo();
        System.out.println("요청 경로: " + path);

        // 1. 경로 유효성 검사
        if (path == null || !actionMap.containsKey(path)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            // 2. Map에서 해당 경로에 맞는 메서드를 꺼내서 실행 (apply)
            ResponseDTO result = actionMap.get(path).apply(req);

            // 3. 결과 전송
            if (result != null) {
                sendJson(resp, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDTO error = new ResponseDTO("fail", "서버 에러: " + e.getMessage());
            sendJson(resp, error);
        }
    }

    private void sendJson(HttpServletResponse resp, ResponseDTO result) throws IOException {
        PrintWriter out = resp.getWriter();
        StringBuilder json = new StringBuilder();
        
        json.append("{");
        json.append("\"status\": \"").append(result.getStatus()).append("\",");
        json.append("\"message\": \"").append(result.getMessage()).append("\"");

        if (result.getData() != null && result.getData() instanceof UserDTO) {
            UserDTO user = (UserDTO) result.getData();
            json.append(", \"data\": {");
            json.append("\"userId\": \"").append(user.getUserId()).append("\",");
            json.append("\"userName\": \"").append(user.getUserName()).append("\",");
            json.append("\"nickname\": \"").append(user.getNickname()).append("\",");
            String role = user.getRole() != null ? user.getRole() : "USER";
            json.append("\"role\": \"").append(role).append("\",");
            json.append("\"addressId\": ").append(user.getAddressId()).append(",");
            String addrDetail = user.getAddressDetail() != null ? user.getAddressDetail() : "";
            json.append("\"addressDetail\": \"").append(addrDetail).append("\"");
            json.append("}");
        }

        json.append("}");
        out.print(json.toString());
        out.flush();
    }

}

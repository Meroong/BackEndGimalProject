package controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MeetingService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import dto.MeetingDTO;
import dto.MeetingLocationDTO;

@WebServlet("/meeting/*")
public class MeetingController extends HttpServlet {
	MeetingService meetingService;

	public void init(ServletConfig config) throws ServletException {
		meetingService = new MeetingService();
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		
		switch(path) {
			case "/list":
				ArrayList<MeetingDTO> aList = meetingService.getMeetingList();
				req.getSession().setAttribute("meetingList", aList);
				if(!aList.isEmpty()) {
					resp.sendRedirect(req.getContextPath()+"/views/");
					return;
				}
				else {
					
				}
				break;
			//게시글 상세 조회 미팅 아이디를 인자로 받음
			case "/info":
				MeetingInfoDTO meetingDto = meetingService.getMeetingInfo((long)req.getParameter("meetingId"));
				
		}
}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String path = req.getPathInfo();

	    String latitudeStr = req.getParameter("latitude");
	    String longitudeStr = req.getParameter("longitude");

	    double latitude = (latitudeStr != null && !latitudeStr.isEmpty()) ? Double.parseDouble(latitudeStr) : 37.1;
	    double longitude = (longitudeStr != null && !longitudeStr.isEmpty()) ? Double.parseDouble(longitudeStr) : 107.1;

	    try {
	        switch (path) {

	            /* ========================
	             * UPDATE
	             * ======================== */
	            case "/update":
	                long locationId = Long.parseLong(req.getParameter("locationId"));
	                long meetingId = Long.parseLong(req.getParameter("meetingId"));

	                // location 업데이트
	                meetingService.updateLocation(
	                        locationId,
	                        req.getParameter("roadAddress"),
	                        req.getParameter("jibunAddress"),
	                        req.getParameter("addrDetail"),
	                        latitude,
	                        longitude
	                );

	                // meeting 업데이트
	                String dateStr = req.getParameter("date");
	                dateStr = dateStr.length() == 10 ? dateStr + " 00:00:00" : dateStr;
	                Timestamp date = Timestamp.valueOf(dateStr);

	                meetingService.updateMeetingInfo(
	                        meetingId,
	                        req.getParameter("title"),
	                        req.getParameter("content"),
	                        date,
	                        locationId,
	                        Integer.parseInt(req.getParameter("maxMembers")),
	                        Integer.parseInt(req.getParameter("currentMembers")),
	                        Integer.parseInt(req.getParameter("cost")),
	                        req.getParameter("tag"),
	                        req.getParameter("status"),
	                        latitude,
	                        longitude
	                );

	                // 성공 시
	                resp.sendRedirect(req.getContextPath() + "/weatherAPI.jsp");
	                return;


	            /* ========================
	             * INSERT
	             * ======================== */
	            case "/insert":
	                // location insert
	                long newLocationId = meetingService.insertLocation(
	                        req.getParameter("roadAddress"),
	                        req.getParameter("jibunAddress"),
	                        req.getParameter("addrDetail"),
	                        latitude,
	                        longitude
	                );

	                // meeting insert
	                String dateStrInsert = req.getParameter("date");
	                dateStrInsert = dateStrInsert.length() == 10 ? dateStrInsert + " 00:00:00" : dateStrInsert;

	                meetingService.insertMeetingInfo(
	                        req.getParameter("title"),
	                        req.getParameter("content"),
	                        Timestamp.valueOf(dateStrInsert),
	                        newLocationId,
	                        Integer.parseInt(req.getParameter("maxMembers")),
	                        Integer.parseInt(req.getParameter("currentMembers")),
	                        Integer.parseInt(req.getParameter("cost")),
	                        req.getParameter("tag"),
	                        req.getParameter("status"),
	                        latitude,
	                        longitude
	                );

	                // 성공 시
	                resp.sendRedirect(req.getContextPath() + "/weatherAPI.jsp");
	                return;

	            default:
	                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "잘못된 요청 경로입니다.");
	        }

	    	} catch (Exception e) {
	        e.printStackTrace();

	        // 에러 메시지를 request에 담아서 회원가입/모임 페이지로 포워딩
	        req.setAttribute("errorMsg", e.getMessage());

	        // 기존 입력값도 유지
	        req.setAttribute("roadAddress", req.getParameter("roadAddress"));
	        req.setAttribute("jibunAddress", req.getParameter("jibunAddress"));
	        req.setAttribute("addrDetail", req.getParameter("addrDetail"));
	        req.setAttribute("title", req.getParameter("title"));
	        req.setAttribute("content", req.getParameter("content"));
	        req.setAttribute("date", req.getParameter("date"));
	        req.setAttribute("maxMembers", req.getParameter("maxMembers"));
	        req.setAttribute("currentMembers", req.getParameter("currentMembers"));
	        req.setAttribute("cost", req.getParameter("cost"));
	        req.setAttribute("tag", req.getParameter("tag"));
	        req.setAttribute("status", req.getParameter("status"));

	        // 포워딩
	        req.getRequestDispatcher("/meetingForm.jsp").forward(req, resp);
	    }
	}

}



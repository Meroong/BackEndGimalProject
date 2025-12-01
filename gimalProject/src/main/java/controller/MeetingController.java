package controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MeetingService;

import java.io.IOException;

@WebServlet("/meeting/*")
public class MeetingController extends HttpServlet {
	MeetingService meetingService;

	public void init(ServletConfig config) throws ServletException {
		meetingService = new MeetingService();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		
		switch(path) {
		//테스트용
			case "/update":
				String roadAddress = req.getParameter("roadAddress");
				String jibunAddress = req.getParameter("jibunAddress");
				String addrDetail = req.getParameter("addrDetail");
				String latitude = req.getParameter("latitude");
				String longitude = req.getParameter("longitude");
				
				
				Boolean result = meetingService.postMeeting(roadAddress, jibunAddress, addrDetail, latitude, longitude);
				
				resp.sendRedirect(req.getContextPath()+"/weatherAPI.jsp");
				return;
		}
	}

}

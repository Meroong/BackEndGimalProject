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

import dto.MeetingDTO;
import dto.MeetingLocationDTO;

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
		String latitudeStr;
		String longitudeStr;
	    double latitude = 37.1;
	    double longitude = 107.1;
	    MeetingLocationDTO locationDto = new MeetingLocationDTO();
	    boolean result = false;
		
		switch(path) {
		//테스트용
			case "/update":
				
				latitudeStr = req.getParameter("latitude");
				longitudeStr = req.getParameter("longitude");
				latitude =37.1;
				longitude = 107.1;
				locationDto = new MeetingLocationDTO();
			    
				if (latitudeStr != null && !latitudeStr.isEmpty()) {
					  latitude = Double.parseDouble(latitudeStr);
				  }
				if (longitudeStr != null && !longitudeStr.isEmpty()) {
					  longitude = Double.parseDouble(longitudeStr); 
				  }
			    locationDto.setRoadAddress(req.getParameter("roadAddress"));
			    locationDto.setJibunAddress(req.getParameter("jibunAddress"));
			    locationDto.setAddrDetail(req.getParameter("addrDetail"));
			    locationDto.setLatitude(latitude);
			    locationDto.setLongitude(longitude);
			    locationDto.setId(Long.parseLong(req.getParameter("locationId")));
				
			    //로케이션 업데이트 meeting_location 테이블
				result = meetingService.updateLocation(locationDto);
				
					//모임 정보 업데이트 meeting 테이블
				if(result) {
						 	MeetingDTO meetingDto = new MeetingDTO();
						    meetingDto.setMeetingId(Long.parseLong(req.getParameter("meetingId")));
						    meetingDto.setTitle(req.getParameter("title"));
						    meetingDto.setContent(req.getParameter("content"));
						    
						    //date 파싱
						    String dateStr = req.getParameter("date");
						    dateStr = dateStr.length() == 10 ? dateStr + " 00:00:00" : dateStr;
						    meetingDto.setDate(Timestamp.valueOf(dateStr));
						    
						    meetingDto.setLocationId(locationDto.getId());
						    meetingDto.setMaxMembers(Integer.parseInt(req.getParameter("maxMembers")));
						    meetingDto.setCurrentMembers(Integer.parseInt(req.getParameter("currentMembers")));
						    meetingDto.setCost(Integer.parseInt(req.getParameter("cost")));
						    meetingDto.setTag(req.getParameter("tag"));
						    meetingDto.setStatus(req.getParameter("status"));
						    result = meetingService.updateMeetingInfo(meetingDto, latitude, longitude);
						    if(result) {
								resp.sendRedirect(req.getContextPath()+"/weatherAPI.jsp");
								return;
						    }
						    else
						    	break;
				}
				else
					break;
				
			case "/insert":
				latitudeStr = req.getParameter("latitude");
				longitudeStr = req.getParameter("longitude");
				latitude =37.1;
				longitude = 107.1;
				locationDto = new MeetingLocationDTO();
			    
				if (latitudeStr != null && !latitudeStr.isEmpty()) {
					  latitude = Double.parseDouble(latitudeStr);
				  }
				if (longitudeStr != null && !longitudeStr.isEmpty()) {
					  longitude = Double.parseDouble(longitudeStr); 
				  }
			    locationDto.setRoadAddress(req.getParameter("roadAddress"));
			    locationDto.setJibunAddress(req.getParameter("jibunAddress"));
			    locationDto.setAddrDetail(req.getParameter("addrDetail"));
			    locationDto.setLatitude(latitude);
			    locationDto.setLongitude(longitude);
			    
			    long locationId = 0;
				try {
					 locationId = meetingService.insertLocation(locationDto);
				} catch (Exception e) {
					e.printStackTrace();
			        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "모임 위치 삽입 실패");
			        return;
				}
				
				 	MeetingDTO meetingDto = new MeetingDTO();
				    meetingDto.setTitle(req.getParameter("title"));
				    meetingDto.setContent(req.getParameter("content"));
				    
				    //date 파싱
				    String dateStr = req.getParameter("date");
				    dateStr = dateStr.length() == 10 ? dateStr + " 00:00:00" : dateStr;
				    meetingDto.setDate(Timestamp.valueOf(dateStr));
				    
				    meetingDto.setLocationId(locationId);
				    meetingDto.setMaxMembers(Integer.parseInt(req.getParameter("maxMembers")));
				    meetingDto.setCurrentMembers(Integer.parseInt(req.getParameter("currentMembers")));
				    meetingDto.setCost(Integer.parseInt(req.getParameter("cost")));
				    meetingDto.setTag(req.getParameter("tag"));
				    meetingDto.setStatus(req.getParameter("status"));
				    result = meetingService.insertMeetingInfo(meetingDto, latitude, longitude);
			        
			        if (result) {
			            resp.sendRedirect(req.getContextPath() + "/weatherAPI.jsp");
			            return;
			        } else {
			            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "모임 정보 업데이트 실패");
			            return;
			        }
			    }
		}
}

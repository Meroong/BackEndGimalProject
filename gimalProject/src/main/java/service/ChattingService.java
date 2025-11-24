package service;

import java.sql.Timestamp;
import java.util.ArrayList;

import auth.JwtAuth;
import dao.ChatMessageDAO;
import dao.ChatRoomDAO;
import dao.ChatRoomUserDAO;
import dto.ChatMessageDTO;
import dto.ChatRoomDTO;
import dto.ResponseDTO;
import io.jsonwebtoken.Claims;

public class ChattingService {

    private ChatRoomUserDAO roomUserDao = new ChatRoomUserDAO();
    private ChatRoomDAO roomDao = new ChatRoomDAO();
    private ChatMessageDAO messageDao = new ChatMessageDAO();
    
    // private채팅방 개설
    public ResponseDTO makePrivateRoom(long itemId, String RoomType, long hostId, long receiverId ) {
    	ChatRoomDTO dto = new ChatRoomDTO();
    	boolean isExists = roomDao.isPrivateRoomExists(itemId, hostId, receiverId);
    	
    	if(!isExists) {
    		dto.setItemId(itemId);
        	dto.setRoomType(RoomType);
        	dto.setHostId(hostId);
        	
        	
        	int affectedRow = roomDao.createChatRoom(dto);
        	
        	if(affectedRow >0) {
        		System.out.println("개설 성공");
        		return new ResponseDTO(true, "개인채팅방이 개설되었습니다.");
        	}
        	else {
        		System.out.println("이미 존재하는 개인채팅방 혹은 오류");
        		return new ResponseDTO(false, "개인채팅방 개설 실패");
        		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
        	}
    	}
    	return new ResponseDTO(false, "에러 이미 존재하는 개인채팅방");
    	
    }
    // group 채팅방 개설
    public ResponseDTO makeGroupRoom(long meetingId, String RoomType, long hostId ) {
    	ChatRoomDTO dto = new ChatRoomDTO();
    	boolean isExists = roomDao.isGroupRoomExist(meetingId, hostId);
    	
    	if(!isExists) {
    		dto.setMeetingId(meetingId);
        	dto.setHostId(hostId);
        	
        	int affectedRow = roomDao.createChatRoom(dto);
        	
        	if(affectedRow >0) {
        		System.out.println("개설 성공");
        		return new ResponseDTO(true, "그룹 채팅방이 개설되었습니다.");
        	}
        	else {
        		System.out.println("이미 존재하는 그룹채팅방 혹은 오류");
        		return new ResponseDTO(false, "그룹 채팅방 개설 실패");
        		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
        	}
    	}
    	return new ResponseDTO(false, "에러 이미 존재하는 그룹 채팅방");
    	
    }    

    // 오토아이디 기반으로 채팅방 목록 조회
    public ResponseDTO getRoomList(long autoId) {
        ArrayList<Long> roomList;
        ArrayList<ChatRoomDTO> chatList = new ArrayList<ChatRoomDTO>();
        
        roomList = roomUserDao.getRoomIdsByUser(autoId);

        if (roomList != null) {
            for (Long roomId : roomList) {
                ChatRoomDTO dto = roomDao.getChatRoomById(roomId);
                if (dto != null) {
                	chatList.add(dto);
                    // 콘솔 출력
                    System.out.println(
                        "방 ID: " + dto.getRoomId() +
                        " | 타입: " + dto.getRoomType() +
                        " | 호스트 ID: " + dto.getHostId() +
                        " | 아이템 ID: " + dto.getItemId() +
                        " | 모임 ID: " + dto.getMeetingId() +
                        " | 생성일시: " + dto.getCreatedAt()
                    );
                
                }
            }
            return new ResponseDTO(true, "채팅 리스트 반환 성공", chatList);
        }
        else return new ResponseDTO(false, "채팅 리스트 반환 실패");
    }
    //채팅방에 맞는 채팅가져오기
    public ResponseDTO getMessage (long roomId) {
    	ArrayList <ChatMessageDTO> messageList = messageDao.getMessageByRoomId(roomId);
    	if(messageList.isEmpty()) {
    		return new ResponseDTO(false, "채팅목록이 없습니다.");
    	}
    	else {
            System.out.println("===== 채팅방 " + roomId + " 메시지 목록 =====");
            for (ChatMessageDTO msg : messageList) {
                System.out.println(
                    "[" + msg.getSentAt() + "] " +
                    "보낸사람 ID: " + msg.getSenderId() + " | " +
                    "내용: " + msg.getContent()
                );
            }
            return new ResponseDTO(true, "채팅목록을 가져왔습니다.", messageList);
    	}	 
    }
    
    
    // 채팅방 삭제 (일단 냅두기 ? 관리자용?)
    public ResponseDTO deleteRoomById(long hostId, long roomId ) {
    	int affectedRow = roomDao.deleteChatRoom(hostId, roomId);
    	
    	if(affectedRow >0) {
    		System.out.println("삭제 성공");
    		return new ResponseDTO(true, "삭제 성공");
    	}
    	else {
    		System.out.println("삭제할 데이터가 없습니다.");
    		return new ResponseDTO(false, "삭제 실패");
    		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
    	}
    }
    public ResponseDTO quitRoomById(long userId, long roomId) {
    	int affectedRow = roomUserDao.quitRoom(userId, roomId);
    	
    	if(affectedRow >0) {
    		System.out.println("삭제 성공");
    		return new ResponseDTO(true, "채팅방 나오기 성공");
    	}
    	else {
    		System.out.println("삭제할 데이터가 없습니다.");
    		return new ResponseDTO(false, "채팅방 나오기 실패");
    		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
    	}
    }
    
    //채팅 보내기 
    public ResponseDTO chattingWithUserAndRoomId(long autoId, long roomId, String content) {
    	ChatMessageDTO dto = new ChatMessageDTO();
    	ChatMessageDAO dao = new ChatMessageDAO();
    	
    	//메시지 아이디와 보낸시점은 디비에서 세팅 
        dto.setSenderId(autoId);
        dto.setRoomId(roomId);
        dto.setContent(content);
        
        //디비 인서트 결과를 어펙티드로우로 저장 
    	int affectedRow = dao.sendMessage(dto);
    	
    	if(affectedRow > 0) {
    		return new ResponseDTO(true, "채팅 전송 성공");
    	}
    	else {
    		return new ResponseDTO(false, "채팅전송 실패");
    	}
    }
    //url 통해 방에 접근하는 거 방지용 로직
    public boolean checkUserInRoom(long userId, long roomId) {
        return roomUserDao.isUserInRoom(userId, roomId);
    }
}

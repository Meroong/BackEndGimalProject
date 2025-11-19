package service;

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
    
    //채팅방 개설
    public ResponseDTO makeRoom(String title, long itemId, String RoomType, long hostId ) {
    	ChatRoomDTO dto = new ChatRoomDTO();
    	dto.setItemId(itemId);
    	dto.setRoomType(RoomType);
    	dto.setHostId(hostId);
    	
    	int affectedRow = roomDao.createChatRoom(dto);
    	
    	if(affectedRow >0) {
    		System.out.println("개설 성공");
    		return new ResponseDTO(true, "채팅방이 개설되었습니다.");
    	}
    	else {
    		System.out.println("이미 존재하는 채팅방 혹은 오류");
    		return new ResponseDTO(false, "채팅방 개설 실패");
    		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
    	}
    	
    }

    // 오토아이디 기반으로 채팅방 목록 조회
    public ArrayList<ChatRoomDTO> getRoomList(long autoId) {
        ArrayList<Long> roomList;
        ArrayList<ChatRoomDTO> chatList = new ArrayList<ChatRoomDTO>();
        
        roomList = roomUserDao.getRoomIdsByUser(autoId);

        if (roomList != null) {
            for (Long roomId : roomList) {
                ChatRoomDTO dto = roomDao.getChatRoomById(roomId);
                if (dto != null) chatList.add(dto);
            }
        }

        return chatList;
    }
    //채팅방에 맞는 채팅가져오기
    public ResponseDTO getMessage (long roomId) {
    	ArrayList <ChatMessageDTO> messageList = messageDao.getMessageByRoomId(roomId);
    	if(messageList.isEmpty()) {
    		return new ResponseDTO(false, "채팅목록이 없습니다.");
    	}
    	else return new ResponseDTO(true, "채팅목록을 가져왔습니다.");
    }
    
    
    // 채팅방 삭제 
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
    
    //채팅 보내기 
    public ResponseDTO chattingWithUserAndRoomId() {
    	ChatMessageDTO dto = new ChatMessageDTO();
    	
    	return new ResponseDTO(true, "채팅성공");
}
}

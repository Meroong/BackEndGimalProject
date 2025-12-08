package service;

import java.util.ArrayList;

import dao.ChatMessageDAO;
import dao.ChatRoomDAO;
import dao.ChatRoomUserDAO;
import dao.MeetingParticipantDAO;
import dao.UserDAO;
import dto.ChatMessageDTO;
import dto.ChatRoomDTO;
import dto.MeetingParticipantDTO;
import dto.UserDTO;
import dto.chatParticipantsUserDTO;

public class ChattingService {

    private ChatRoomUserDAO roomUserDao = new ChatRoomUserDAO();
    private ChatRoomDAO roomDao = new ChatRoomDAO();
    private ChatMessageDAO messageDao = new ChatMessageDAO();
    
    // private채팅방 개설
    public boolean makePrivateRoom(long itemId, String RoomType, long hostId, long receiverId ) {
    	System.out.println("work service: makePrivateRoom");
    	ChatRoomDTO dto = new ChatRoomDTO();
    	boolean isExists = roomDao.isPrivateRoomExists(itemId, hostId, receiverId);
    	
    	if(!isExists) {
    		dto.setItemId(itemId);
        	dto.setRoomType(RoomType);
        	dto.setHostId(hostId);
        	
        	
        	int affectedRow = roomDao.createChatRoom(dto);
        	
        	if(affectedRow >0) {
                // 참여자 등록
                roomUserDao.addUserToRoom(hostId, roomId);
                roomUserDao.addUserToRoom(receiverId, roomId);
        		System.out.println("개설 성공");
        		return true;
        	}
        	else {
        		System.out.println("이미 존재하는 개인채팅방 혹은 오류");
        		return false;
        		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
        	}
    	}
    	System.out.println("존재하는 채팅방");
    	return false;
    }
    // group 채팅방 개설
    public boolean makeGroupRoom(long meetingId, String roomType, long hostId ) {
    	System.out.println("work service: makeGroupRoom");
    	ChatRoomDTO dto = new ChatRoomDTO();
    	boolean isExists = roomDao.isGroupRoomExist(meetingId, hostId);
    	
    	if(!isExists) {
    		dto.setMeetingId(meetingId);
        	dto.setHostId(hostId);
        	dto.setRoomType(roomType);
        	
        	int roomId = roomDao.createChatRoom(dto);
        	System.out.println(roomId);
        	if(roomId >0) {
        		System.out.println("개설 성공");
        		roomUserDao.addUserToRoom(hostId, roomId);
        		return true;
        	}
        	else {
        		System.out.println("이미 존재하는 그룹채팅방 혹은 오류");
        		return false;
        		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
        	}
    	}
    	System.out.println("존재하는 채팅방");
    	return false;
    }    

    // 오토아이디 기반으로 채팅방 목록 조회
    public ArrayList<ChatRoomDTO> getRoomList(long autoId) {
    	System.out.println("work service: getRoomList");
        ArrayList<Long> roomList;
        ArrayList<ChatRoomDTO> chatList = new ArrayList<ChatRoomDTO>();
        
        roomList = roomUserDao.getRoomIdsByUser(autoId);

        if (roomList != null) {
            for (Long roomId : roomList) {
                ChatRoomDTO dto = roomDao.getChatRoomInfo(roomId);
                if (dto != null) {
                	chatList.add(dto);
                }
            }
            System.out.println("채팅방 리스트 조회 성공");
            return chatList;
        }
        else {
        	System.out.println("채팅방 리스트 조회 실패 혹은 없음");
        	return chatList;
        }
    }
    
    //룸 아이디로 특정 채팅방 정보 조회
    public ChatRoomDTO getRoomInfo(long roomId) {
    	System.out.println("work service: getRoomInfo");
        return roomDao.getChatRoomInfo(roomId);
    }
    
    public ArrayList<UserDTO> getUserInfoListInRoom(Long roomId) {
        ArrayList<Long> userIds = roomUserDao.getUserInfo(roomId);  // 기존: ID만 가져오는 메서드
        ArrayList<UserDTO> users = new ArrayList<>();

        for (Long id : userIds) {
            UserDTO dto = new UserDAO().searchByAutoId(id); // 기존의 내 정보 조회 재사용 가능
            users.add(dto);
        }
        return users;
    }
    //모임 참가자 정보반환
    public ArrayList<chatParticipantsUserDTO> getParticipantUsers(ChatRoomDTO roomDto) {
    // 그룹 채팅 & 호스트일 때만
    if (!"GROUP".equalsIgnoreCase(roomDto.getRoomType())) return null;

    ArrayList<MeetingParticipantDTO> participants = new MeetingService().getParticipantsInfo(roomDto.getMeetingId());

    ArrayList<chatParticipantsUserDTO> list = new ArrayList<>();
    UserService userService = new UserService();
    ImageService imageService = new ImageService();
    
    long roomId = roomDto.getRoomId();
    
    for (MeetingParticipantDTO p : participants) {

        UserDTO u = userService.getUserInfo(p.getUserId());
        if (u == null) continue;

        String url = imageService.getProfileImage(p.getUserId(), "PROFILE");

        chatParticipantsUserDTO dto = new chatParticipantsUserDTO();

        // meeting participant 정보
        dto.setParticipantId(p.getUserId());
        dto.setMeetingId(p.getMeetingId());
        dto.setPaid(p.isPaid());

        // user 정보
        dto.setUserId(u.getUserId());
        dto.setNickname(u.getNickname());
        dto.setProfileImage(url);
        
        boolean isChatMember = roomUserDao.isUserInRoom(p.getUserId(), roomId);
        dto.setInChat(isChatMember);

        list.add(dto);
    }

    return list;
}
    
    //채팅방에 맞는 채팅가져오기
    public ArrayList <ChatMessageDTO> getMessage (long roomId) {
    	System.out.println("work service: getMessage");
    	ArrayList <ChatMessageDTO> messageList = messageDao.getMessageByRoomId(roomId);
    	if(!messageList.isEmpty()) {
    		System.out.println("채팅목록 조회 성공");
    		return messageList;
    	}
    	else {
    		System.out.println("채팅목록 없음 혹은 조회 실패");
            return messageList;
    	}	 
    }
    
    
    // 채팅방 삭제 (일단 냅두기 ? 관리자용?)
    public boolean deleteRoomById(long hostId, long roomId ) {
    	System.out.println("work service: deleteRoomById");
    	int affectedRow = roomDao.deleteChatRoom(hostId, roomId);
    	
    	if(affectedRow >0) {
    		System.out.println("삭제 성공");
    		return true;
    	}
    	else {
    		System.out.println("삭제할 데이터가 없습니다.");
    		return false;
    		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
    	}
    }
    public boolean quitRoomById(long userId, long roomId) {
    	int affectedRow = roomUserDao.quitRoom(userId, roomId);
    	
    	if(affectedRow >0) {
    		System.out.println("삭제 성공");
    		return true;
    	}
    	else {
    		System.out.println("삭제할 데이터가 없습니다.");
    		return false;
    		//jsp에서 1과 0 값으로 메시지와 페이지 세팅 >0은 성공 아니면 실패 
    	}
    }
    
    //채팅 보내기 
    public boolean chattingWithUserAndRoomId(long autoId, long roomId, String content) {
    	System.out.println("work service: chattingWithUserAndRoomId");
    	ChatMessageDTO dto = new ChatMessageDTO();
    	ChatMessageDAO dao = new ChatMessageDAO();
    	
    	//메시지 아이디와 보낸시점은 디비에서 세팅 
        dto.setSenderId(autoId);
        dto.setRoomId(roomId);
        dto.setContent(content);
        
        //디비 인서트 결과를 어펙티드로우로 저장 
    	int affectedRow = dao.sendMessage(dto);
    	
    	if(affectedRow > 0) {
    		System.out.println("채팅 insert 성공");
    		return true;
    	}
    	else {
    		System.out.println("채팅 insert 실패");
    		return false;
    	}
    }
    //url 통해 방에 접근하는 거 방지용 로직
    public boolean checkUserInRoom(long userId, long roomId) {
        System.out.println("work service: checkUserInRoom");
    	boolean isIn =roomUserDao.isUserInRoom(userId, roomId);
        
        if(isIn) {
        	System.out.println("유효한 유저");
        	return true;
        }
        else {
        	System.out.println("유효하지 않은 유저");
        	return false;
        }
    }
    public boolean inviteUser(long meetId, long roomId, long hostId, long receiverId) throws Exception {
        System.out.println("work service: inviteUser");
        
        // 호스트인지 체크
        if (!roomDao.isHost(roomId, hostId)) {
            throw new Exception("초대 권한이 없습니다. (호스트만 초대 가능)");
        }

        // 초대할 유저가 모임 참가자인지 확인
        if (!new MeetingParticipantDAO().isParticipant(meetId, receiverId)) {
            throw new Exception("유저가 모임 참가자가 아닙니다.");
        }

        // 유저가 이미 채팅방에 있는지 확인
        if (roomUserDao.isUserInRoom(receiverId, roomId)) {
            throw new Exception("이미 채팅방에 있는 유저입니다.");
        }

        // 유저 추가
        boolean result = roomUserDao.addUserToRoom(receiverId, roomId);
        if (result) {
            return true;
        } else {
            throw new Exception("유저 추가 실패");
        }
    }
    public boolean kickUser(long hostId, long roomId, long targetUserId, long meetId) throws Exception {
    	System.out.println("work Service: kickUser");
        // 방장인지 확인
        if (!roomDao.isHost(roomId, hostId)) {
            throw new Exception("방장만 강퇴할 수 있습니다.");
        }
        System.out.println(roomId+" "+targetUserId);
        // 대상 유저가 방에 있는지 확인
        if (!roomUserDao.isUserInRoom(targetUserId, roomId)) {
            throw new Exception("해당 유저는 채팅방에 없습니다.");
        }

        // host 자신은 kick 불가
        if (hostId == targetUserId) {
            throw new Exception("자기 자신은 강퇴할 수 없습니다.");
        }

        // 강퇴 실행 유저 방나가기 dao 재활용
        int chatResult = roomUserDao.quitRoom(targetUserId, roomId);
        if (chatResult <= 0) throw new Exception("채팅방 강퇴 실패");
        
        // 2. 모임 참가자에서 제외
        boolean meetResult = new MeetingService().quitMeet(meetId, targetUserId);
        if (!meetResult) throw new Exception("모임 참가자 강퇴 실패");
        
        else throw new Exception("강퇴 실패");
    }
}

package dto;

import java.util.List;

public class ChatRoomResponseDTO {
    private Long roomId;
    private String roomType;
    private Long itemId;
    private List<ChatRoomUserDTO> participants;
}


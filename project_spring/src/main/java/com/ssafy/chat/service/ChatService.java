package com.ssafy.chat.service;

import com.ssafy.chat.model.ChatMessage;
import com.ssafy.chat.model.ChatRoom;

import java.util.List;

public interface ChatService {
    List<ChatRoom> getAllRooms();
    ChatRoom createRoom(String roomName, String createdBy);
    ChatRoom getRoomById(String roomId);
    boolean enterRoom(String roomId, String userId, int userType);
    boolean leaveRoom(String roomId, String userId);
    void saveMessage(ChatMessage message);
    List<ChatMessage> getMessages(String roomId);
    boolean deleteRoom(String roomId);
    String getUserBySession(String sessionId);
    String getRoomBySession(String sessionId);
}

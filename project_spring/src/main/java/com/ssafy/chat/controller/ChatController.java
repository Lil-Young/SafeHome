package com.ssafy.chat.controller;

import com.ssafy.chat.model.ChatMessage;
import com.ssafy.chat.model.ChatRoom;
import com.ssafy.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat-room")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, ChatService chatService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.register")
    public void register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        String chatRoomId = chatMessage.getChatRoomId();
        chatService.saveMessage(chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/chatRoom/" + chatRoomId, chatMessage);
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        chatService.saveMessage(chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/chatRoom/" + chatMessage.getChatRoomId(), chatMessage);
    }

    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody Map<String, String> request) {
        String roomName = request.get("roomName");
        String createdBy = request.get("createdBy");

        if (roomName == null || createdBy == null || roomName.isBlank() || createdBy.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        ChatRoom createdRoom = chatService.createRoom(roomName, createdBy);
        return ResponseEntity.ok(createdRoom);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoom>> getAllRooms() {
        return ResponseEntity.ok(chatService.getAllRooms());
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    @PostMapping("/{roomId}/enter")
    public ResponseEntity<String> enterRoom(@PathVariable String roomId, @RequestBody Map<String, String> request) {
        String userId = request.get("userId");

        boolean success = chatService.enterRoom(roomId, userId, 1);
        if (success) {
            return ResponseEntity.ok("채팅방 입장 성공");
        }
        return ResponseEntity.badRequest().body("채팅방 입장 실패: 이미 제한 인원이 차 있거나 조건에 맞지 않음");
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<String> leaveRoom(@PathVariable String roomId, @RequestBody Map<String, String> request) {
        String userId = request.get("userId");

        boolean success = chatService.leaveRoom(roomId, userId);
        if (success) {
            return ResponseEntity.ok("채팅방 퇴장 성공");
        }
        return ResponseEntity.badRequest().body("채팅방 퇴장 실패: 방에 없는 사용자");
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable String roomId, @RequestHeader("username") String username) {
        try {
            ChatRoom room = chatService.getRoomById(roomId);

            if (room == null) {
                return ResponseEntity.badRequest().body("채팅방이 존재하지 않습니다.");
            }

            if (!room.getCreatedBy().equals(username)) {
                return ResponseEntity.status(403).body("방 삭제 권한이 없습니다.");
            }

            boolean success = chatService.deleteRoom(roomId);
            if (success) {
                return ResponseEntity.ok("채팅방 삭제 성공");
            }
            return ResponseEntity.status(500).body("채팅방 삭제 중 오류가 발생했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
        }
    }
}
